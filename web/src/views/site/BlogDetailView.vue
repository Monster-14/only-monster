<template>
  <section v-if="article" class="page">
    <router-link class="back" to="/blog">← 返回文章</router-link>
    <h1>{{ article.title }}</h1>
    <p v-if="article.publishedAt" class="date">{{ article.publishedAt.slice(0, 10) }}</p>
    <p class="summary">{{ article.summary }}</p>
    <article class="markdown" v-html="html" />
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchPublicArticle } from '@/api/article'
import { renderMarkdown } from '@/utils/markdown'
import type { ArticleItem } from '@/api/types'
import 'highlight.js/styles/github-dark.css'

const route = useRoute()
const router = useRouter()
const article = ref<ArticleItem | null>(null)
const html = computed(() => renderMarkdown(article.value?.contentMd || ''))

onMounted(async () => {
  try {
    const { data } = await fetchPublicArticle(String(route.params.slug))
    article.value = data.data
  } catch {
    router.replace('/blog')
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

.date,
.summary {
  color: var(--monster-muted);
}

.markdown {
  line-height: 1.8;
}

.markdown :deep(pre) {
  padding: 16px;
  overflow: auto;
  border-radius: 8px;
  background: #0f172a;
}

.markdown :deep(code) {
  font-family: Consolas, "Courier New", monospace;
}

.markdown :deep(a) {
  color: var(--monster-primary);
}
</style>
