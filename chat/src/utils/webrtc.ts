import wsIns from "@/utils/websocket";
import {WsRequestMsgType} from "@/utils/wsType";

const clientVersion = "1.0.1";
const clientChannel = "webchat";


class WebrtcConnection {
    callBaseInfo; //基础信息: 呼叫者uid名称等等
    role;  // none, called, caller
    callStatus; // none, calling, communicating
    isDisplay;
    peer;
    localStream;
    remoteStream;
    hangupcallback;
    rejectcallback;
    cancelback;
    busycallback;
    remotestreamback;
    constructor() {
        this.setFree();
    }
    setCallBaseInfo(callerUid, calledUid, callerName, calledName){
        this.callBaseInfo = {
            calledUid: calledUid, callerUid: callerUid,
            callerName: callerName, calledName: calledName
        };
    }
    setCommunicating(){
        this.callStatus = 'communicating';
    }
    setCalling(){
        this.callStatus = 'calling';
    }
    close(){
        this.setFree();
        if (this.peer) {
            this.peer.close()
            this.peer = undefined
        }
        if (this.localStream) {
            for (let track of this.localStream.getTracks()) {
                track.stop();
            }
        }
        if (this.remoteStream) {
            for (let track of this.remoteStream.getTracks()) {
                track.stop();
            }
        }
        this.localStream = undefined;
        this.remoteStream = undefined;
        this.isDisplay = false;
        this.role = 'none';
        this.callStatus = 'none';
        this.callBaseInfo = {};
    }
    registerHangupEvent(callback){
        this.hangupcallback = callback;
    }
    registerRejectEvent(callback){
        this.rejectcallback = callback;
    }
    registerCancelEvent(callback){
        this.cancelback = callback;
    }
    registerBusyEvent(callback){
        this.busycallback = callback;
    }
    registerRemoteStreamEvent(callback){
        this.remotestreamback = callback;
    }
    sendCallReject(){
        if (this.isCalled()) {
            callReject(this.callBaseInfo.calledUid, this.callBaseInfo.callerUid);
        }
    }
    sendCallCancel(){
        if (this.isCaller()) {
            callReject(this.callBaseInfo.callerUid, this.callBaseInfo.calledUid);
        }
    }
    sendCallHangup(){
        console.log(this)
        if (this.isCalled()) {
            callHangup(this.callBaseInfo.calledUid, this.callBaseInfo.callerUid);
        }else {
            callHangup(this.callBaseInfo.callerUid, this.callBaseInfo.calledUid);
        }
    }
    receiveHangup(data){
        this.close()
        if (this.hangupcallback) {
            this.hangupcallback(data);
        }
    }
    receiveReject(data){
        this.close()
        if (this.rejectcallback) {
            this.rejectcallback(data);
        }
    }
    receiveCancel(data){
        this.close()
        if (this.cancelback) {
            this.cancelback(data);
        }

    }
    receiveBusy(data){
        this.close()
        if (this.busycallback) {
            this.busycallback(data);
        }
    }
    isCaller(){
        return this.role === 'caller';
    }
    isCalling(){
        return this.callStatus === 'calling';
    }
    isCommunicating(){
        return this.callStatus === 'communicating';
    }
    isCalled(){
        return this.role === 'called';
    }
    isFree(){
        // 空闲
        return this.role === 'none' && this.callStatus === 'none';
    }
    setFree(){
        this.role = 'none';
        this.callStatus = 'none';
    }
    setCaller(){
        this.role = 'caller';
    }
    setCalled(){
        this.role = 'called';
    }
    async init(){
        this.localStream = await getLocalStream();
        this.peer = new RTCPeerConnection();
        this.peer.onicecandidate = this.iceCandidateCallBack;
        this.peer.oniceconnectionstatechange = this.iceConnectionStateChangeCallBack;
        this.peer.ontrack = this.trackCallBack;
    }
    async connection(callerUid, calledUid, callerName, calledName){
        this.setCallBaseInfo(callerUid, calledUid, callerName, calledName);
        this.setCaller();  // 呼叫者
        this.callStatus = 'calling'  // 呼叫中
        await this.init();
        await this.callerReceiveAcceptCall();

    }
    // 发送请求呼叫
    async sendCallerRemote() {
        console.log(this.callBaseInfo)
        if (this.isFree()) {
            this.setCaller();  // 呼叫者
            this.callStatus = 'calling'  // 呼叫中
            callerRemote(this.callBaseInfo);
            await this.init();
            console.log(this.localStream)
        }
    }
    // 发送同意通话信息
    sendCalledAccept(){
        console.log("接收者同意了建立间接的请求")
        if (this.isCalled()) {
            calledAccept(this.callBaseInfo);
        }
    }
    // 收到同意信令
    async callerReceiveAcceptCall() {
        console.log("接收到了 同意信令", this)
        if (this.isCaller()) {
            // 添加本地音视频流
            for (let track of this.localStream.getTracks()) {
                this.peer.addTrack(track, this.localStream)
            }
            // 生成offer
            const offer = await this.peer.createOffer({
                offerToReceiveAudio: 1,
                offerToReceiveVideo: 1
            })
            console.log('5、发起者向 接收者发送了offer', offer);
            // 设置本地描述的offer
            await this.peer!.setLocalDescription(offer);
            callerSendOffer(this.callBaseInfo, offer);
        }
    }
    // 接收到 offer
    async calledReceiveOffer(offer) {
        if (this.isCalled()) {
            console.log("接收到了offer", offer)
            await this.init();
            // 添加本地音视频流
            this.localStream = await getLocalStream()
            for (let track of this.localStream.getTracks()) {
                this.peer.addTrack(track, this.localStream)
            }
            // 设置远端描述信息
            await this.peer!.setRemoteDescription(offer);
            const answer = await this.peer!.createAnswer()
            console.log("发送answer信令", answer);
            await this.peer!.setLocalDescription(answer);
            calledSendAnswer(this.callBaseInfo, answer)
        }
    }
    async callerReceiveAnswer(answer){
        // 设置远端answer信息
        if (this.isCaller()) {  // 判断是否是发起方
            console.log("接收到了answer信令", answer)
            this.peer!.setRemoteDescription(answer).then(res=>{
                console.log("设置远程描述成功", res)
            }).catch(err=>{
                console.log("设置远程描述失败", err)
            });
        }
    }

