<template>
  <el-container class="admin">
    <el-aside width="220px" class="aside">
      <div class="logo">Monster Admin</div>
      <el-menu router :default-active="route.path" background-color="#0f172a" text-color="#cbd5e1" active-text-color="#93c5fd">
        <el-menu-item index="/admin">概览</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>{{ auth.username }}</span>
        <el-button link type="primary" @click="logout">退出</el-button>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

function logout() {
  auth.clearSession()
  router.push('/admin/login')
}
</script>

<style scoped>
.admin {
  min-height: 100vh;
}

.aside {
  background: #0f172a;
  color: #fff;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  font-weight: 700;
}

.header {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  background: #fff;
  border-bottom: 1px solid #e2e8f0;
}
</style>
