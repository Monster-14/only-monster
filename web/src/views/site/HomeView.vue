<template>
  <section class="hero">
    <p class="eyebrow">MONSTER PORTFOLIO</p>
    <h1>个人主页脚手架已就绪</h1>
    <p class="lead">
      当前是第一步：Vue3 前台、Spring Boot 后台、统一响应和本地代理已经打通。
    </p>
    <el-card id="health" class="status-card" shadow="never">
      <template #header>
        <div class="card-head">
          <span>后端健康检查</span>
          <el-button type="primary" :loading="loading" @click="loadHealth">重新检测</el-button>
        </div>
      </template>
      <el-alert
        v-if="ok"
        type="success"
        :closable="false"
        :title="`接口正常 · ${health?.app} · ${health?.status}`"
      />
      <el-alert
        v-else-if="error"
        type="error"
        :closable="false"
        :title="error"
      />
      <el-empty v-else description="点击按钮请求 /api/public/health" />
      <pre v-if="health">{{ JSON.stringify(health, null, 2) }}</pre>
    </el-card>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchHealth } from '@/api/health'
import type { HealthData } from '@/api/types'

const loading = ref(false)
const ok = ref(false)
const error = ref('')
const health = ref<HealthData | null>(null)

async function loadHealth() {
  loading.value = true
  error.value = ''
  try {
    const { data } = await fetchHealth()
    health.value = data.data
    ok.value = data.code === 0
  } catch (err) {
    ok.value = false
    health.value = null
    error.value = err instanceof Error ? err.message : '后端未启动或代理失败'
  } finally {
    loading.value = false
  }
}

onMounted(loadHealth)
</script>

<style scoped>
.hero {
  max-width: 880px;
  margin: 0 auto;
  padding: 72px 24px 96px;
}

.eyebrow {
  letter-spacing: 0.18em;
  color: var(--monster-primary);
  font-size: 13px;
  margin-bottom: 12px;
}

h1 {
  font-size: 40px;
  margin: 0 0 16px;
}

.lead {
  color: var(--monster-muted);
  font-size: 16px;
  line-height: 1.7;
  margin-bottom: 32px;
}

.status-card {
  border-radius: 12px;
}

.card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

pre {
  margin: 16px 0 0;
  padding: 12px;
  background: #0f172a;
  color: #e2e8f0;
  border-radius: 8px;
  overflow: auto;
}
</style>
