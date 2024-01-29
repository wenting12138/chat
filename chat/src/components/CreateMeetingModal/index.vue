<script setup lang="ts" name="CreateMeetingModal">
import {computed, reactive, ref} from 'vue'
import { ElMessage } from 'element-plus'
import Chat from '@/components/MeetingChat/index.vue'
import Priview from '@/components/Priview/index.vue'
import { useMeetStore } from '@/stores/meet'
import { judgeClient } from '@/utils/detectDevice'
import {types} from "sass";
const client = judgeClient()
const meetStore = useMeetStore()
type Client = {
  uid: number,
  name: string,
  roomId: string,
  localStream: MediaStream,
  peerConnection: RTCPeerConnection,
  muted: boolean,
  view: boolean,
  chat: boolean,
  isSelf: boolean,
  isRoomAdmin: boolean,
  nowStream: string
}
const dialogFormVisible = ref(true)
const inRoom = ref(false)
const isView = ref(false)
const isMuted = ref(false)
const isBan = ref(false)
const roomFromData = reactive({
    nickname: '',
    roomId: '',
    roomPw: '',
    radio: '2'
})
const clientList = reactive<Client[]>([])
const video_full = ref<HTMLVideoElement>()
const show = computed(() => meetStore.meetInfo.show)
const close = () => {
  console.log("关闭了")
  meetStore.meetInfo.show = false
}
const closeView = ()=>{
  console.log("关闭view")
}
const changeView = ()=>{
  console.log("changeView")
}
const changeMicro = ()=>{
  console.log("changeMicro")
}
const ban = ()=>{
  console.log("changeMicro")
}
const createOrEnterRoom = ()=>{
  console.log("createOrEnterRoom")
  dialogFormVisible.value = false
  inRoom.value = true;
  clientList.push({
    uid: JSON.parse(localStorage.getItem("USER_INFO")).uid,
    name: roomFromData.nickname,
    roomId: roomFromData.roomId,
    localStream: undefined,
    peerConnection: undefined,
    muted: false,
    view: true,
    chat: true,
    isSelf: true,
    isRoomAdmin: true,
    nowStream: 'screen'
  })
}
const notice = (msg)=>{
  console.log('notice:' + msg)
}
const receiveMsg = (msg)=>{
  console.log('receiveMsg:' + msg)
}
const fullScreen = (msg)=>{
  console.log('fullScreen:' + msg)
}
const changeStream = (msg)=>{
  console.log('changeStream:' + msg)
}
const kick = (msg)=>{
  console.log('kick:' + msg)
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
        <el-header height="214px">
          <template v-for="(client) in clientList">
            <preview
                v-if="client!==undefined"
                :key="client.uid"
                :client="client"
                :is-room-admin="clientList[0].isRoomAdmin"
                @banEvent="ban"
                @microEvent="changeMicro"
                @fullEvent="fullScreen"
                @kickEvent="kick"
                @viewEvent="changeView"
                @changeStreamEvent="changeStream"
            />
          </template>
        </el-header>
        <el-main>
          <div style="text-align: center ;height: calc(100vh - 300px);width: 100%">
            <video ref="video_full" style="height:100%" muted autoplay playsinline />
          </div>
        </el-main>
        <el-aside width="350px">
          <Chat :receive-msg="receiveMsg" @chatEvent="sendChat" @noticeEvent="notice" />
          <div style="text-align: center;margin-top: 10px">
            <el-button v-if="clientList[0].isRoomAdmin" type="danger" round size="small" @click="changeView('')">
              <span v-show="isView">
                全体禁视
              </span>
              <span v-show="!isView">
                取消禁视
              </span>
            </el-button>
            <el-button v-if="clientList[0].isRoomAdmin" type="danger" round size="small" @click="changeMicro('')">
              <span v-show="!isMuted">
                全体禁音
              </span>
              <span v-show="isMuted">
                取消禁音
              </span></el-button>
            <el-button v-if="clientList[0].isRoomAdmin" type="danger" round size="small" @click="ban('')">
              <span v-show="!isBan">
                全体禁言
              </span>
              <span v-show="isBan">
                取消禁言
              </span></el-button>
          </div>
        </el-aside>
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
          <el-input v-model="roomFromData.roomPw" type="password" maxlength="10" autocomplete="off" />
        </el-form-item>
        <el-form-item label="视频来源:" prop="roomPw">
          <el-radio v-model="roomFromData.radio" label="1">摄像头</el-radio>
          <el-radio v-model="roomFromData.radio" label="2">电脑屏幕</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="createOrEnterRoom('enter')">加 入</el-button>
          <el-button type="primary" @click="createOrEnterRoom('create')">创 建</el-button>
        </el-form-item>
      </el-form>
    </div>
  </ElDialog>
</template>

<style lang="scss" src="./styles.scss" scoped />
