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
    <p v-if="extra?.goal"><span>目标</span>{{ extra.goal }}</p>
    <p v-if="extra?.contribution"><span>贡献</span>{{ extra.contribution }}</p>
    <p v-if="extra?.outcome"><span>成果</span>{{ extra.outcome }}</p>
    <img v-if="project.coverUrl" class="cover" :src="project.coverUrl" :alt="project.title" />
    <article class="content">{{ project.content }}</article>
  </section>
  <section v-else class="page">
    <p class="summary">{{ error || '正在读取项目…' }}</p>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchPublicProject } from '@/api/project'
import { highlightFor } from '@/data/resume'
import type { ProjectItem } from '@/api/types'

const route = useRoute()
const router = useRouter()
const project = ref<ProjectItem | null>(null)
const error = ref('')
const extra = computed(() => (project.value ? highlightFor(project.value.slug) : undefined))

onMounted(async () => {
  try {
    const { data } = await fetchPublicProject(String(route.params.slug))
    project.value = data.data
  } catch {
    error.value = '没有找到这个项目。'
    setTimeout(() => router.replace('/projects'), 1200)
  }
})
</script>

<style scoped>
.page {
  max-width: 760px;
  padding: 48px 0 96px;
}

.back,
.summary,
.content {
  color: #6b6560;
  text-decoration: none;
  line-height: 1.8;
}

h1 {
  margin: 16px 0;
  font-size: 40px;
  letter-spacing: -0.04em;
}

.tags,
.links {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin: 16px 0;
}

.tags span,
.links a,
p span {
  color: #635bff;
}

p span {
  display: inline-block;
  min-width: 42px;
  margin-right: 8px;
}

.cover {
  width: 100%;
  max-height: 320px;
  object-fit: cover;
  border-radius: 18px;
  margin: 16px 0;
}

.content {
  white-space: pre-wrap;
  user-select: text;
}
</style>
