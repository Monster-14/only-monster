<template>
  <div class="uploader">
    <el-upload
      :show-file-list="false"
      :http-request="onUpload"
      :before-upload="beforeUpload"
      accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
    >
      <el-button :loading="loading">上传图片</el-button>
    </el-upload>
    <el-input v-model="url" placeholder="也可直接填写图片链接" />
    <img v-if="url" class="preview" :src="url" alt="预览" />
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { UploadRequestOptions } from 'element-plus'
import { uploadImage } from '@/api/file'

const props = defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const loading = ref(false)
const url = computed({
  get: () => props.modelValue,
  set: (value: string) => emit('update:modelValue', value),
})

function beforeUpload(file: File) {
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.warning('图片不能超过 2MB')
    return false
  }
  return true
}

async function onUpload(options: UploadRequestOptions) {
  loading.value = true
  try {
    const { data } = await uploadImage(options.file as File)
    url.value = data.data.url
    ElMessage.success('上传成功')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.uploader {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

.preview {
  width: 96px;
  height: 96px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}
</style>
