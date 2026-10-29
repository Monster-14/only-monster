<template>
  <section class="page">
    <h1>联系我</h1>
    <p class="lead">有合作或想打招呼，可以直接留言。</p>
    <div v-if="profile" class="social">
      <a v-if="profile.email" :href="`mailto:${profile.email}`">{{ profile.email }}</a>
      <a v-if="profile.githubUrl" :href="profile.githubUrl" target="_blank" rel="noreferrer">GitHub</a>
      <span v-if="profile.wechat">微信：{{ profile.wechat }}</span>
    </div>
    <el-form class="form" :model="form" label-position="top" @submit.prevent="onSubmit">
      <el-form-item label="称呼">
        <el-input v-model="form.nickname" maxlength="32" />
      </el-form-item>
      <el-form-item label="邮箱（选填）">
        <el-input v-model="form.email" maxlength="128" />
      </el-form-item>
      <el-form-item label="留言">
        <el-input v-model="form.content" type="textarea" :rows="5" maxlength="500" show-word-limit />
      </el-form-item>
      <el-button type="primary" :loading="submitting" @click="onSubmit">发送</el-button>
    </el-form>
  </section>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchPublicProfile } from '@/api/profile'
import { submitMessage } from '@/api/message'
import type { SiteProfile } from '@/api/types'

const submitting = ref(false)
const profile = ref<SiteProfile | null>(null)
const form = reactive({
  nickname: '',
  email: '',
  content: '',
})

onMounted(async () => {
  const { data } = await fetchPublicProfile()
  profile.value = data.data
})

async function onSubmit() {
  if (!form.nickname.trim() || form.content.trim().length < 4) {
    ElMessage.warning('请填写称呼，留言至少 4 个字')
    return
  }
  submitting.value = true
  try {
    await submitMessage({ ...form })
    ElMessage.success('已送出，我会尽快看到')
    form.content = ''
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.page {
  max-width: 640px;
  margin: 0 auto;
  padding: 48px 24px 80px;
}

.lead,
.social {
  color: var(--monster-muted);
}

.social {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 28px;
}

.social a {
  color: var(--monster-primary);
  text-decoration: none;
}

.form {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}
</style>
