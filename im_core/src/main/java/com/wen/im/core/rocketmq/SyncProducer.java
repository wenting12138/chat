package com.wen.im.core.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

public class SyncProducer {
	public static void main(String[] args) throws Exception {
//    	// 实例化消息生产者Producer
//        DefaultMQProducer producer = new DefaultMQProducer("my_im_chat");
//    	// 设置NameServer的地址
//    	producer.setNamesrvAddr("47.96.80.134:9876");
//    	// 启动Producer实例
//        producer.start();
//    	for (int i = 0; i < 100; i++) {
//    	    // 创建消息，并指定Topic，Tag和消息体
//    	    Message msg = new Message("TopicTest" /* Topic */,
//        	"TagA" /* Tag */,
//        	("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
//        	);
//        	// 发送消息到一个Broker
//            producer.sendOneway(msg);
//    	}
//    	// 如果不再发送消息，关闭Producer实例。
//    	producer.shutdown();

		rocketMQConsumer();
    }

	public static void rocketMQConsumer() {
		try {
			System.out.println("rocketMQConsumer  开始------");
			// 消费目标
			// 声明一个消费者consumer，需要传入一个组
			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my_im_chat");
			// 设置集群的NameServer地址，多个地址之间以分号分隔
			consumer.setNamesrvAddr("47.96.80.134:9876");
			// 设置consumer的消费策略
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			// 集群模式消费，广播消费不会重试
			consumer.setMessageModel(MessageModel.CLUSTERING);
			// 设置最大重试次数，默认是16次
			consumer.setMaxReconsumeTimes(5);
			// 设置consumer所订阅的Topic和Tag，*代表全部的Tag
			consumer.subscribe("TopicTest", "*");
			// Listener，主要进行消息的逻辑处理,监听topic，如果有消息就会立即去消费
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					// 获取第一条消息，进行处理
					try {
						if (msgs != null && msgs.size() > 0) {
							MessageExt messageExt = msgs.get(0);
							String msgBody = new String(messageExt.getBody(), "utf-8");
							System.out.println(" 接收消息整体为：" + msgBody);
						}
					} catch (Exception e) {
						System.out.println("消息消费失败，请尝试重试！！！");
						e.printStackTrace();
						// 尝试重新消费，直接第三次如果还不成功就放弃消费，进行消息消费失败补偿操作
						if (msgs.get(0).getReconsumeTimes() == 3) {
							System.out.println("消息记录日志：" + msgs);
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
						} else {
							// 重试状态码，重试机制可配置
							// System.out.println("消息消费失败，尝试重试！！！");
							System.out.println("消息消费失败，请尝试重试！！！");
							return ConsumeConcurrentlyStatus.RECONSUME_LATER;
						}
					}
					System.out.println("消息消费成功！！！");
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			// 调用start()方法启动consumer
			consumer.start();
			System.out.println("消费者启动成功。。。");
			System.out.println("rocketMQConsumer 结束------");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("消息消费操作失败--" + e.getMessage());
		}
	}
}