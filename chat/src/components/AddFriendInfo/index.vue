<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRequest } from 'alova'
import apis from '@/services/apis'
import { ElMessage } from 'element-plus'
import { useGlobalStore } from '@/stores/global'
import { judgeClient } from '@/utils/detectDevice'

const client = judgeClient()

const globalStore = useGlobalStore()
const requestMsg = ref()
const reqUserName = ref()

const { send, loading } = useRequest(apis.sendAddFriendUsernameRequest, { immediate: false })
const show = computed(() => globalStore.addFriendInfo.show)
const close = () => {
  globalStore.addFriendInfo.show = false
}
const onSend = async () => {
  await send({ msg: requestMsg.value, targetUserName: reqUserName.value })
  ElMessage.success('TA一定会被你的诚意打动的~')
  close()
}
</script>

<template>
  <ElDialog
    class="setting-box-modal"
    :model-value="show"
    :width="client === 'PC' ? 350 : '50%'"
    :close-on-click-modal="false"
    center
    :show-close="false"
    @close="close"
  >
    <ElInput
        :autosize="{ minRows: 4, maxRows: 4 }"
        :maxlength="50"
        type="text"
        resize="none"
        v-model="reqUserName"
        placeholder="输入对方账号"
    />
    <ElInput
      :autosize="{ minRows: 4, maxRows: 4 }"
      :maxlength="50"
      style="margin-top: 10px"
      type="textarea"
      resize="none"
      v-model="requestMsg"
      placeholder="发条有趣的问候语吧~"
    />
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="onSend" :loading="loading">发送</el-button>
      </span>
    </template>
  </ElDialog>
</template>

<style lang="scss" src="./styles.scss" scoped />
