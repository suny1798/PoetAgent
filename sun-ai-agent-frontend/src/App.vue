<template>
  <div class="app-root">
    <header class="app-header">
      <h1 class="app-title">Sun AI Agent</h1>
      <nav class="app-nav">
        <RouterLink to="/" class="nav-link">主页</RouterLink>
        <RouterLink to="/poet" class="nav-link">AI 诗歌生成大师</RouterLink>
        <RouterLink to="/manus" class="nav-link">AI 超级智能体</RouterLink>
      </nav>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router';
</script>

<style scoped>
.app-root {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #0f172a, #020617);
  color: #e5e7eb;
}

.app-header {
  padding: 16px 24px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.3);
  display: flex;
  align-items: center;
  justify-content: space-between;
  backdrop-filter: blur(10px);
  position: sticky;
  top: 0;
  z-index: 10;
}

.app-title {
  font-size: 20px;
  font-weight: 600;
}

.app-nav {
  display: flex;
  gap: 12px;
}

.nav-link {
  padding: 6px 12px;
  border-radius: 9999px;
  font-size: 14px;
  color: #e5e7eb;
  text-decoration: none;
  border: 1px solid transparent;
}

.nav-link.router-link-active {
  background: rgba(59, 130, 246, 0.15);
  border-color: rgba(59, 130, 246, 0.7);
}

.app-main {
  flex: 1;
  padding: 16px;
}
</style>

