<template>
  <section class="page">
    <h1>文章</h1>
    <p class="lead">已发布的 Markdown 文章会显示在这里。</p>
    <div v-if="articles.length" class="list">
      <router-link v-for="item in articles" :key="item.id" class="card" :to="`/blog/${item.slug}`">
        <h2>{{ item.title }}</h2>
        <p>{{ item.summary }}</p>
        <span v-if="item.publishedAt" class="date">{{ formatDate(item.publishedAt) }}</span>
      </router-link>
    </div>
    <el-empty v-else description="还没有已发布的文章" />
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchPublicArticles } from '@/api/article'
import type { ArticleItem } from '@/api/types'

const articles = ref<ArticleItem[]>([])

function formatDate(value: string) {
  return value.slice(0, 10)
}

onMounted(async () => {
  const { data } = await fetchPublicArticles()
  articles.value = data.data
})
</script>

<style scoped>
.page {
  max-width: 800px;
  margin: 0 auto;
  padding: 48px 24px 80px;
}

.lead {
  color: var(--monster-muted);
  margin-bottom: 32px;
}

.list {
  display: grid;
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
  font-size: 20px;
}

.card p {
  margin: 0;
  color: var(--monster-muted);
  line-height: 1.6;
}

.date {
  display: inline-block;
  margin-top: 12px;
  color: var(--monster-muted);
  font-size: 13px;
}
</style>
