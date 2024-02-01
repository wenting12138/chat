import { defineStore } from 'pinia'
import {reactive, ref} from 'vue'
import { useUserInfo } from '@/hooks/useCached'
import {
    getLocalStream,
    callHangup,
    callReject,
    callCancel,
    getDisplayStream, WebrtcConnection, calledBusy
} from "@/utils/webrtc";

export const useCallStore = defineStore('call', () => {
    const callInfo = reactive<{
        show: boolean,
        calledUid: undefined, // 接收者uid
        calledName: undefined,
        callerUid: undefined, // 拨打者uid
        callerName: undefined,
        isDisplay: boolean,
        hangupReq: boolean,
        rejectReq: boolean,
        cancelReq: boolean,
        busyReq: boolean,
    }>({
        show: false,  // 是否展示页面
        calledUid: undefined, // 接收者uid
        calledName: undefined,
        callerUid: undefined, // 拨打者uid
        callerName: undefined,
        isDisplay: false,
        hangupReq: false,
        rejectReq: false,
        cancelReq: false,
        busyReq: false,
    })
    const receiveHangup = (fromUid)=> {
        callInfo.hangupReq = true
    }
    const receiveReject = (fromUid)=> {
        callInfo.rejectReq = true
    }
    const receiveCancel = (fromUid)=> {
        callInfo.cancelReq = true
    }
    const receiveBusy = (fromUid)=> {
        callInfo.busyReq = true
    }
    const wc = ref<WebrtcConnection>(new WebrtcConnection());
    wc.value.registerBusyEvent(receiveBusy);
    wc.value.registerCancelEvent(receiveCancel);
    wc.value.registerHangupEvent(receiveHangup);
    wc.value.registerRejectEvent(receiveReject);
    const caller = ref<boolean>(false);
    const called = ref<boolean>(false);
    const calling = ref<boolean>(false);
    const communicating = ref<boolean>(false);
    // reset
    const reset = ()=>{
        called.value = false;
        caller.value = false;
        calling.value = false;
        communicating.value = false;
        callInfo.calledUid = undefined;
        callInfo.callerUid = undefined;
        callInfo.callerName = undefined;
        callInfo.calledName = undefined;
        callInfo.isDisplay = undefined;
        callInfo.hangupReq = undefined;
        callInfo.rejectReq = undefined;
        callInfo.cancelReq = undefined;
        callInfo.busyReq = undefined;
        wc.value.close()
    }
    // 拒接
    const reject = (isSendSignal)=>{
        if (isSendSignal) {
            wc.value.sendCallReject();
        }
        reset();
    }
    // 取消
    const cancel = (isSendSignal)=>{
        if (isSendSignal) {
            wc.value.sendCallCancel();
        }
        reset();
    }

    // 挂断
    const hangUp = (isSendSignal)=>{
        // 发送hangup信令
        if (isSendSignal) {
            wc.value.sendCallHangup();
        }
        reset();
    }

    // 发送请求
    const callerRemoteRequest = async (callback) => {
        wc.value.setCallBaseInfo(callInfo.callerUid, callInfo.calledUid, callInfo.callerName, callInfo.calledName);
        caller.value = true;
        calling.value = true;
        await wc.value.sendCallerRemote();
        wc.value.registerRemoteStreamEvent(callback);
    };
    // 接收者收到请求通话
    const calledReceiveRemoteRequest = (body)=>{
        console.log("2、接收者收到发起者的建立连接请求", wc)
        if (wc.value && (wc.value.isCalling() || wc.value.isCommunicating())) {
            wc.value.sendCalledBusy(body.calledUid, body.callerUid);
        }else {
            // 弹框打开
            callInfo.show = true
            callInfo.callerUid = body.callerUid
            callInfo.calledUid = body.calledUid
            callInfo.calledName = useUserInfo(body.calledUid).value.name
            callInfo.callerName = useUserInfo(body.callerUid).value.name
            wc.value.setCalled();
            wc.value.setCalling();
            called.value = true;
            calling.value = true;
            wc.value.setCallBaseInfo(callInfo.callerUid, callInfo.calledUid, callInfo.callerName, callInfo.calledName);
        }
    }
    // 接收者点击同意按钮
    const calledAcceptCallRequest = (callback)=>{
        wc.value.sendCalledAccept();
        wc.value.registerRemoteStreamEvent(callback);
    }

    const changeDisplayStream = async (refresh) => {
        await wc.value.disPlay();
        refresh();
        callInfo.isDisplay = true
    }
    const stopDisplay = async (refresh) => {
        await wc.value.stopDisPlay();
        refresh();
        callInfo.isDisplay = false
    }


    return{
        callInfo,
        caller,
        called,
        calling,
        communicating,
        wc,
        changeDisplayStream,
        callerRemoteRequest,
        calledReceiveRemoteRequest,
        calledAcceptCallRequest,
        reset,
        hangUp,
        reject,
        cancel,
        receiveHangup,
        receiveReject,
        receiveCancel,
        receiveBusy,
        stopDisplay,
    }
})