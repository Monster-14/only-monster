<template>
  <el-card shadow="never">
    <template #header>概览</template>
    <p>当前登录账号：<strong>{{ profile?.username || auth.username }}</strong></p>
    <p>未读留言：<strong>{{ unread }}</strong></p>
    <p class="muted">资料、项目、文章和留言都可以在左侧菜单维护。</p>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchAdminMe } from '@/api/auth'
import { fetchUnreadCount } from '@/api/message'
import { useAuthStore } from '@/stores/auth'
import type { AdminProfile } from '@/api/types'

const auth = useAuthStore()
const profile = ref<AdminProfile | null>(null)
const unread = ref(0)

onMounted(async () => {
  const [me, count] = await Promise.all([fetchAdminMe(), fetchUnreadCount()])
  profile.value = me.data.data
  unread.value = count.data.data.count
})
</script>

<style scoped>
.muted {
  color: var(--monster-muted);
}
</style>
