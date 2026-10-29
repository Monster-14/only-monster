<template>
  <el-card shadow="never">
    <template #header>{{ isNew ? '写文章' : '编辑文章' }}</template>
    <el-form :model="form" label-width="96px" style="max-width: 800px" @submit.prevent="onSave">
      <el-form-item label="标题">
        <el-input v-model="form.title" maxlength="128" @blur="fillSlug" />
      </el-form-item>
      <el-form-item label="路径别名">
        <el-input v-model="form.slug" maxlength="80" placeholder="例如 hello-monster" />
      </el-form-item>
      <el-form-item label="摘要">
        <el-input v-model="form.summary" maxlength="255" />
      </el-form-item>
      <el-form-item label="封面">
        <ImageUpload v-model="form.coverUrl" />
      </el-form-item>
      <el-form-item label="Markdown">
        <el-input v-model="form.contentMd" type="textarea" :rows="14" placeholder="支持标题、列表和代码块" />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :value="0">草稿</el-radio>
          <el-radio :value="1">发布</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
        <el-button @click="router.push('/admin/articles')">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createAdminArticle, fetchAdminArticle, updateAdminArticle } from '@/api/article'
import ImageUpload from '@/components/ImageUpload.vue'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const isNew = computed(() => route.params.id === 'new')

const form = reactive({
  slug: '',
  title: '',
  coverUrl: '',
  summary: '',
  contentMd: '',
  status: 1,
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
  const { data } = await fetchAdminArticle(Number(route.params.id))
  Object.assign(form, {
    slug: data.data.slug,
    title: data.data.title,
    coverUrl: data.data.coverUrl,
    summary: data.data.summary,
    contentMd: data.data.contentMd,
    status: data.data.status,
  })
})

async function onSave() {
  fillSlug()
  if (!form.title.trim() || !form.slug.trim()) {
    ElMessage.warning('请填写标题和路径别名')
    return
  }
  saving.value = true
  try {
    const payload = { ...form }
    if (isNew.value) {
      await createAdminArticle(payload)
    } else {
      await updateAdminArticle(Number(route.params.id), payload)
    }
    ElMessage.success('已保存')
    await router.push('/admin/articles')
  } finally {
    saving.value = false
  }
}
</script>
