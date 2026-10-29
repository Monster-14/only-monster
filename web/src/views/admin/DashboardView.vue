<template>
  <el-card shadow="never">
    <template #header>后台已打通</template>
    <p>当前登录账号：<strong>{{ profile?.username || auth.username }}</strong></p>
    <p class="muted">这一步只做登录和空白管理页。下一步会接个人资料、项目和文章的后台编辑。</p>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchAdminMe } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'
import type { AdminProfile } from '@/api/types'

const auth = useAuthStore()
const profile = ref<AdminProfile | null>(null)

onMounted(async () => {
  const { data } = await fetchAdminMe()
  profile.value = data.data
})
</script>

<style scoped>
.muted {
  color: var(--monster-muted);
}
</style>
