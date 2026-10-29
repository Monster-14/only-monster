<template>
  <section class="page">
    <h1>项目</h1>
    <p class="lead">已发布的作品会显示在这里。</p>
    <div v-if="projects.length" class="grid">
      <router-link v-for="item in projects" :key="item.id" class="card" :to="`/projects/${item.slug}`">
        <h2>{{ item.title }}</h2>
        <p>{{ item.summary }}</p>
        <div class="tags">
          <span v-for="tag in item.techStack" :key="tag">{{ tag }}</span>
        </div>
      </router-link>
    </div>
    <el-empty v-else description="还没有已发布的项目" />
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchPublicProjects } from '@/api/project'
import type { ProjectItem } from '@/api/types'

const projects = ref<ProjectItem[]>([])

onMounted(async () => {
  const { data } = await fetchPublicProjects()
  projects.value = data.data
})
</script>

<style scoped>
.page {
  max-width: 960px;
  margin: 0 auto;
  padding: 48px 24px 80px;
}

.lead {
  color: var(--monster-muted);
  margin-bottom: 32px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 16px;
}

.card {
  display: block;
  padding: 20px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  text-decoration: none;
  color: inherit;
}

.card h2 {
  margin: 0 0 8px;
  font-size: 18px;
}

.card p {
  margin: 0 0 12px;
  color: var(--monster-muted);
  line-height: 1.6;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tags span {
  font-size: 12px;
  color: var(--monster-primary);
  background: #eff4ff;
  padding: 2px 8px;
  border-radius: 999px;
}
</style>
