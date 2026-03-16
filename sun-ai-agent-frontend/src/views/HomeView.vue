<template>
  <div class="home">
    <section class="hero">
      <h2>欢迎使用 Sun AI Agent</h2>
      <p>请选择一个应用，开始你的智能创作之旅。</p>
    </section>

    <section class="cards">
      <RouterLink to="/poet" class="card">
        <h3>AI 诗歌生成大师</h3>
        <p>聊天式生成优美诗歌，支持多轮对话与上下文记忆。</p>
      </RouterLink>

      <RouterLink to="/manus" class="card">
        <h3>AI 超级智能体</h3>
        <p>具备工具调用能力的智能体，为你解决更复杂的问题。</p>
      </RouterLink>
    </section>
  </div>
</template>

<script setup>
import { RouterLink } from 'vue-router';
</script>

<style scoped>
.home {
  max-width: 960px;
  margin: 0 auto;
}

.hero {
  margin-bottom: 24px;
}

.hero h2 {
  font-size: 28px;
  margin-bottom: 8px;
}

.hero p {
  color: #9ca3af;
}

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.card {
  display: block;
  padding: 20px;
  border-radius: 16px;
  border: 1px solid rgba(148, 163, 184, 0.4);
  background: radial-gradient(circle at top left, rgba(56, 189, 248, 0.2), transparent),
    radial-gradient(circle at bottom right, rgba(129, 140, 248, 0.2), transparent);
  text-decoration: none;
  color: inherit;
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.15s ease;
}

.card h3 {
  font-size: 18px;
  margin-bottom: 6px;
}

.card p {
  font-size: 14px;
  color: #d1d5db;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 18px 45px rgba(15, 23, 42, 0.8);
  border-color: rgba(59, 130, 246, 0.8);
}
</style>

