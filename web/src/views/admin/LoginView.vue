<template>
  <div class="login-page">
    <el-card class="login-card" shadow="never">
      <h1>Monster 后台</h1>
      <p class="hint">第一步之后：管理员登录</p>
      <el-form :model="form" @submit.prevent="onSubmit">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password @keyup.enter="onSubmit" />
        </el-form-item>
        <el-button type="primary" size="large" :loading="loading" style="width: 100%" @click="onSubmit">
          登录
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const form = reactive({
  username: 'admin',
  password: 'admin123',
})

async function onSubmit() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const { data } = await login(form.username, form.password)
    auth.setSession(data.data.token, data.data.username)
    ElMessage.success('登录成功')
    await router.replace('/admin')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: linear-gradient(180deg, #eff4ff 0%, #f5f7fb 100%);
}

.login-card {
  width: 380px;
  border-radius: 16px;
  padding: 12px 8px 8px;
}

h1 {
  margin: 0 0 8px;
  text-align: center;
}

.hint {
  margin: 0 0 24px;
  text-align: center;
  color: var(--monster-muted);
}
</style>
