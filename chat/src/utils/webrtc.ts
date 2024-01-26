import wsIns from "@/utils/websocket";
import {WsRequestMsgType} from "@/utils/wsType";

const clientVersion = "1.0.1";
const clientChannel = "webchat";
// 获取本地音视频流
const getLocalStream = async () => {
    const stream = await navigator.mediaDevices.getUserMedia({ // 获取音视频流
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

// 挂断/拒接
const hangup = (curUid, toUid)=>{
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


export {
    getLocalStream,
    callerRemote,
    calledAccept,
    callerSendOffer,
    calledSendAnswer,
    callerSendCandidate,
    calledSendCandidate,
    hangup,
}