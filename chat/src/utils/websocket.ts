import Router from '@/router'
import { useWsLoginStore, LoginStatus } from '@/stores/ws'
import { useUserStore } from '@/stores/user'
import { useChatStore } from '@/stores/chat'
import { useCallStore } from '@/stores/call'
import { useGroupStore } from '@/stores/group'
import { useContactStore } from '@/stores/contacts'
import { useGlobalStore } from '@/stores/global'
import { useEmojiStore } from '@/stores/emoji'
import {WsRequestMsgType, WsResponseMessageType} from './wsType'
import type {
  WsReqMsgContentType,
} from './wsType'
import type { MessageType, MarkItemType, RevokedMsgType } from '@/services/types'
import { OnlineEnum, ChangeTypeEnum, RoomTypeEnum } from '@/enums'
import { computedToken } from '@/services/request'
import { worker } from './initWorker'
import shakeTitle from '@/utils/shakeTitle'
import notify from '@/utils/notification'
import {data} from "autoprefixer";

class WS {
  #tasks: WsReqMsgContentType[] = []
  // 重连🔐
  #connectReady = false

  constructor() {
    console.log("init ws")
    this.initConnect()
    // 收到消息
    worker.addEventListener('message', this.onWorkerMsg)

    // 后台重试次数达到上限之后，tab 获取焦点再重试
    document.addEventListener('visibilitychange', () => {
      if (!document.hidden && !this.#connectReady) {
        this.initConnect()
      }

      // 获得焦点停止消息闪烁
      if (!document.hidden) {
        shakeTitle.clear()
      }
    })
  }

  initConnect = () => {
    const token = localStorage.getItem('TOKEN')
    // 如果token 是 null, 而且 localStorage 的用户信息有值，需要清空用户信息
    if (token === null && localStorage.getItem('USER_INFO')) {
      localStorage.removeItem('USER_INFO')
    }
    // 初始化 ws
    worker.postMessage(`{"type":"initWS","value":${token ? `"${token}"` : null}}`)
  }

  onWorkerMsg = (e: MessageEvent<any>) => {
    const params: { type: string; value: unknown } = JSON.parse(e.data)
    switch (params.type) {
      case 'message': {
        this.onMessage(params.value as string)
        break
      }
      case 'open': {
        this.#dealTasks()
        break
      }
      case 'close':
      case 'error': {
        this.#onClose()
        break
      }
    }
  }

