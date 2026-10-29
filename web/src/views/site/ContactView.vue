<template>
  <section class="page">
    <p class="kicker">联系</p>
    <h1>打个招呼</h1>
    <p class="lead">有合作或想认识，可以直接留言。</p>
    <div v-if="profile" class="social">
      <a v-if="profile.email" :href="`mailto:${profile.email}`">{{ profile.email }}</a>
      <a v-if="profile.githubUrl" :href="profile.githubUrl" target="_blank" rel="noreferrer">GitHub</a>
      <span v-if="profile.wechat">微信：{{ profile.wechat }}</span>
    </div>
    <form class="form" @submit.prevent="onSubmit">
      <label>
        称呼
        <input v-model="form.nickname" maxlength="32" required />
      </label>
      <label>
        邮箱（选填）
        <input v-model="form.email" maxlength="128" type="email" />
      </label>
      <label>
        留言
        <textarea v-model="form.content" rows="5" maxlength="500" required />
      </label>
      <button class="btn" type="submit" :disabled="submitting">{{ submitting ? '发送中…' : '发送' }}</button>
    </form>
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
  try {
    const { data } = await fetchPublicProfile()
    profile.value = data.data
  } catch {
    profile.value = null
  }
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
  padding: 48px 0 96px;
}

.kicker {
  color: #635bff;
  font-size: 14px;
}

h1 {
  margin: 8px 0 12px;
  font-size: 42px;
  letter-spacing: -0.04em;
}

.lead,
.social {
  color: #6b6560;
}

.social {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin: 16px 0 28px;
}

.social a {
  color: #635bff;
  text-decoration: none;
}

.form {
  display: grid;
  gap: 16px;
}

label {
  display: grid;
  gap: 8px;
  font-size: 14px;
}

input,
textarea {
  width: 100%;
  border: 1px solid #e4ddd2;
  background: #fffcf7;
  color: inherit;
  font: inherit;
  border-radius: 12px;
  padding: 10px 12px;
}

.btn {
  width: fit-content;
  min-height: 44px;
  padding: 0 20px;
  border: 0;
  border-radius: 999px;
  background: #635bff;
  color: #fff;
  font: inherit;
  cursor: pointer;
}

.btn:disabled {
  opacity: 0.6;
}
</style>
