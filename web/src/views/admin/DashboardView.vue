<template>
  <el-card shadow="never">
    <template #header>后台已打通</template>
    <p>当前登录账号：<strong>{{ profile?.username || auth.username }}</strong></p>
    <p class="muted">资料、项目和文章都可以在左侧菜单维护。</p>
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
