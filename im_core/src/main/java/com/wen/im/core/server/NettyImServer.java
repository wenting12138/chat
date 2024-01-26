package com.wen.im.core.server;

import com.alibaba.fastjson2.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wen.im.common.config.ImServerConfig;
import com.wen.im.common.event.IMEvent;
import com.wen.im.common.serirals.SerializeType;
import com.wen.im.common.serirals.Serializer;
import com.wen.im.common.serirals.SerializerManager;
import com.wen.im.common.utils.CommonThreadPool;
import com.wen.im.common.utils.NetworkUtil;
import com.wen.im.common.utils.Pair;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.processors.PingProcessor;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImRequestHeader;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.service.DefaultClientServiceImpl;
import com.wen.im.core.service.IClientService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author wenting
 */
public class NettyImServer implements ImServer {
    public static final Logger log = LoggerFactory.getLogger(NettyImServer.class);
    private ImServerConfig config;
    private final static SerializeType SERIALIZE_TYPE = SerializeType.JSON;
    private final static Serializer SERIALIZER = SerializerManager.getSerializer(SERIALIZE_TYPE);
    private final CommonThreadPool defaultThreadPool;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private IClientService clientService;
    private DefaultMQProducer producer;
    private DefaultMQPushConsumer consumer;
    protected ConcurrentHashMap<Integer, Pair<NettyWebSocketRemotingProcessor, ExecutorService>> processorTable = new ConcurrentHashMap<>();
    protected Pair<NettyWebSocketRemotingProcessor, ExecutorService> defaultRequestProcessorPair;
    protected Map<Integer, IMEvent> eventList = new ConcurrentHashMap<>();

    public NettyImServer(){
        this.clientService = new DefaultClientServiceImpl(this);
        this.defaultThreadPool = new CommonThreadPool();
    }

    public NettyImServer(ImServerConfig config){
        this.config = config;
        this.clientService = new DefaultClientServiceImpl(this);;
        this.defaultThreadPool = new CommonThreadPool();
        initConfigs();
    }

    public void setConfig(ImServerConfig config) {
        this.config = config;
    }

    public CommonThreadPool getDefaultThreadPool() {
        return defaultThreadPool;
    }

    public ConcurrentHashMap<Integer, Pair<NettyWebSocketRemotingProcessor, ExecutorService>> getProcessorTable() {
        return processorTable;
    }

