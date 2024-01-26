<script setup lang="ts">
import {computed, ref} from 'vue'
import {LoginStatus, useWsLoginStore} from '@/stores/ws'
import apis from '@/services/apis'
import {ElMessage} from 'element-plus'
import {computedToken} from "@/services/request";
import {OnlineEnum} from "@/enums";
import {useGroupStore} from "@/stores/group";
import {useUserStore} from "@/stores/user";
import {useChatStore} from "@/stores/chat";
import {useGlobalStore} from "@/stores/global";
import {useEmojiStore} from "@/stores/emoji";
import wsIns from '@/utils/websocket'
import {WsRequestMsgType} from "@/utils/wsType";

const loginStore = useWsLoginStore()
const userStore = useUserStore()
const chatStore = useChatStore()
const groupStore = useGroupStore()
const globalStore = useGlobalStore()
const emojiStore = useEmojiStore()
// v-model
const emit = defineEmits([
  'update:modelValue',
  'change',
  'focus',
  'blur',
  'callBackRefAndRange',
  'send',
])


const visible = computed({
  get() {
    return loginStore.showLogin
  },
  set(value) {
    loginStore.showLogin = value
  },
})

const loginUserName = ref()
const loginPassword = ref()
const loginQrCode = computed(() => loginStore.loginQrCode)
const loginStatus = computed(() => loginStore.loginStatus)

const login = async function (e) {
  if (!loginUserName.value) {
    ElMessage.warning("账号不能为空")
    return;
  }
  apis.login({userName: loginUserName.value, password: loginPassword.value}).send().then(data=> {
    console.log(data)
    localStorage.setItem('USER_INFO', JSON.stringify(data))
    localStorage.setItem('TOKEN', "")
    // 更新一下请求里面的 token.
    computedToken.clear()
    computedToken.get()
    loginStore.loginStatus = LoginStatus.Success
    userStore.isSign = true
    // 关闭登录弹窗
    loginStore.showLogin = false
    // 获取用户详情
    chatStore.getSessionList(true)
    // 自定义表情列表
    emojiStore.getEmojiList()
    // 自己更新自己上线
    groupStore.batchUpdateUserStatus([
      {
        activeStatus: OnlineEnum.ONLINE,
        avatar: data.avatar,
        lastOptTime: Date.now(),
        name: data.name,
        uid: data.uid,
      },
    ])

    // 发送注册消息
    wsIns.send({
      type: WsRequestMsgType.REGISTER,
      header: {
        "clientVersion": "1.0.1",
        "clientIdentify": "webchat",
        "uid": data.uid
      }
    });
  }).catch(err => {
    console.log(err)
    ElMessage.warning("登录失败")
  })
}

</script>

<template>
  <ElDialog class="login-box-modal" :width="376" v-model="visible" center>
    <div class="login-box">
      <h>登录</h>
      <ElInput
          :autosize="{ minRows: 4, maxRows: 4 }"
          :maxlength="50"
          resize="none"
          v-model="loginUserName"
          placeholder="请输入账号"
      />
      <ElInput
          :autosize="{ minRows: 4, maxRows: 4 }"
          :maxlength="50"
          resize="none"
          type="password"
          v-model="loginPassword"
          placeholder="请输入密码"
      />
      <button style="margin-top: 10px" @click="login">登录</button>
    </div>
  </ElDialog>
</template>

<style lang="scss" src="./styles.scss" scoped />
