<script setup lang="ts">
import {computed, ref, watch} from 'vue'
import {useCallStore} from '@/stores/call'

const callStore = useCallStore();
const localVideo = ref<HTMLVideoElement>() // video标签实例，播放本人的视频
const remoteVideo = ref<HTMLVideoElement>() // video标签实例，播放远端的视频
const localStream = computed(() => callStore.callInfo.localStream)
const remoteStream = computed(() => callStore.callInfo.remoteStream)
const calledCreateStream = computed(() => callStore.callInfo.calledCreateStream)
const callerCreateStream = computed(() => callStore.callInfo.callerCreateStream)
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

// 发起方发起视频请求
const callRemote = async () => {
  console.log('发起视频');
  // 发送呼叫请求
  callStore.callerRemoteRequest();
}

// 接收方同意视频请求
const calledAcceptCall = () => {
  callStore.calledAcceptCallRequest()
}

// 挂断视频
const hangUp = () => {
  console.log('挂断视频');
  // 关闭视频流
  localVideo.value!.srcObject = null
  remoteVideo.value!.srcObject = null
  callStore.hangUp();
}
const close = () => {
  localVideo.value!.srcObject = null
  remoteVideo.value!.srcObject = null
  callStore.reset();
}
</script>

<template>
  <ElDialog
      class="setting-box-modal"
      :model-value="show"
      :width="client === 'PC' ? 350 : '50%'"
      :close-on-click-modal="false"
      center
      :show-close="true"
      @close="close"
  >
    <div class="flex items-center flex-col text-center p-12 h-screen">
      <div class="relative h-full mb-4">
        <video
            ref="localVideo"
            class="w-96 h-full bg-gray-200 mb-4 object-cover"
        ></video>
        <video
            ref="remoteVideo"
            class="w-32 h-48 absolute bottom-0 right-0 object-cover"
        ></video>
        <div v-if="caller && calling" class="absolute top-2/3 left-36 flex flex-col items-center">
          <p class="mb-4 text-white">等待对方接听...</p>
          <img @click="hangUp" src="" class="w-16 cursor-pointer" alt="">
        </div>
        <div v-if="called && calling" class="absolute top-2/3 left-32 flex flex-col items-center">
          <p class="mb-4 text-white">收到视频邀请...</p>
          <div class="flex">
            <button
                class="rounded-md bg-red-600 px-4 py-2 text-sm font-semibold text-white"
                @click="hangUp"
            >拒绝</button>
            <button
                class="rounded-md bg-red-600 px-4 py-2 text-sm font-semibold text-white"
                @click="calledAcceptCall"
            >接受</button>
          </div>
        </div>
      </div>
      <div class="flex gap-2 mb-4">
        <button
            class="rounded-md bg-indigo-600 px-4 py-2 text-sm font-semibold text-white"
            @click="callRemote"
            v-if="!calling && !communicating"
        >发起视频</button>
        <button
            class="rounded-md bg-red-600 px-4 py-2 text-sm font-semibold text-white"
            @click="hangUp"
            v-if="(!called && calling) || communicating"
        >挂断视频</button>
      </div>
    </div>
  </ElDialog>
</template>

<style lang="scss" src="./styles.scss" scoped />
