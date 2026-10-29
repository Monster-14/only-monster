<template>
  <el-card shadow="never">
    <template #header>个人资料</template>
    <el-form :model="form" label-width="96px" style="max-width: 640px" @submit.prevent="onSave">
      <el-form-item label="姓名">
        <el-input v-model="form.name" maxlength="64" />
      </el-form-item>
      <el-form-item label="一句话">
        <el-input v-model="form.headline" maxlength="128" placeholder="例如：全栈开发者" />
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.bio" type="textarea" :rows="4" maxlength="4000" />
      </el-form-item>
      <el-form-item label="头像链接">
        <el-input v-model="form.avatarUrl" placeholder="https://..." />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="GitHub">
        <el-input v-model="form.githubUrl" placeholder="https://github.com/..." />
      </el-form-item>
      <el-form-item label="博客">
        <el-input v-model="form.blogUrl" />
      </el-form-item>
      <el-form-item label="微信">
        <el-input v-model="form.wechat" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchAdminProfile, updateAdminProfile } from '@/api/profile'
import type { SiteProfile } from '@/api/types'

const saving = ref(false)
const form = reactive<SiteProfile>({
  name: '',
  headline: '',
  bio: '',
  avatarUrl: '',
  email: '',
  githubUrl: '',
  blogUrl: '',
  wechat: '',
})

function fill(data: SiteProfile) {
  Object.assign(form, data)
}

onMounted(async () => {
  const { data } = await fetchAdminProfile()
  fill(data.data)
})

async function onSave() {
  if (!form.name.trim()) {
    ElMessage.warning('请填写姓名')
    return
  }
  saving.value = true
  try {
    const { data } = await updateAdminProfile({ ...form })
    fill(data.data)
    ElMessage.success('已保存，前台首页会同步更新')
  } finally {
    saving.value = false
  }
}
</script>