    public void setProcessorTable(ConcurrentHashMap<Integer, Pair<NettyWebSocketRemotingProcessor, ExecutorService>> processorTable) {
        this.processorTable = processorTable;
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    public DefaultMQPushConsumer getConsumer() {
        return consumer;
    }

    public Map<Integer, IMEvent> getEventList() {
        return eventList;
    }

    public void setEventList(Map<Integer, IMEvent> eventList) {
        this.eventList = eventList;
    }

    public void sendMqMsg(ImRequest request) throws UnsupportedEncodingException, RemotingException, InterruptedException, MQClientException {
        Message msg = new Message(
                /* Topic */
                this.config.getRocketMqSendMessageTopic(),
                /* Tag */
                "",
                /* Message body */
                JSON.toJSONString(request).getBytes(RemotingHelper.DEFAULT_CHARSET)
        );
        this.producer.sendOneway(msg);
    }

    public IClientService getClientService() {
        return clientService;
    }


    @Override
    public void registerProcessor(int requestCode, NettyWebSocketRemotingProcessor processor, ExecutorService executor) {
        ExecutorService tmp = executor;
        if (executor == null) {
            tmp = this.defaultThreadPool.getPool();
        }
        Pair<NettyWebSocketRemotingProcessor, ExecutorService> pair = new Pair<>(processor, tmp);
        this.processorTable.put(requestCode, pair);
    }

    @Override
    public void registerProcessor(RequestCode requestCode, NettyWebSocketRemotingProcessor processor, ExecutorService executor) {
        ExecutorService tmp = executor;
        if (executor == null) {
            tmp = this.defaultThreadPool.getPool();
        }
        Pair<NettyWebSocketRemotingProcessor, ExecutorService> pair = new Pair<>(processor, tmp);
        this.processorTable.put(requestCode.getCode(), pair);
    }


    @Override
    public void registerDefaultProcessor(NettyWebSocketRemotingProcessor processor, ExecutorService executor) {
        this.defaultRequestProcessorPair = new Pair<>(processor, executor);
    }

    public void registerIMEvent(IMEvent event){
        this.eventList.put(event.getEventType(), event);
    }

    @Override
    public boolean start() throws MQClientException {
        registerSystemProcessors();
        startService();
        initServer();
        return true;
    }

    private void startService() throws MQClientException {
        producer.start();
        log.info("rocket mq producer start success");
        consumer.start();
        log.info("rocket mq consumer start success");
    }

    public void registerMqListener(MessageListener listener){
        consumer.registerMessageListener(listener);
    }

    private void registerSystemProcessors() {
        this.registerProcessor(RequestCode.PING.getCode(), new PingProcessor(this), null);
    }

    public void initConfigs() {
        try {
            initMqProducer();
            initMqConsumer();
        }catch (Exception e) {
            log.error("error: {}", e);
        }
    }

    private void initMqProducer() throws MQClientException {
        // 实例化消息生产者Producer
        this.producer = new DefaultMQProducer(this.config.getRocketMqSendMessageGroup());
        // 设置NameServer的地址
        producer.setNamesrvAddr(this.config.getRocketMqNameAddr());
    }

    private void initMqConsumer() throws MQClientException {
        // 声明一个消费者consumer，需要传入一个组
        this.consumer = new DefaultMQPushConsumer(this.config.getRocketMqSendMessageGroup());
        // 设置集群的NameServer地址，多个地址之间以分号分隔
        consumer.setNamesrvAddr(this.config.getRocketMqNameAddr());
        // 设置consumer的消费策略
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 集群模式消费，广播消费不会重试
        consumer.setMessageModel(MessageModel.BROADCASTING);
        // 设置最大重试次数，默认是16次
        consumer.setMaxReconsumeTimes(5);
        // 设置consumer所订阅的Topic和Tag，*代表全部的Tag
        consumer.subscribe(this.config.getRocketMqSendMessageTopic(), "*");
    }

    private void initServer() {
        log.info("start server ...");
        try {
            initGroup();
            this.bootstrap = new ServerBootstrap();
            this.bootstrap.group(this.bossGroup, this.workerGroup);

            this.bootstrap.channel(NioServerSocketChannel.class);
            this.bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    NettyImServer.this.initChannels(ch);
                }
            });
            Channel channel = bootstrap.bind(this.config.getPort()).sync().channel();
            log.info("server start success, wsPath: {}, port: {}", this.config.getWsPath(), this.config.getPort());
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("长链接错误: {}", e);
        }finally {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
        }
    }

    private void initGroup() {
        ThreadFactory bossThreadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((thread, throwable) -> {
                    log.error("NettyServerBoss has uncaughtException.");
                    log.error(throwable.getMessage(), throwable);
                })
                .setDaemon(true)
                .setNameFormat("netty-server-boss-%d")
                .build();
        ThreadFactory workerThreadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((thread, throwable) -> {
                    log.error("NettyServerWorker has uncaughtException.");
                    log.error(throwable.getMessage(), throwable);
                })
                .setDaemon(true)
                .setNameFormat("netty-server-worker-%d")
                .build();
        if (this.useEpoll()) {
            this.bossGroup = new EpollEventLoopGroup(bossThreadFactory);
            this.workerGroup = new EpollEventLoopGroup(workerThreadFactory);
        } else {
            this.bossGroup = new NioEventLoopGroup(bossThreadFactory);
            this.workerGroup = new NioEventLoopGroup(workerThreadFactory);
        }
    }

    private void initChannels(SocketChannel ch){
        ch.pipeline().addLast(new IdleStateHandler(120, 0, 0, TimeUnit.SECONDS));
        ch.pipeline().addLast("logging", new LoggingHandler("INFO"));
        ch.pipeline().addLast(new HttpServerCodec());
        ch.pipeline().addLast(new ChunkedWriteHandler());
        ch.pipeline().addLast(new HttpObjectAggregator(8192));
        ch.pipeline().addLast(new WebSocketServerProtocolHandler(this.config.getWsPath()));
        ch.pipeline().addLast(new NettyConnectManageHandler());
        // 自定义的handler ，处理业务逻辑
        ch.pipeline().addLast(new NettyServerHandler());
    }

    public class NettyConnectManageHandler extends ChannelDuplexHandler {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            NettyImServer.this.channelConnection(ctx);
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            NettyImServer.this.channelClose(ctx);
            super.channelInactive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            NettyImServer.this.channelException(ctx, cause);
            super.exceptionCaught(ctx, cause);
        }
    }

    protected void channelConnection(ChannelHandlerContext ctx) throws Exception {
        if (this.clientService != null) {
            this.clientService.onChannelConnection(ctx.channel());
        }
    }

    protected void channelClose(ChannelHandlerContext ctx) throws Exception {
        if (this.clientService != null) {
            this.clientService.onChannelClose(ctx.channel());
        }
    }
    protected void channelException(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (this.clientService != null) {
            this.clientService.onChannelException(ctx.channel(), cause);
        }
    }


    public class NettyServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
            if (msg instanceof PingWebSocketFrame) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("ping"));
                return;
            }
            if (msg instanceof PongWebSocketFrame) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("pong"));
                return;
            }
            if (msg instanceof CloseWebSocketFrame) {
                NettyImServer.this.getClientService().removeClient(ctx.channel());
            }
            if (msg instanceof TextWebSocketFrame) {
                TextWebSocketFrame text = (TextWebSocketFrame) msg;
                ImRequest request = SERIALIZER.deserialize(text.text().getBytes(), ImRequest.class);
                NettyImServer.this.processReceiveMsg(ctx, request);
            }
        }
    }

    public void processReceiveMsg(ChannelHandlerContext ctx, ImRequest request) {
        final Pair<NettyWebSocketRemotingProcessor, ExecutorService> matched = this.processorTable.get(request.getCode());
        final Pair<NettyWebSocketRemotingProcessor, ExecutorService> pair = null == matched ? this.defaultRequestProcessorPair : matched;
        if (pair == null) {
            log.info("request type {} not supported");
            return;
        }
//        if (!this.checkRequestHeader(request)) {
//            ctx.close();
//        }
        pair.getObject2().execute(() -> {
            ImResponse response = null;
            try {
                response = pair.getObject1().handleFrontend(ctx, request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (response != null) {
                ctx.writeAndFlush(new TextWebSocketFrame(new String(SERIALIZER.serialize(response))));
            }
        });
    }

    private boolean checkRequestHeader(ImRequest request) {
        ImRequestHeader header = request.getHeader();
        if (header.getUid() == null || header.getUid().length() == 0) {
            return false;
        }
        if (header.getClientIdentify() == null || header.getClientIdentify().length() == 0) {
            return false;
        }
        if (header.getClientVersion() == null || header.getClientVersion().length() == 0) {
            return false;
        }
        return true;
    }

    protected boolean useEpoll() {
        return NetworkUtil.isLinuxPlatform()
                && Epoll.isAvailable();
    }

    @Override
    public void shutdown() {
        try {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
            this.defaultThreadPool.destroy();

        } catch (Exception e) {
            log.error("im Server shutdown exception, ", e);
        }
    }

    @Override
    public void registerShutdownHook(Runnable task) {
        Runtime.getRuntime().addShutdownHook(new Thread(task));
    }
}
