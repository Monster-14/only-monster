<template>
  <section class="page">
    <p class="kicker">文章</p>
    <h1>技术笔记</h1>
    <p class="lead">已发布的 Markdown 文章会显示在这里。</p>
    <div v-if="loading" class="muted">正在读取文章…</div>
    <p v-else-if="error" class="muted">{{ error }}</p>
    <div v-else-if="articles.length" class="list">
      <router-link v-for="item in articles" :key="item.id" class="row" :to="`/blog/${item.slug}`">
        <h2>{{ item.title }}</h2>
        <p>{{ item.summary }}</p>
        <span v-if="item.publishedAt">{{ item.publishedAt.slice(0, 10) }}</span>
      </router-link>
    </div>
    <p v-else class="muted">还没有已发布的文章。</p>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchPublicArticles } from '@/api/article'
import type { ArticleItem } from '@/api/types'

const articles = ref<ArticleItem[]>([])
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const { data } = await fetchPublicArticles()
    articles.value = data.data
  } catch {
    error.value = '文章列表暂时无法加载。'
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page {
  max-width: 760px;
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
.muted,
.row p,
.row span {
  color: #6b6560;
  line-height: 1.7;
}

.list {
  display: grid;
  gap: 8px;
  margin-top: 32px;
}

.row {
  display: block;
  padding: 20px 0;
  border-bottom: 1px solid #e8e1d6;
  text-decoration: none;
  color: inherit;
}

h2 {
  margin: 0 0 8px;
  font-size: 24px;
}
</style>