  // 重置一些属性
  #onClose = () => {
    this.#connectReady = false
  }

  #dealTasks = () => {
    this.#connectReady = true
    // 先探测登录态
    // this.#detectionLoginStatus()

    setTimeout(() => {
      const userStore = useUserStore()
      const loginStore = useWsLoginStore()
      if (userStore.isSign) {
        // 发送注册消息
        let item = localStorage.getItem('USER_INFO');
        this.send(
            {
              type: WsRequestMsgType.REGISTER,
              header: {
                "clientVersion": "1.0.1",
                "clientIdentify": "webchat",
                "uid": JSON.parse(item || '{}').uid
              }
            }
        )
        // 处理堆积的任务
        this.#tasks.forEach((task) => {
          this.send(task)
        })
        // 清空缓存的消息
        this.#tasks = []
      } else {
        // 如果没登录，而且已经请求了登录二维码，就要更新登录二维码。
        loginStore.loginQrCode && loginStore.getLoginQrCode()
      }
    }, 500)
  }

  #send(msg: WsReqMsgContentType) {
    worker.postMessage(
      `{"type":"message","value":${typeof msg === 'string' ? msg : JSON.stringify(msg)}}`,
    )
  }

  send = (params: WsReqMsgContentType) => {
    params.code = params.type.toString()
    if (this.#connectReady) {
      this.#send(params)
    } else {
      // 放到队列
      this.#tasks.push(params)
    }
  }

  // 收到消息回调
  onMessage = (value: string) => {
    // FIXME 可能需要 try catch,
    let tmp = JSON.parse(value)
    tmp.type = JSON.parse(value).code
    tmp.data = JSON.parse(value).body
    // value.
    const params: { type: WsResponseMessageType; data: unknown } = JSON.parse(JSON.stringify(tmp))
    const loginStore = useWsLoginStore()
    const userStore = useUserStore()
    const chatStore = useChatStore()
    const callStore = useCallStore()
    const groupStore = useGroupStore()
    const globalStore = useGlobalStore()
    const emojiStore = useEmojiStore()
    const contactStore = useContactStore()
    switch (params.type) {
      // 收到消息
      case WsResponseMessageType.ReceiveMessage: {
        chatStore.pushMsg(params.data as MessageType)
        break
      }
      // 新好友申请
      case WsResponseMessageType.RequestNewFriend: {
        const data = params.data as { uid: number; unreadCount: number }
        globalStore.unReadMark.newFriendUnreadCount += data.unreadCount
        notify({
          name: '新好友',
          text: '您有一个新好友, 快来看看~',
          onClick: () => {
            Router.push('/contact')
          },
        })
        break
      }
      // 新好友申请
      case WsResponseMessageType.NewFriendSession: {
        // changeType 1 加入群组，2： 移除群组
        const data = params.data as {
          roomId: number
          uid: number
          changeType: ChangeTypeEnum
          activeStatus: OnlineEnum
          lastOptTime: number
        }
        if (
          data.roomId === globalStore.currentSession.roomId &&
          globalStore.currentSession.type === RoomTypeEnum.Group
        ) {
          if (data.changeType === ChangeTypeEnum.REMOVE) {
            // 移除群成员
            groupStore.filterUser(data.uid)
            chatStore.sessionList(true)
            groupStore.getGroupUserList(true)
            // TODO 添加一条退出群聊的消息
          } else {
            groupStore.getGroupUserList(true);
            chatStore.sessionList(true)
            groupStore.getGroupUserList(true)
            // TODO 添加群成员
            // TODO 添加一条入群的消息
          }
        }
        break
      }
      case WsResponseMessageType.PONG:
        console.log("pong")
        break
      case WsResponseMessageType.REGISTER_SUCCESS:
        console.log("register success")
        break

      case WsResponseMessageType.CALL_REMOTE_RES:
          console.log("call remote")
          callStore.calledReceiveRemoteRequest(params.data)
          break
      case WsResponseMessageType.CALL_ACCEPT_RES:
          callStore.wc.callerReceiveAcceptCall()
          break
      case WsResponseMessageType.CALL_SEND_OFFER_RES:
          callStore.wc.calledReceiveOffer(params.data.offer)
          break
      case WsResponseMessageType.CALL_SEND_ANSWER_RES:
          callStore.wc.callerReceiveAnswer(params.data.answer)
          break
      case WsResponseMessageType.CALL_SEND_CANDIDATE_RES:
          callStore.wc.calledReceiveCandidate(params.data.candidate)
          break
      case WsResponseMessageType.CALL_SEND_CANDIDATE_2_RES:
          callStore.wc.callerReceiveCandidate(params.data.candidate)
          break
      case WsResponseMessageType.CALL_SEND_HANGUP_RES:
          callStore.wc.receiveHangup(params.data.toUid)
          break
      case WsResponseMessageType.CALL_SEND_REJECT_RES:
          callStore.wc.receiveReject(params.data.toUid)
          break
      case WsResponseMessageType.CALL_SEND_CANCEL_RES:
          callStore.wc.receiveCancel(params.data.toUid)
          break
      case WsResponseMessageType.CALL_SEND_BUSY_RES:
          callStore.wc.receiveBusy(params.data.toUid)
          break
      default: {
        console.log('接收到未处理类型的消息:', params)
        break
      }
    }
  }
}

export default new WS()
