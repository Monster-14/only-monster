<template>
  <section class="hero">
    <div v-if="profile" class="intro">
      <img v-if="profile.avatarUrl" class="avatar" :src="profile.avatarUrl" :alt="profile.name" />
      <div v-else class="avatar avatar-fallback">{{ initials }}</div>
      <p class="eyebrow">{{ profile.headline || 'PERSONAL SITE' }}</p>
      <h1>{{ profile.name }}</h1>
      <p class="lead">{{ profile.bio }}</p>
      <div class="links">
        <a v-if="profile.githubUrl" :href="profile.githubUrl" target="_blank" rel="noreferrer">GitHub</a>
        <a v-if="profile.email" :href="`mailto:${profile.email}`">邮箱</a>
        <a v-if="profile.blogUrl" :href="profile.blogUrl" target="_blank" rel="noreferrer">博客</a>
        <span v-if="profile.wechat">微信：{{ profile.wechat }}</span>
      </div>
    </div>
    <el-skeleton v-else :rows="4" animated />
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { fetchPublicProfile } from '@/api/profile'
import type { SiteProfile } from '@/api/types'

const profile = ref<SiteProfile | null>(null)

const initials = computed(() => (profile.value?.name || 'M').slice(0, 1).toUpperCase())

onMounted(async () => {
  const { data } = await fetchPublicProfile()
  profile.value = data.data
})
</script>

<style scoped>
.hero {
  max-width: 880px;
  margin: 0 auto;
  padding: 72px 24px 96px;
}

.intro {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.avatar {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 20px;
}

.avatar-fallback {
  display: grid;
  place-items: center;
  background: var(--monster-primary);
  color: #fff;
  font-size: 32px;
  font-weight: 700;
}

.eyebrow {
  letter-spacing: 0.12em;
  color: var(--monster-primary);
  font-size: 13px;
  margin-bottom: 12px;
}

h1 {
  font-size: 40px;
  margin: 0 0 16px;
}

.lead {
  color: var(--monster-muted);
  font-size: 16px;
  line-height: 1.7;
  margin-bottom: 24px;
  white-space: pre-wrap;
}

.links {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  color: var(--monster-muted);
}

.links a {
  color: var(--monster-primary);
  text-decoration: none;
}
</style>
