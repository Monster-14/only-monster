<template>
  <el-card shadow="never">
    <template #header>
      <div class="head">
        <span>文章</span>
        <el-button type="primary" @click="router.push('/admin/articles/new')">写文章</el-button>
      </div>
    </template>
    <el-table :data="articles" empty-text="还没有文章">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="slug" label="别名" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">{{ row.status === 1 ? '已发布' : '草稿' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button link type="primary" @click="router.push(`/admin/articles/${row.id}`)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteAdminArticle, fetchAdminArticles } from '@/api/article'
import type { ArticleItem } from '@/api/types'

const router = useRouter()
const articles = ref<ArticleItem[]>([])

async function load() {
  const { data } = await fetchAdminArticles()
  articles.value = data.data
}

onMounted(load)

async function onDelete(row: ArticleItem) {
  await ElMessageBox.confirm(`确定删除「${row.title}」？`, '删除文章', { type: 'warning' })
  await deleteAdminArticle(row.id)
  ElMessage.success('已删除')
  await load()
}
</script>

<style scoped>
.head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
