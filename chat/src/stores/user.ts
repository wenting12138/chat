import { ref } from 'vue'
import apis from '@/services/apis'
import { defineStore } from 'pinia'
import type { UserInfoType } from '@/services/types'
import wsIns from "@/utils/websocket";
import {WsRequestMsgType} from "@/utils/wsType";

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<Partial<UserInfoType>>({})
  const isSign = ref(false)
  let localUserInfo = {}
  try {
    let item = localStorage.getItem('USER_INFO');
    if (item) {
      isSign.value = true
      // 发送注册消息
      wsIns.send({
        type: WsRequestMsgType.REGISTER,
        header: {
          "clientVersion": "1.0.1",
          "clientIdentify": "webchat",
          "uid": JSON.parse(item || '{}').uid
        }
      });
    }
    localUserInfo = JSON.parse(item || '{}');
  } catch (error) {
    localUserInfo = {}
  }

  // 从 local读取
  if (!Object.keys(userInfo.value).length && Object.keys(localUserInfo).length) {
    userInfo.value = localUserInfo
  }

  function getUserDetailAction() {
    apis
      .getUserDetail()
      .send()
      .then((data) => {
        userInfo.value = { ...userInfo.value, ...data }
        localStorage.setItem('USER_INFO', JSON.stringify(userInfo.value));
      })
      .catch(() => {
        // 删除缓存
        localStorage.removeItem('TOKEN')
        localStorage.removeItem('USER_INFO')
      })
  }

  return { userInfo, isSign, getUserDetailAction }
})
