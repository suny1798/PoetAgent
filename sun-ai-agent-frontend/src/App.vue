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
    <footer class="app-footer">
      <div class="footer-inner">
        <div class="footer-left">
          <div class="footer-logo">Sun AI Agent</div>
          <div class="footer-links">
            <a href="https://github.com/suny1798" target="_blank" rel="noreferrer">GitHub</a>
          </div>
        </div>
        <div class="footer-right">
          <div class="footer-row">2026 Suny 版权所有</div>
          <div class="footer-row">本项目为 AI 超级智能体 / AI 诗歌生成学习示例</div>
        </div>
      </div>
    </footer>
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
  background: #f8fafc;
  color: #0f172a;
}

.app-header {
  padding: 16px 24px;
  border-bottom: 1px solid #e2e8f0;
  background: rgba(248, 250, 252, 0.85);
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
  color: #0f172a;
  text-decoration: none;
  border: 1px solid #e2e8f0;
  background: #ffffff;
}

.nav-link.router-link-active {
  background: rgba(59, 130, 246, 0.08);
  border-color: rgba(59, 130, 246, 0.45);
}

.app-main {
  flex: 1;
  padding: 16px 16px 8px;
}

.app-footer {
  border-top: 1px solid #e2e8f0;
  padding: 12px 16px;
  background: #f8fafc;
  font-size: 12px;
  color: #64748b;
}

.footer-inner {
  max-width: 1040px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.footer-logo {
  font-weight: 600;
  color: #0f172a;
}

.footer-links {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.footer-links a {
  color: #64748b;
  text-decoration: none;
}

.footer-links a:hover {
  color: #2563eb;
}

.footer-right {
  text-align: right;
}

.footer-row {
  line-height: 1.4;
}

@media (max-width: 768px) {
  .app-header {
    padding: 12px 12px;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .app-main {
    padding: 12px 12px 4px;
  }

  .footer-inner {
    align-items: flex-start;
  }

  .footer-right {
    text-align: left;
  }
}
</style>

