<template>
  <el-card shadow="never">
    <template #header>
      <div class="head">
        <span>项目</span>
        <el-button type="primary" @click="router.push('/admin/projects/new')">新建项目</el-button>
      </div>
    </template>
    <el-table :data="projects" empty-text="还没有项目">
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="slug" label="别名" width="180" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">{{ row.status === 1 ? '已发布' : '草稿' }}</template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button link type="primary" @click="router.push(`/admin/projects/${row.id}`)">编辑</el-button>
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
import { deleteAdminProject, fetchAdminProjects } from '@/api/project'
import type { ProjectItem } from '@/api/types'

const router = useRouter()
const projects = ref<ProjectItem[]>([])

async function load() {
  const { data } = await fetchAdminProjects()
  projects.value = data.data
}

onMounted(load)

async function onDelete(row: ProjectItem) {
  await ElMessageBox.confirm(`确定删除「${row.title}」？`, '删除项目', { type: 'warning' })
  await deleteAdminProject(row.id)
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
