<template>
  <section v-if="article" class="page">
    <router-link class="back" to="/blog">← 返回文章</router-link>
    <h1>{{ article.title }}</h1>
    <p v-if="article.publishedAt" class="meta">{{ article.publishedAt.slice(0, 10) }}</p>
    <p class="meta">{{ article.summary }}</p>
    <article class="markdown" v-html="html" />
  </section>
  <section v-else class="page">
    <p class="meta">{{ error || '正在读取文章…' }}</p>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchPublicArticle } from '@/api/article'
import { renderMarkdown } from '@/utils/markdown'
import type { ArticleItem } from '@/api/types'
import 'highlight.js/styles/github.css'

const route = useRoute()
const router = useRouter()
const article = ref<ArticleItem | null>(null)
const error = ref('')
const html = computed(() => renderMarkdown(article.value?.contentMd || ''))

onMounted(async () => {
  try {
    const { data } = await fetchPublicArticle(String(route.params.slug))
    article.value = data.data
  } catch {
    error.value = '没有找到这篇文章。'
    setTimeout(() => router.replace('/blog'), 1200)
  }
})
</script>

<style scoped>
.page {
  max-width: 760px;
  padding: 48px 0 96px;
}

.back,
.meta {
  color: #6b6560;
  text-decoration: none;
}

h1 {
  margin: 16px 0;
  font-size: 40px;
  letter-spacing: -0.04em;
}

.markdown {
  line-height: 1.85;
  user-select: text;
}

.markdown :deep(pre) {
  padding: 16px;
  overflow: auto;
  border-radius: 12px;
  background: #202124;
  color: #f5f3ee;
}

.markdown :deep(code) {
  font-family: Consolas, "Courier New", monospace;
}

.markdown :deep(a) {
  color: #635bff;
}
</style>
