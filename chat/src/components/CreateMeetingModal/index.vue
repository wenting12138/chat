<script setup lang="ts" name="CreateMeetingModal">
import {computed, reactive, ref} from 'vue'
import { ElMessage } from 'element-plus'
import { useMeetStore } from '@/stores/meet'
import { judgeClient } from '@/utils/detectDevice'
import poster from "@/assets/poster.gif";
import {WebrtcConnection} from "@/utils/webrtc";
const client = judgeClient()
const meetStore = useMeetStore()

const dialogFormVisible = ref(true)
const inRoom = ref(false)
const isView = ref(false)
const isMuted = ref(false)
const isBan = ref(false)
const roomFromData = reactive({
    // nickname: JSON.parse(localStorage.getItem("USER_INFO")).name,
    roomId: '',
    roomPw: '',
    radio: '2'
})
const video_full = ref<HTMLVideoElement>()
const show = computed(() => meetStore.meetInfo.show)
const close = () => {
  console.log("关闭了")
  meetStore.clientList.splice(0, meetStore.clientList.length);
  inRoom.value = false;
  dialogFormVisible.value = true;
  roomFromData.roomId = '';
  roomFromData.roomPw = '';
  roomFromData.radio = '2';
  meetStore.meetInfo.show = false
}
const changeView = ()=>{
  console.log("changeView")
}
const changeMicro = ()=>{
  console.log("changeMicro")
}
const createRoom = ()=>{
  console.log("createRoom")
  dialogFormVisible.value = false
  inRoom.value = true;
  meetStore.createRoom(JSON.parse(localStorage.getItem("USER_INFO")).uid, roomFromData);
}
const enterRoom = ()=>{
  console.log("enterRoom")
  dialogFormVisible.value = false
  inRoom.value = true;

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
      <div v-if="inRoom">
        <h2>屏幕</h2>
        <video ref="video_full" style="height:100%" muted autoplay playsinline :poster="poster"/>
        <div>
          <span v-for="(item, index) in meetStore.clientList">
            {{item.name}}
          </span>
        </div>
      </div>

    <div v-if="dialogFormVisible">
      <el-form ref="romeForm" :model="roomFromData" status-icon label-width="100px">
        <el-form-item label="昵称:" prop="nickname">
          <el-input v-model="roomFromData.nickname" maxlength="20" autocomplete="off" />
        </el-form-item>
        <el-form-item label="房间号:" prop="roomId">
          <el-input v-model="roomFromData.roomId" maxlength="10" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码:" prop="roomPw">
          <el-input v-model="roomFromData.roomPw" type="text" maxlength="10" autocomplete="off" />
        </el-form-item>
        <el-form-item label="视频来源:" prop="radio">
          <el-radio v-model="roomFromData.radio" label="1">摄像头</el-radio>
          <el-radio v-model="roomFromData.radio" label="2">电脑屏幕</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="enterRoom()">加 入</el-button>
          <el-button type="primary" @click="createRoom()">创 建</el-button>
        </el-form-item>
      </el-form>
    </div>
  </ElDialog>
</template>

<style lang="scss" src="./styles.scss" scoped />
