<script setup lang="ts">
import {computed, onBeforeMount, onMounted, ref, watch} from 'vue'
import {useCallStore} from '@/stores/call'
import {ElMessage} from "element-plus";
import poster from "@/assets/poster.gif";
import { judgeClient } from '@/utils/detectDevice'

const client = judgeClient()
const callStore = useCallStore();
const localVideo = ref<HTMLVideoElement>() // video标签实例，播放本人的视频
const remoteVideo = ref<HTMLVideoElement>() // video标签实例，播放远端的视频
const localStream = computed(() => callStore.callInfo.localStream)
const remoteStream = computed(() => callStore.callInfo.remoteStream)
const calledCreateStream = computed(() => callStore.callInfo.calledCreateStream)
const callerCreateStream = computed(() => callStore.callInfo.callerCreateStream)
const hangupReq = computed(() => callStore.callInfo.hangupReq)
const rejectReq = computed(() => callStore.callInfo.rejectReq)
const cancelReq = computed(() => callStore.callInfo.cancelReq)
const show = computed(() => callStore.callInfo.show)
const called = computed(() => callStore.callInfo.called)
const caller = computed(() => callStore.callInfo.caller)
const calling = computed(() => callStore.callInfo.calling)
const communicating = computed(() => callStore.callInfo.communicating)

// 接收者 接收了offer 才会播放本地视频流
watch(calledCreateStream, async (val, oldVal) => {
    if (called) {
      console.log("addLocaltionStream", localStream.value)
      localVideo.value!.srcObject = localStream.value
      localVideo.value!.play()
    }
})

watch(callerCreateStream, async (val, oldVal) => {
    if (caller && val && !oldVal) {
      console.log("addLocaltionStream", localStream.value)
      localVideo.value!.srcObject = localStream.value
      localVideo.value!.play()
    }
})

watch(communicating, async (val, oldVal) => {
    if (remoteStream.value) {
      console.log("addRemoteStream", remoteStream.value, called.value, caller.value)
      remoteVideo.value!.srcObject = remoteStream.value
      remoteVideo.value!.play()
    }
})

watch(hangupReq, async (val, oldVal) => {
    if (val && !oldVal) {
      let name = caller ? callStore.callInfo.calledName : callStore.callInfo.callerName
      ElMessage.warning(name + "已挂断你的电话")
      // 关闭视频流
      closeVideo()
      let calledTmp = called.value
      callStore.hangUp(false);
      if (calledTmp) {
        callStore.callInfo.show = false
        callStore.reset()
      }
    }
})

watch(rejectReq, async (val, oldVal) => {
  if (val && !oldVal) {
    let name = caller ? callStore.callInfo.calledName : callStore.callInfo.callerName
    ElMessage.warning(name + "已拒绝你的电话")
    // 关闭视频流
    closeVideo()
    callStore.hangUp(false);
  }
})
watch(cancelReq, async (val, oldVal) => {
  if (val && !oldVal) {
    let name = caller ? callStore.callInfo.calledName : callStore.callInfo.callerName
    ElMessage.warning(name + "已取消你的电话")
    // 关闭视频流
    closeVideo()
    callStore.hangUp(false);
  }
})

// 发起方发起视频请求
const callRemote = async () => {
  console.log('发起视频');
  // 发送呼叫请求
  callStore.callerRemoteRequest();
  console.log(callStore.callInfo)
}

// 接收方同意视频请求
const calledAcceptCall = () => {
  callStore.calledAcceptCallRequest()
}

// 挂断视频
const hangUp = () => {
  console.log('挂断视频');
  closeVideo()
  let calledTmp = called.value
  callStore.hangUp(true);
  if (calledTmp) {
    callStore.callInfo.show = false
    callStore.reset()
  }
}
const reject = () => {
  console.log('拒接视频');
  closeVideo()
  callStore.reject(true);
}
const cancel = () => {
  console.log('取消视频');
  closeVideo()
  callStore.cancel(true);
}
const closeVideo=()=>{
  // 关闭视频流
  if(localVideo.value){
    localVideo.value!.srcObject = null
  }
  // if (remoteVideo.value) {
    remoteVideo.value!.srcObject = null
  // }
}
const close = () => {
  closeVideo()
  callStore.reset();
  callStore.callInfo.show = false;
}
</script>

<template>
  <ElDialog
      class="setting-box-modal"
      style="background-color: #fafbff; width: 850px; height: 70vh"
      :model-value="show"
      :width="client === 'PC' ? 350 : '50%'"
      :close-on-click-modal="false"
      center
      :show-close="true"
      @close="close"
  >
    <div style="display: flex; justify-content: flex-end">
      <div style="margin-left: 20px">
        <h2 style="font-family: Arial; color: #2287e1; margin-left: 40px" v-if="communicating">对方: {{caller ? callStore.callInfo.calledName : callStore.callInfo.callerName}}</h2>
        <video
            ref="remoteVideo"
            :poster="poster"
            style="height: 500px; width: 500px; box-sizing: border-box; padding: 0"
            class="w-32 h-48 absolute bottom-0 right-0 object-cover"
        ></video>
      </div>
      <div style="margin-left: 20px">
        <h2 style="font-family: Arial; color: #2287e1; margin-left: 40px" v-if="communicating">我: {{caller ? callStore.callInfo.callerName : callStore.callInfo.calledName}}</h2>
        <video
            ref="localVideo"
            :poster="poster"
            style="height: 200px; width: 200px"
            class="w-96 h-full bg-gray-200 mb-4 object-cover"
        ></video>
      </div>
    </div>
    <ElButton v-if="!calling && !communicating"
              type="success"
              block
              @click="callRemote">
      发起电话
    </ElButton>
    <ElButton v-if="calling && called"
              type="success"
              block
              @click="calledAcceptCall">
      接受电话
    </ElButton>
    <ElButton v-if="communicating"
              type="danger"
              block
              @click="hangUp">
      挂断视频通话
    </ElButton>
    <ElButton v-if="called && calling && !communicating"
              type="danger"
              block
              @click="reject">
      拒接视频通话
    </ElButton>
    <ElButton v-if="caller && calling && !communicating"
              type="danger"
              block
              @click="cancel">
      取消视频通话
    </ElButton>
  </ElDialog>
</template>

<style lang="scss" src="./styles.scss" scoped />
