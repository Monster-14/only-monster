<template>
  <div class="site">
    <header class="site-header">
      <router-link class="brand" to="/">Monster</router-link>
      <nav>
        <a href="#health">联调状态</a>
      </nav>
    </header>
    <main class="site-main">
      <router-view />
    </main>
    <footer class="site-footer">Vue3 + Spring Boot 脚手架 · 第一步</footer>
  </div>
</template>

<style scoped>
.site {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.site-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  background: #fff;
  border-bottom: 1px solid #e2e8f0;
}

.brand {
  font-weight: 700;
  color: var(--monster-primary);
  text-decoration: none;
  font-size: 20px;
}

.site-header nav a {
  color: var(--monster-muted);
  text-decoration: none;
}

.site-main {
  flex: 1;
}

.site-footer {
  text-align: center;
  padding: 16px;
  color: var(--monster-muted);
  font-size: 13px;
}
</style>
