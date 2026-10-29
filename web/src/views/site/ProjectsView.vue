<template>
  <section class="page">
    <p class="kicker">项目</p>
    <h1>做过的东西</h1>
    <p class="lead">已发布的作品会显示在这里，封面和链接来自后台真实数据。</p>
    <div v-if="loading" class="muted">正在读取项目…</div>
    <p v-else-if="error" class="muted">{{ error }}</p>
    <div v-else-if="projects.length" class="list">
      <router-link v-for="item in projects" :key="item.id" class="row" :to="`/projects/${item.slug}`">
        <img v-if="item.coverUrl" :src="item.coverUrl" :alt="item.title" />
        <div>
          <h2>{{ item.title }}</h2>
          <p>{{ item.summary }}</p>
          <div class="tags">
            <span v-for="tag in item.techStack" :key="tag">{{ tag }}</span>
          </div>
        </div>
      </router-link>
    </div>
    <p v-else class="muted">还没有已发布的项目。</p>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchPublicProjects } from '@/api/project'
import type { ProjectItem } from '@/api/types'

const projects = ref<ProjectItem[]>([])
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const { data } = await fetchPublicProjects()
    projects.value = data.data
  } catch {
    error.value = '项目列表暂时无法加载。'
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page {
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
.muted {
  color: #6b6560;
  line-height: 1.7;
}

.list {
  display: grid;
  gap: 8px;
  margin-top: 32px;
}

.row {
  display: grid;
  grid-template-columns: 140px 1fr;
  gap: 20px;
  padding: 20px 0;
  border-bottom: 1px solid #e8e1d6;
  text-decoration: none;
  color: inherit;
}

.row:not(:has(img)) {
  grid-template-columns: 1fr;
}

.row img {
  width: 100%;
  height: 88px;
  object-fit: cover;
  border-radius: 14px;
  background: #ece6dc;
}

h2 {
  margin: 0 0 8px;
  font-size: 22px;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  color: #635bff;
  font-size: 12px;
}

@media (max-width: 640px) {
  .row {
    grid-template-columns: 1fr;
  }
}
</style>
