import { defineStore } from 'pinia'
import {reactive} from 'vue'
import { useUserInfo } from '@/hooks/useCached'
import {
    getLocalStream,
    callerRemote,
    calledAccept,
    callerSendOffer,
    calledSendAnswer,
    callerSendCandidate,
    calledSendCandidate,
    callHangup,
    callReject,
    callCancel
} from "@/utils/webrtc";
import {types} from "sass";
import List = types.List;

export const useCallStore = defineStore('call', () => {
    const callInfo = reactive<{
        show: boolean,
        calledUid: number
        calledName: string
        callerUid: number,
        callerName: string,
        called: boolean,
        caller: boolean,
        calling: boolean,
        communicating: boolean,
        localStream: MediaStream,
        peer: RTCPeerConnection,
        remoteStream: MediaStream,
        calledCreateStream: boolean,
        callerCreateStream: boolean,
        hangupReq: boolean,
        rejectReq: boolean,
        cancelReq: boolean,
    }>({
        show: false,  // 是否展示页面
        calledUid: undefined, // 接收者uid
        calledName: undefined,
        callerUid: undefined, // 拨打者uid
        callerName: undefined,
        called: false,   // 是否是接收方
        caller: false,  // 是否是发起方
        calling: false,  // 呼叫中
        communicating: false, // 视频通话中
        localStream: undefined,
        peer: undefined,
        remoteStream: undefined,
        calledCreateStream: false,
        callerCreateStream: false,
        hangupReq: false,
        rejectReq: false,
        cancelReq: false,
    })
    const close = ()=>{
        callInfo.called = false;
        callInfo.caller = false;
        callInfo.calling = false;
        callInfo.communicating = false;
        if (callInfo.peer) {
            callInfo.peer.close()
            callInfo.peer = undefined
        }
        if (callInfo.localStream) {
            for (let track of callInfo.localStream.getTracks()) {
                track.stop();
            }
        }
        if (callInfo.remoteStream) {
            for (let track of callInfo.remoteStream.getTracks()) {
                track.stop();
            }
        }
        callInfo.localStream = undefined;
        callInfo.remoteStream = undefined;
        callInfo.calledCreateStream = false;
        callInfo.callerCreateStream = false;
        callInfo.hangupReq = false;
        callInfo.rejectReq = false;
        callInfo.cancelReq = false;
    }
    // reset
    const reset = ()=>{
        callInfo.calledUid = undefined;
        callInfo.calledName = undefined;
        callInfo.callerUid = undefined;
        callInfo.callerName = undefined;
        close()
    }
    // 拒接
    const reject = (isSendSignal)=>{
        if (isSendSignal) {
            if (callInfo.called) {
                callReject(callInfo.calledUid, callInfo.callerUid);
            }
        }
        close()
    }
    // 取消
    const cancel = (isSendSignal)=>{
        if (isSendSignal) {
            if (callInfo.caller) {
                callCancel(callInfo.callerUid, callInfo.calledUid);
            }
        }
        close()
    }

    // 挂断
    const hangUp = (isSendSignal)=>{
        // 发送hangup信令
        if (isSendSignal) {
            if (callInfo.called) {
                callHangup(callInfo.calledUid, callInfo.callerUid);
            }else {
                callHangup(callInfo.callerUid, callInfo.calledUid);
            }
        }
        close();
    }

    const receiveHangup = (fromUid)=> {
        callInfo.hangupReq = true
    }
    const receiveReject = (fromUid)=> {
        callInfo.rejectReq = true
    }

    const receiveCancel = (fromUid)=> {
        callInfo.cancelReq = true
    }


    const bindChangeEvent = ()=>{
        callInfo.peer.oniceconnectionstatechange = (event: Event) => {
            if (callInfo.peer!.iceConnectionState === 'connected') {
                callInfo.peer!.getStats(null).then((stats: RTCStatsReport) =>
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
                            callInfo.peer.addIceCandidate(remoteCandidate).then(res => {
                                console.log("success")
                            }).catch(e => {
                                console.log("Error: Failure during addIceCandidate()", e);
                            });
                        }
                    })
                )
            } else {
                console.log('ice状态变更:')
            }

        }}

    // 发送请求
    const callerRemoteRequest = async () => {
        callInfo.localStream = await getLocalStream();
        // 标记 是 发起者
        callInfo.caller = true
        // 通话中
        callInfo.calling = true;
        // 发送信令
        callerRemote(callInfo);
        callInfo.callerCreateStream = true
    };
    // 接收者收到请求通话
    const calledReceiveRemoteRequest = (body)=>{
        console.log("2、接收者收到发起者的建立连接请求")
        callInfo.called = true // 表示是接收方
        callInfo.calling = true  // 通话中
        callInfo.show = true    // 弹框打开
        callInfo.callerUid = body.callerUid
        callInfo.callerName = useUserInfo(body.callerUid).value.name
        callInfo.calledUid = body.calledUid
        callInfo.calledName = useUserInfo(body.calledUid).value.name
    }
    // 接收者同意了请求通话
    const calledAcceptCallRequest = ()=>{
        if (callInfo.called) {
            console.log(callInfo.calledUid)
            console.log(callInfo.callerUid)
            console.log(callInfo.called)
            console.log(callInfo.caller)
            console.log("3、接收者同意了建立间接的请求")
            calledAccept(callInfo);
        }
    }
    // 发起者收到了接收者的同意
    const callerReceiveAcceptCall = ()=> {
        console.log("4、发起者收到了接收者同意建立间接的请求")
        callerCreatePeer()
    }

    const callerCreatePeer = async () => {
        if (callInfo.caller) {
            // 创建RTCPeerConnection
            callInfo.peer = new RTCPeerConnection()
            // 绑定 candidate事件
            // 通过监听onicecandidate事件获取candidate信息
            callInfo.peer!.onicecandidate = (event: any) => {
                if (event.candidate) {
                    console.log('9、发起者向接收者同步了 candidate ', event.candidate);
                    // 通过信令服务器发送candidate信息给用户B
                    callerSendCandidate(callInfo, event.candidate)
                }
            }
            bindChangeEvent()
            callInfo.peer!.ontrack = (e) => {
                if (e && e.streams) {
                    console.log("收到对方音频/视频流数据...");
                    callInfo.remoteStream = e.streams[0];
                    callInfo.communicating = true;
                    callInfo.calling = false;
                }
            };
            // 添加本地音视频流
            callInfo.peer!.addStream(callInfo.localStream)
            // 生成offer
            const offer = await callInfo.peer.createOffer({
                offerToReceiveAudio: 1,
                offerToReceiveVideo: 1
            })
            console.log('5、发起者向 接收者发送了offer', offer);
            // 设置本地描述的offer
            await callInfo.peer!.setLocalDescription(offer);
            callerSendOffer(callInfo, offer);
        }
    }

    // 接收者接收到了发起者的offer
    const calledReceiveOffer = async (offer) => {
        if (callInfo.called) {
            console.log("6、接收者接收到了发起者的offer", offer)
            // 创建自己的RTCPeerConnection
            callInfo.peer = new RTCPeerConnection()
            // 绑定 candidate事件
            // 通过监听onicecandidate事件获取candidate信息
            callInfo.peer!.onicecandidate = (event: any) => {
                if (event.candidate) {
                    console.log('11、接收者向发起者的发送了自己的candidate信息', event, event.candidate);
                    // 通过信令服务器发送candidate信息给用户A
                    calledSendCandidate(callInfo, event.candidate)
                }
            }
            bindChangeEvent()
            callInfo.peer!.ontrack = (e) => {
                if (e && e.streams) {
                    console.log("收到对方音频/视频流数据...");
                    callInfo.remoteStream = e.streams[0];
                    callInfo.communicating = true;
                    callInfo.calling = false;
                }
            };
            // 添加本地音视频流
            callInfo.localStream = await getLocalStream()
            callInfo.peer.addStream(callInfo.localStream)
            callInfo.calledCreateStream = true;
            // 设置远端描述信息
            await callInfo.peer!.setRemoteDescription(offer);
            const answer = await callInfo.peer!.createAnswer()
            console.log("7、接收者向发起者发送了answer信令" ,answer);
            await callInfo.peer!.setLocalDescription(answer);
            calledSendAnswer(callInfo, answer)
        }
    }

    // 发起者接收到了answer
    const callerReceiveAnswer = (answer) => {
        // 设置远端answer信息
        if (callInfo.caller) {  // 判断是否是发起方
            console.log("8、发起者接收到了接收者的 answer信令", answer)
            callInfo.peer!.setRemoteDescription(answer).then(res=>{
                console.log("设置远程描述成功", res)
            }).catch(err=>{
                console.log("设置远程描述失败", err)
            });
        }
    }

    // 接收方接收到发起方的candidate
    const calledReceiveCandidate = async (candidate) => {
        if (callInfo.called) {
            if (candidate){
                console.log('10、接收者获取到了发起者的candidate信息', candidate);
                callInfo.peer.addIceCandidate(candidate).then(res=> {
                    console.log("success")
                }).catch(e=>{
                    console.log("Error: Failure during addIceCandidate()", e);
                });
            }
        }
    }
    // 发起方  接收到 接收方 的 candidate
    const callerReceiveCandidate = async (candidate) => {
        if (callInfo.caller) {
            console.log('12、发起者获取到了接收者的candidate 信息', candidate);
            callInfo.peer.addIceCandidate(candidate).then(res=> {
                console.log("candidate success")
            }).catch(e=>{
                console.log("candidate Error: Failure during addIceCandidate()", e);
            });
        }
    }

    return{
        callInfo,
        callerRemoteRequest,
        calledReceiveRemoteRequest,
        calledAcceptCallRequest,
        callerReceiveAcceptCall,
        calledReceiveOffer,
        callerReceiveAnswer,
        calledReceiveCandidate,
        callerReceiveCandidate,
        reset,
        hangUp,
        reject,
        cancel,
        receiveHangup,
        receiveReject,
        receiveCancel,
    }
})