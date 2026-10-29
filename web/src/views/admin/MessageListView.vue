<template>
  <el-card shadow="never">
    <template #header>留言</template>
    <el-table :data="messages" empty-text="还没有留言">
      <el-table-column prop="nickname" label="称呼" width="120" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="content" label="内容" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">{{ row.status === 1 ? '已读' : '未读' }}</template>
      </el-table-column>
      <el-table-column prop="createdAt" label="时间" width="170">
        <template #default="{ row }">{{ row.createdAt?.replace('T', ' ').slice(0, 19) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button v-if="row.status !== 1" link type="primary" @click="onRead(row)">已读</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteAdminMessage, fetchAdminMessages, markMessageRead } from '@/api/message'
import type { MessageItem } from '@/api/types'

const messages = ref<MessageItem[]>([])

async function load() {
  const { data } = await fetchAdminMessages()
  messages.value = data.data
}

onMounted(load)

async function onRead(row: MessageItem) {
  await markMessageRead(row.id)
  ElMessage.success('已标为已读')
  await load()
}

async function onDelete(row: MessageItem) {
  await ElMessageBox.confirm(`确定删除「${row.nickname}」的留言？`, '删除留言', { type: 'warning' })
  await deleteAdminMessage(row.id)
  ElMessage.success('已删除')
  await load()
}
</script>