    stopDisPlay= async () => {
        this.isDisplay = false;
        for (let track of this.localStream.getTracks()) {
            track.stop();
        }
        this.localStream = await getLocalStream()
        for (let newTrack of this.localStream.getTracks()) {
            this.replaceTrack(newTrack);
        }
    }
    disPlay = async () => {
        this.isDisplay = true;
        for (let track of this.localStream.getTracks()) {
            track.stop();
        }
        this.localStream = await getDisplayStream()
        for (let newTrack of this.localStream.getTracks()) {
            this.replaceTrack(newTrack);
        }
    }

    replaceTrack = (newTrack) => {
        const sender = this.peer.getSenders().find(sender =>
            sender.track.kind === newTrack.kind
        );
        if (!sender) {
            console.warn('failed to find sender');
            return;
        }
        sender.replaceTrack(newTrack);
    }

    async calledReceiveCandidate(candidate){
        console.log(this)
        if (this.isCalled()) {
            if (candidate){
                console.log('获取到candidate信息', candidate);
                this.peer.addIceCandidate(candidate).then(res=> {
                    console.log("success")
                }).catch(e=>{
                    console.log("Error: Failure during addIceCandidate()", e);
                });
            }
        }
    }
    async callerReceiveCandidate(candidate){
        if (this.isCaller()) {
            console.log('获取到了candidate 信息', candidate);
            this.peer.addIceCandidate(candidate).then(res=> {
                console.log("candidate success")
            }).catch(e=>{
                console.log("candidate Error: Failure during addIceCandidate()", e);
            });
        }
    }
    // 呼叫忙
    sendCalledBusy(calledUid, callerUid){
        calledBusy(calledUid, callerUid);
    }
    //
    iceCandidateCallBack = (event: RTCPeerConnectionIceEvent)=>{
        if (event.candidate) {
            console.log('candidate信息', event, event.candidate);
            // 通过信令服务器发送candidate信息给用户A
            if (this.role === 'called') {
                calledSendCandidate(this.callBaseInfo, event.candidate)
            }else {
                callerSendCandidate(this.callBaseInfo, event.candidate)
            }
        }
    }
    trackCallBack = (e: RTCTrackEvent)=>{
        console.log(e)
        if (e && e.streams) {
            console.log("收到对方音频/视频流数据...", e);
            this.remoteStream = e.streams[0];
            if (this.remotestreamback) {
                this.remotestreamback();
            }
            this.setCommunicating();
        }
    }
    iceConnectionStateChangeCallBack = (event: Event)=>{
        if (this.peer!.iceConnectionState === 'connected') {
            console.log("通话已连接")
            this.peer!.getStats(null).then((stats: RTCStatsReport) =>
                stats.forEach((report) => {
                    if (report.type === 'transport') {
                        let activeCandidatePair = stats.get(report.selectedCandidatePairId)
                        let remoteCandidate = stats.get(activeCandidatePair.remoteCandidateId)
                        let localCandidate = stats.get(activeCandidatePair.localCandidateId)
                        // ipv6
                        if (localCandidate && localCandidate.address && localCandidate.address.indexOf(':') !== -1) {
                            console.log(`网络通道:[${localCandidate.address}]:${localCandidate.port}<=>[${remoteCandidate.address}]:${remoteCandidate.port}`)
                        } else {
                            console.log(`网络通道:${localCandidate.address}:${localCandidate.port}<=>${remoteCandidate.address}:${remoteCandidate.port}`)
                        }
                        console.log('本地candidate', localCandidate)
                        console.log('远程candidate', remoteCandidate)
                        this.peer.addIceCandidate(remoteCandidate).then(res => {
                            console.log("success")
                            if (this.remotestreamback) {
                                this.remotestreamback();
                            }
                        }).catch(e => {
                            console.log("Error: Failure during addIceCandidate()", e);
                        });
                    }
                })
            )
        } else {
            console.log('ice状态变更:', event)
        }
    }
}


