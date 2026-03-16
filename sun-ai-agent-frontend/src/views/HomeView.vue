<template>
  <div class="home">
    <section class="hero">
      <h2>欢迎使用 Sun AI Agent</h2>
      <p>请选择一个应用，开始你的智能创作之旅。</p>
    </section>

    <section class="cards">
      <RouterLink to="/poet" class="card">
        <div class="card-header">
          <div class="card-icon card-icon-poet">诗</div>
          <div>
            <h3>AI 诗歌生成大师</h3>
            <p>聊天式生成优美诗歌，支持多轮对话与上下文记忆。</p>
          </div>
        </div>
        <div class="card-tags">
          <span class="tag tag-primary">实时生成</span>
          <span class="tag">古今风格</span>
          <span class="tag">创意灵感</span>
        </div>
        <div class="card-footer">进入应用 &rarr;</div>
      </RouterLink>

      <RouterLink to="/manus" class="card">
        <div class="card-header">
          <div class="card-icon card-icon-manus">智</div>
          <div>
            <h3>AI 超级智能体</h3>
            <p>具备工具调用能力的智能体，为你解决更复杂的问题。</p>
          </div>
        </div>
        <div class="card-tags">
          <span class="tag tag-primary">多步骤推理</span>
          <span class="tag">工具调用</span>
          <span class="tag">任务编排</span>
        </div>
        <div class="card-footer">进入应用 &rarr;</div>
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
  color: #475569;
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
  border: 1px solid #e2e8f0;
  background: #ffffff;
  text-decoration: none;
  color: inherit;
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.15s ease;
}

.card-header {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 10px;
}

.card-icon {
  width: 40px;
  height: 40px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
  color: #0f172a;
}

.card-icon-poet {
  background: linear-gradient(135deg, #bfdbfe, #e0f2fe);
}

.card-icon-manus {
  background: linear-gradient(135deg, #bbf7d0, #e0f2fe);
}

.card h3 {
  font-size: 18px;
  margin-bottom: 6px;
}

.card p {
  font-size: 14px;
  color: #475569;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin: 8px 0 12px;
}

.tag {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 999px;
  background: #f1f5f9;
  color: #64748b;
}

.tag-primary {
  background: rgba(59, 130, 246, 0.12);
  color: #1d4ed8;
}

.card-footer {
  font-size: 13px;
  color: #2563eb;
  margin-top: 2px;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 30px rgba(15, 23, 42, 0.08);
  border-color: rgba(59, 130, 246, 0.4);
}
</style>

