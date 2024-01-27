import { defineStore } from 'pinia'
import {reactive} from 'vue'
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
} from "@/utils/webrtc";


export const useMeetStore = defineStore('meet', () => {
    const meetInfo = reactive<{
        show: boolean,
        peer: RTCPeerConnection
    }>({
        show: false,
        peer: undefined
    })

    const createLocalStream = (callback) => {
        getLocalStream().then(res => {
            console.log(res)
            callback(res);
        })
        meetInfo.peer = new RTCPeerConnection()
        meetInfo.peer.createOffer().then(offer=>{
            console.log(offer)
        })
    }


    return {
        meetInfo,
        createLocalStream
    }
})

