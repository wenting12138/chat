<script setup lang="ts" name="ContactSide">
import { ref } from 'vue'
import { useContactStore } from '@/stores/contacts'
import { useGlobalStore } from '@/stores/global'
import { useMeetStore } from '@/stores/meet'
import type { ContactItem as ContactItemType, RequestFriendItem } from '@/services/types'

import ContactItem from './ContactItem.vue'
import NewFriendItem from './NewFriendItem.vue'

const contactStore = useContactStore()
const globalStore = useGlobalStore()
const meetStore = useMeetStore()

const active = ref('1')
const onFrozen = ref(true)

const onNodeClick = (item: RequestFriendItem | ContactItemType) => {
  globalStore.currentSelectedContact = item
}

const onNewFriendLoad = () => {
  contactStore.getRequestFriendsList(false)
}
const onContactsLoad = () => {
  contactStore.getContactList(false)
}
const onChange = () => {
  onFrozen.value = true
  setTimeout(() => {
    onFrozen.value = false
  }, 500)
}
const onCreateGroup = () => {
  globalStore.createGroupModalInfo.show = true
}
const onCreateMeet = () => {
  meetStore.meetInfo.show = true
}
const onAddFriend = () => {
  globalStore.addFriendInfo.show = true
}
</script>

<template>
  <div class="side">
    <div style="display: flex">
      <ElButton class="add-group" type="primary" size="small" @click="onCreateGroup"
      >创建群聊</ElButton>
      <ElButton class="add-group" style=""  type="primary" size="small" @click="onCreateMeet"
      >创建会议</ElButton>
      <ElButton class="add-group" style="" type="primary" size="small" @click="onAddFriend"
      >搜索朋友</ElButton>
    </div>
    <el-collapse class="side-collapse" v-model="active" @change="onChange" accordion>
      <el-collapse-item title="新的朋友" name="1">
        <ul
          v-infinite-scroll="onNewFriendLoad"
          :infinite-scroll-disabled="onFrozen"
          class="read-unread-list"
        >
          <NewFriendItem
            v-for="item in contactStore.requestFriendsList"
            :key="item.uid"
            :item="item"
            @click="onNodeClick(item)"
          />
          <li v-if="contactStore.requestFriendsList?.length === 0" class="read-unread-no-data">
            暂无数据~
          </li>
        </ul>
      </el-collapse-item>
      <el-collapse-item title="联系人" name="2">
        <ul
          v-infinite-scroll="onContactsLoad"
          :infinite-scroll-disabled="onFrozen"
          class="read-unread-list"
        >
          <ContactItem
            v-for="item in contactStore.contactsList"
            :key="item.uid"
            :item="item"
            @click="onNodeClick(item)"
          />
          <li v-if="contactStore.contactsList?.length === 0" class="read-unread-no-data">
            暂无数据~
          </li>
        </ul>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<style lang="scss" src="./styles.scss" scoped />

<style lang="scss">
.contact-tree-item .el-tree-node__content {
  width: 100%;
  height: 60px;
}
</style>
