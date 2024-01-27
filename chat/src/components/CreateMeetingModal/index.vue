<script setup lang="ts" name="CreateMeetingModal">
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useMeetStore } from '@/stores/meet'
import { judgeClient } from '@/utils/detectDevice'
const client = judgeClient()
const meetStore = useMeetStore()

const localVideo = ref<HTMLVideoElement>()
const show = computed(() => meetStore.meetInfo.show)
const joinMeet = ref(false)
const joinMeetId = ref()
const close = () => {
  console.log("关闭了")
  meetStore.meetInfo.show = false
}

const closeStream = ()=>{
  const stream = localVideo.value.srcObject;
  if (stream) {
    for (let track of stream.getTracks()) {
      track.stop();
    }
  }
  localVideo.value.srcObject = null
}

const toMeet = async () => {
  console.log("meet: ", joinMeetId.value)
  meetStore.createLocalStream(stream =>{
    localVideo.value.srcObject = stream
    localVideo.value.play()
  })
}

</script>

<template>
  <ElDialog
    class="setting-box-modal"
    :model-value="show"
    :width="client === 'PC' ? 620 : '50%'"
    :close-on-click-modal="false"
    center
    :show-close="true"
    @close="close"
  >
    <div v-if="!joinMeet.value">
      <ElInput           :autosize="{ minRows: 4, maxRows: 4 }"
                         :maxlength="50"
                         resize="none"
                         v-model="joinMeetId"
                         placeholder="请输入会议号"/>
      <button style="margin-top: 10px" @click="toMeet">加入会议</button>

      <video id="video" ref="localVideo" style="width: 100px; height: 100px"></video>
      <button v-if="joinMeetId" @click="closeStream">关闭视频</button>
    </div>
  </ElDialog>
</template>

<style lang="scss" src="./styles.scss" scoped />
