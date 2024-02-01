// 本地配置到 .env 里面修改。生产配置在 .env.production 里面
const prefix = import.meta.env.PROD ? import.meta.env.VITE_API_PREFIX : ''
export default {
  getMemberStatistic: `${prefix}/imapi/chat/public/member/statistic`,
  getUserInfoBatch: `${prefix}/imapi/user/public/summary/userInfo/batch`,
  getBadgesBatch: `${prefix}/imapi/user/public/badges/batch`,
  getAllUserBaseInfo: `${prefix}/imapi/room/group/member/list`, // 房间内的所有群成员列表-@专用
  getMsgList: `${prefix}/imapi/chat/public/msg/page`,
  sendMsg: `${prefix}/imapi/chat/msg`,
  getUserInfoDetail: `${prefix}/imapi/user/userInfo`, // 获取用户信息详情
  modifyUserName: `${prefix}/imapi/user/name`, // 修改用户名
  modifyUserAvatar: `${prefix}/imapi/user/avatar`, // 修改头像
  login: `${prefix}/imapi/user/login`, // 登录
  getBadgeList: `${prefix}/imapi/user/badges`, // 徽章列表
  setUserBadge: `${prefix}/imapi/user/badge`, // 设置用户徽章
  markMsg: `${prefix}/imapi/chat/msg/mark`, // 消息标记
  blockUser: `${prefix}/imapi/user/black`, // 拉黑用户
  recallMsg: `${prefix}/imapi/chat/msg/recall`, // 撤回消息
  fileUpload: `${prefix}/imapi/oss/upload/url`, // 文件上传
  addEmoji: `${prefix}/imapi/user/emoji`, // 增加表情
  deleteEmoji: `${prefix}/imapi/user/emoji`, // 删除表情
  getEmoji: `${prefix}/imapi/user/emoji/list`, // 查询表情包

  // -------------- 好友相关 ---------------
  getContactList: `${prefix}/imapi/user/friend/page`, // 联系人列表
  requestFriendList: `${prefix}/imapi/user/friend/apply/page`, // 好友申请列表
  sendAddFriendRequest: `${prefix}/imapi/user/friend/apply`, // 申请好友
  sendAddFriendUsernameRequest: `${prefix}/imapi/user/friend/applyUserName`, // 申请好友userName
  deleteFriend: `${prefix}/imapi/user/friend`, // 删除好友
  newFriendCount: `${prefix}/imapi/user/friend/apply/unread`, // 申请未读数

  // -------------- 聊天室相关 ---------------
  getSessionList: `${prefix}/imapi/chat/public/contact/page`, // 会话列表
  getMsgReadList: `${prefix}/imapi/chat/msg/read/page`, // 消息的已读未读列表
  getMsgReadCount: `${prefix}/imapi/chat/msg/read`, // 消息已读未读数
  createGroup: `${prefix}/imapi/room/group`, // 新增群组
  createMeet: `${prefix}/imapi/room/group/createMeet`, // 新增会议室
  getGroupUserList: `${prefix}/imapi/room/public/group/member/page`,
  inviteGroupMember: `${prefix}/imapi/room/group/member`, // 邀请群成员
  exitGroup: `${prefix}/imapi/room/group/member/exit`, // 退群
  addAdmin: `${prefix}/imapi/room/group/admin`, // 添加管理员
  revokeAdmin: `${prefix}/imapi/room/group/admin`, // 添加管理员
  groupDetail: `${prefix}/imapi/room/public/group`, // 群组详情
  sessionDetail: `${prefix}/imapi/chat/public/contact/detail`, // 会话详情
  sessionDetailWithFriends: `${prefix}/imapi/chat/public/contact/detail/friend`, // 会话详情(联系人列表发消息用)
}