// 获取本地音视频流
const getLocalStream = async () => {
    const stream = await navigator.mediaDevices.getUserMedia({ // 获取音视频流
        audio: true,
        video: true
    })
    return stream
}

// 屏幕共享
const getDisplayStream = async () => {
    const stream = await navigator.mediaDevices.getDisplayMedia({ // 获取音视频流
        audio: true,
        video: true
    })
    return stream
}

// 发起者向接收者发送通话信令
const callerRemote = (callInfo)=>{
    console.log("1、发起者发送通话请求建立连接")
    wsIns.send({
        type: WsRequestMsgType.CALL_REMOTE,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": callInfo.callerUid
        },
        body: {
            calledUid: callInfo.calledUid,
            callerUid: callInfo.callerUid,
        }
    })
}

// 接收者 同意 请求 信令
const calledAccept = (callInfo)=>{
    wsIns.send({
        type: WsRequestMsgType.ACCEPT_CALL,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": callInfo.calledUid
        },
        body: {
            calledUid: callInfo.calledUid,
            callerUid: callInfo.callerUid,
        }
    })
}

// 发起者发送offer 向 接收者
const callerSendOffer = (callInfo, offer)=>{
    wsIns.send({
        type: WsRequestMsgType.CALLER_SEND_OFFER,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": callInfo.callerUid
        },
        body: {
            calledUid: callInfo.calledUid,
            callerUid: callInfo.callerUid,
            offer: offer
        }
    })
}

// 接收者发送 answer 向 发起者 信令
const calledSendAnswer = (callInfo, answer)=>{
    wsIns.send({
        type: WsRequestMsgType.CALLER_SEND_ANSWER,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": callInfo.calledUid
        },
        body: {
            calledUid: callInfo.calledUid,
            callerUid: callInfo.callerUid,
            answer: answer
        }
    })
}

// 发起者发送candidate向接收方
const callerSendCandidate = (callInfo, candidate)=>{
    console.log("callInfo", callInfo)
    wsIns.send({
        type: WsRequestMsgType.CALLER_SEND_CANDIDATE,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": callInfo.callerUid
        },
        body: {
            calledUid: callInfo.calledUid,
            callerUid: callInfo.callerUid,
            candidate: candidate
        }
    })
}

// 发起者发送 candidate向发起者
const calledSendCandidate = (callInfo, candidate)=>{
    wsIns.send({
        type: WsRequestMsgType.CALLER_SEND_CANDIDATE_2,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": callInfo.calledUid
        },
        body: {
            calledUid: callInfo.calledUid,
            callerUid: callInfo.callerUid,
            candidate: candidate
        }
    })
}

// 挂断
const callHangup = (curUid, toUid)=>{
    wsIns.send({
        type: WsRequestMsgType.SEND_HANG_UP,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": curUid
        },
        body: {
            toUid: toUid
        }
    })
}
// 拒接
const callReject = (curUid, toUid)=>{
    wsIns.send({
        type: WsRequestMsgType.SEND_REJECT,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": curUid
        },
        body: {
            toUid: toUid
        }
    })
}
// 拒接
const callCancel = (curUid, toUid)=>{
    wsIns.send({
        type: WsRequestMsgType.SEND_CANCEL,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": curUid
        },
        body: {
            toUid: toUid
        }
    })
}

// 拒接
const calledBusy = (curUid, toUid)=>{
    wsIns.send({
        type: WsRequestMsgType.SEND_BUSY,
        header: {
            "clientVersion": clientVersion,
            "clientIdentify": clientChannel,
            "uid": curUid
        },
        body: {
            toUid: toUid
        }
    })
}



export {
    getLocalStream,
    getDisplayStream,
    callerRemote,
    calledAccept,
    callerSendOffer,
    calledSendAnswer,
    callerSendCandidate,
    calledSendCandidate,
    callHangup,
    callReject,
    callCancel,
    calledBusy,
    WebrtcConnection,
}