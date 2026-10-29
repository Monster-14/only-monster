<template>
  <section v-if="project" class="page">
    <router-link class="back" to="/projects">← 返回项目</router-link>
    <h1>{{ project.title }}</h1>
    <p class="summary">{{ project.summary }}</p>
    <div class="tags">
      <span v-for="tag in project.techStack" :key="tag">{{ tag }}</span>
    </div>
    <div class="links">
      <a v-if="project.demoUrl" :href="project.demoUrl" target="_blank" rel="noreferrer">在线演示</a>
      <a v-if="project.repoUrl" :href="project.repoUrl" target="_blank" rel="noreferrer">源码仓库</a>
    </div>
    <article class="content">{{ project.content }}</article>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchPublicProject } from '@/api/project'
import type { ProjectItem } from '@/api/types'

const route = useRoute()
const router = useRouter()
const project = ref<ProjectItem | null>(null)

onMounted(async () => {
  try {
    const { data } = await fetchPublicProject(String(route.params.slug))
    project.value = data.data
  } catch {
    router.replace('/projects')
  }
})
</script>

<style scoped>
.page {
  max-width: 800px;
  margin: 0 auto;
  padding: 48px 24px 80px;
}

.back {
  color: var(--monster-muted);
  text-decoration: none;
}

.summary {
  color: var(--monster-muted);
  line-height: 1.7;
}

.tags,
.links {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin: 16px 0;
}

.tags span {
  font-size: 12px;
  color: var(--monster-primary);
  background: #eff4ff;
  padding: 2px 8px;
  border-radius: 999px;
}

.links a {
  color: var(--monster-primary);
  text-decoration: none;
}

.content {
  white-space: pre-wrap;
  line-height: 1.8;
}
</style>
