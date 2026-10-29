<template>
  <el-card shadow="never">
    <template #header>{{ isNew ? '新建项目' : '编辑项目' }}</template>
    <el-form :model="form" label-width="96px" style="max-width: 720px" @submit.prevent="onSave">
      <el-form-item label="标题">
        <el-input v-model="form.title" maxlength="128" @blur="fillSlug" />
      </el-form-item>
      <el-form-item label="路径别名">
        <el-input v-model="form.slug" maxlength="80" placeholder="例如 monster-portfolio" />
      </el-form-item>
      <el-form-item label="摘要">
        <el-input v-model="form.summary" maxlength="255" />
      </el-form-item>
      <el-form-item label="正文">
        <el-input v-model="form.content" type="textarea" :rows="6" />
      </el-form-item>
      <el-form-item label="技术栈">
        <el-select v-model="form.techStack" multiple filterable allow-create default-first-option placeholder="输入后回车">
          <el-option v-for="tag in form.techStack" :key="tag" :label="tag" :value="tag" />
        </el-select>
      </el-form-item>
      <el-form-item label="封面链接">
        <el-input v-model="form.coverUrl" />
      </el-form-item>
      <el-form-item label="演示地址">
        <el-input v-model="form.demoUrl" />
      </el-form-item>
      <el-form-item label="仓库地址">
        <el-input v-model="form.repoUrl" />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :value="0">草稿</el-radio>
          <el-radio :value="1">发布</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="form.sort" :min="0" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
        <el-button @click="router.push('/admin/projects')">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createAdminProject, fetchAdminProject, updateAdminProject } from '@/api/project'
import type { ProjectItem } from '@/api/types'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const isNew = computed(() => route.params.id === 'new')

const form = reactive<Omit<ProjectItem, 'id'>>({
  slug: '',
  title: '',
  coverUrl: '',
  summary: '',
  content: '',
  techStack: [],
  demoUrl: '',
  repoUrl: '',
  status: 1,
  sort: 0,
})

function toSlug(title: string) {
  return title
    .trim()
    .toLowerCase()
    .replace(/\s+/g, '-')
    .replace(/[^a-z0-9-]/g, '')
    .replace(/-+/g, '-')
    .replace(/^-|-$/g, '')
}

function fillSlug() {
  if (!form.slug && form.title) {
    form.slug = toSlug(form.title)
  }
}

onMounted(async () => {
  if (isNew.value) {
    return
  }
  const id = Number(route.params.id)
  const { data } = await fetchAdminProject(id)
  Object.assign(form, data.data)
})

async function onSave() {
  fillSlug()
  if (!form.title.trim() || !form.slug.trim()) {
    ElMessage.warning('请填写标题和路径别名')
    return
  }
  saving.value = true
  try {
    const payload = { ...form, techStack: [...form.techStack] }
    if (isNew.value) {
      await createAdminProject(payload)
    } else {
      await updateAdminProject(Number(route.params.id), payload)
    }
    ElMessage.success('已保存')
    await router.push('/admin/projects')
  } finally {
    saving.value = false
  }
}
</script>
