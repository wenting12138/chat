import type { UserInfoType, UserItem } from '@/services/types'

// 1.登录返回二维码 2.用户扫描成功等待授权 3.用户登录成功返回用户信息 4.收到消息 5.上下线推送 6.前端token失效
export enum WsResponseMessageType {
  /** 1.登录返回二维码 */
  LoginQrCode = 1,
  /** 2.用户扫描成功等待授权 */
  WaitingAuthorize,
  /** 3.用户登录成功返回用户信息 */
  LoginSuccess,
  /** 4.收到消息 */
  ReceiveMessage,
  /** 5.上下线推送 */
  OnOffLine,
  /** 6.前端token失效 */
  TokenExpired,
  /** 7.禁用的用户 */
  InValidUser,
  /** 8.点赞、倒赞更新通知 */
  WSMsgMarkItem,
  /** 消息撤回 */
  WSMsgRecall,
  /** 新好友申请 */
  RequestNewFriend,
  /** 新好友会话 */
  NewFriendSession,
  PONG = 39999,
  REGISTER_SUCCESS = 40000,
  // 接收到电话请求
  CALL_REMOTE_RES = 40002,
  // 接收到电话请求
  CALL_ACCEPT_RES = 40003,
  // 发送offer
  CALL_SEND_OFFER_RES = 40004,
  // 发送 answer
  CALL_SEND_ANSWER_RES = 40005,
  // 发送 candidate A -> B
  CALL_SEND_CANDIDATE_RES = 40006,
  // // 发送 candidate B -> A
  CALL_SEND_CANDIDATE_2_RES = 40007,
  CALL_SEND_HANGUP_RES = 40008,
  CALL_SEND_REJECT_RES = 40009,
  CALL_SEND_CANCEL_RES = 40010,
}

/**
 * ws 请求 消息类型 1.请求登录二维码，2心跳检测 3用户认证
 */
export enum WsRequestMsgType {
  /** 1.请求登录二维码 */
  RequestLoginQrCode = 1,
  /** 2心跳检测 */
  HeartBeatDetection,
  /** 3用户认证 */
  Authorization,
  /** 4.登录 */
  Login = 10002,
  /** 5.登录注册 */
  REGISTER = 10001,



  /** 6.向信令服务器发送发起请求的事件 */
  CALL_REMOTE = 50000,
  /** 6.向信令服务器发送发起同意的事件 */
  ACCEPT_CALL = 50001,
  /** 发送offer信令 */
  CALLER_SEND_OFFER = 50002,
  /** 发送answer信令 */
  CALLER_SEND_ANSWER = 50003,
  /** 发送 candidate 信令 A -> B */
  CALLER_SEND_CANDIDATE = 50004,
  // /** 发送 candidate 信令  B -> A */
  CALLER_SEND_CANDIDATE_2 = 50005,
  // 挂断
  SEND_HANG_UP = 50006,
  // 拒接
  SEND_REJECT = 50007,
  // 取消
  SEND_CANCEL = 50008,
}

export type WsReqMsgContentType = {
  type: WsRequestMsgType
  code: string
  data?: Record<string, unknown>
  body?: Record<string, unknown>
  header?: Record<string, unknown>
}
export type LoginInitResType = { loginUrl: string }

export type LoginSuccessResType = Pick<UserInfoType, 'avatar' | 'name' | 'uid'> & {
  /** 用户的登录凭证，每次请求携带 */
  token: string
}

export type OnStatusChangeType = {
  changeList: UserItem[]
  onlineNum: number
  totalNum: number
}
