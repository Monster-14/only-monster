<template>
  <div class="site">
    <a class="skip" href="#content">跳到内容</a>
    <header class="site-header">
      <router-link class="brand" to="/">Monster</router-link>
      <button class="menu" type="button" :aria-expanded="navOpen" aria-label="打开导航" @click="navOpen = !navOpen">
        菜单
      </button>
      <nav :class="{ open: navOpen }">
        <router-link to="/" @click="navOpen = false">首页</router-link>
        <router-link to="/projects" @click="navOpen = false">项目</router-link>
        <a href="/#resume" @click="goResume">履历</a>
        <router-link to="/blog" @click="navOpen = false">文章</router-link>
        <router-link to="/contact" @click="navOpen = false">联系</router-link>
        <router-link to="/admin/login" @click="navOpen = false">后台</router-link>
      </nav>
    </header>
    <main id="content" class="site-main">
      <router-view />
    </main>
    <footer class="site-footer">Monster 的数字工作室</footer>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const navOpen = ref(false)

watch(
  () => route.fullPath,
  () => {
    navOpen.value = false
  },
)

function goResume(event: Event) {
  event.preventDefault()
  navOpen.value = false
  router.push({ path: '/', hash: '#resume' })
}
</script>

<style scoped>
.site {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--monster-bg);
  color: var(--monster-text);
}

.skip {
  position: absolute;
  left: -999px;
  top: 8px;
}

.skip:focus {
  left: 16px;
  z-index: 40;
  background: #fff;
  color: #202124;
  padding: 8px 12px;
}

.site-header {
  position: sticky;
  top: 0;
  z-index: 20;
  min-height: 72px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  background: #f5f3ee;
  border-bottom: 1px solid #e8e1d6;
}

.brand {
  font-weight: 700;
  color: var(--monster-text);
  text-decoration: none;
  font-size: 18px;
  letter-spacing: -0.03em;
}

.menu {
  display: none;
  border: 0;
  background: transparent;
  font: inherit;
  color: inherit;
  cursor: pointer;
}

nav {
  display: flex;
  gap: 22px;
}

nav a {
  color: var(--monster-muted);
  text-decoration: none;
  font-size: 14px;
}

nav a:focus-visible {
  outline: 2px solid #635bff;
  outline-offset: 4px;
}

nav a.router-link-active {
  color: var(--monster-text);
}

.site-main {
  flex: 1;
  width: min(1180px, calc(100% - 48px));
  margin: 0 auto;
}

.site-footer {
  text-align: center;
  padding: 28px 16px 40px;
  color: var(--monster-muted);
  font-size: 13px;
}

@media (max-width: 760px) {
  .site-header {
    padding: 0 16px;
    flex-wrap: wrap;
  }

  .menu {
    display: inline-flex;
  }

  nav {
    display: none;
    width: 100%;
    flex-direction: column;
    padding-bottom: 16px;
    gap: 12px;
  }

  nav.open {
    display: flex;
  }

  .site-main {
    width: calc(100% - 32px);
  }
}
</style>
