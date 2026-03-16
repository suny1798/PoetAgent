<template>
  <div class="chat-page">
    <div class="chat-header">
      <div>
        <h2 class="chat-title">{{ title }}</h2>
        <p class="chat-subtitle">{{ subtitle }}</p>
      </div>
      <div class="chat-meta">
        <span class="badge">会话 ID: {{ chatId }}</span>
      </div>
    </div>

    <div class="chat-header-row">
      <div class="chat-tags">
        <span class="tag">SSE 实时对话</span>
        <span class="tag">多轮上下文</span>
      </div>
      <div class="chat-hint">按 Enter 发送，Shift+Enter 换行</div>
    </div>

    <div class="chat-body">
      <div ref="scrollContainer" class="chat-messages">
        <div v-if="!messages.length && !loading" class="empty-state">
          <div class="empty-illustration"></div>
          <h3>还没有对话</h3>
          <p>从右下角输入你的问题，和智能体开始一段对话吧。</p>
          <ul>
            <li>试试描述一个你正在思考的问题</li>
            <li>或者让它帮你写一首诗 / 规划一个任务</li>
          </ul>
        </div>
        <template v-else>
          <div
            v-for="msg in messages"
            :key="msg.id"
            class="chat-message-row"
            :class="msg.role === 'user' ? 'is-user' : 'is-ai'"
          >
            <div class="avatar" :class="msg.role === 'user' ? 'avatar-user' : 'avatar-ai'">
              <span v-if="msg.role === 'user'">你</span>
              <span v-else>AI</span>
            </div>
            <div class="bubble">
              <div class="bubble-content">
                <div class="message-text">{{ msg.content }}</div>
              </div>
              <div class="bubble-meta">
                <span>{{ msg.role === 'user' ? '用户' : '智能体' }}</span>
              </div>
            </div>
          </div>
        </template>
        <div v-if="loading" class="typing">
          AI 正在思考中……
        </div>
      </div>

      <form class="chat-input-area" @submit.prevent="handleSubmit">
        <textarea
          v-model="input"
          class="chat-input"
          placeholder="请输入你的问题，然后回车发送。Shift+Enter 换行。"
          :disabled="loading"
          @keydown.enter.exact.prevent="handleEnter"
          @keydown.shift.enter.stop
        ></textarea>
        <div class="chat-actions">
          <button type="button" class="btn-ghost" @click="handleClear" :disabled="loading">
            清空对话
          </button>
          <button type="submit" class="btn-primary" :disabled="!input.trim() || loading">
            发送
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onBeforeUnmount } from 'vue';

const props = defineProps({
  title: { type: String, required: true },
  subtitle: { type: String, required: true },
  chatId: { type: String, required: true },
  messages: { type: Array, required: true },
  loading: { type: Boolean, default: false }
});

const emits = defineEmits(['send', 'clear']);

const input = ref('');
const scrollContainer = ref(null);

watch(
  () => props.messages.length,
  () => {
    requestAnimationFrame(() => {
      if (scrollContainer.value) {
        scrollContainer.value.scrollTop = scrollContainer.value.scrollHeight;
      }
    });
  }
);

const handleEnter = () => {
  if (!input.value.trim() || props.loading) return;
  handleSubmit();
};

const handleSubmit = () => {
  const text = input.value.trim();
  if (!text) return;
  emits('send', text);
  input.value = '';
};

const handleClear = () => {
  emits('clear');
  input.value = '';
};

onBeforeUnmount(() => {
  // 占位，方便父组件在销毁时清理 SSE 等资源
});
</script>

<style scoped>
.chat-page {
  max-width: 960px;
  margin: 0 auto;
  height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 12px;
}

.chat-title {
  font-size: 22px;
  margin: 0 0 4px;
}

.chat-subtitle {
  margin: 0;
  font-size: 13px;
  color: #475569;
}

.chat-meta {
  font-size: 12px;
  color: #475569;
}

.badge {
  padding: 4px 8px;
  border-radius: 999px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
}

.chat-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  border-radius: 18px;
  border: 1px solid #e2e8f0;
  background: radial-gradient(circle at top left, #e0f2fe, transparent 55%),
    radial-gradient(circle at bottom right, #f1f5f9, transparent 55%), #ffffff;
  backdrop-filter: blur(8px);
  overflow: hidden;
}

.chat-header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 12px;
  color: #64748b;
}

.chat-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.tag {
  padding: 2px 8px;
  border-radius: 999px;
  background: #eff6ff;
  color: #1d4ed8;
}

.chat-hint {
  text-align: right;
}

.chat-messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.empty-state {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #64748b;
  gap: 8px;
}

.empty-illustration {
  width: 120px;
  height: 80px;
  border-radius: 24px;
  background: linear-gradient(135deg, #e0f2fe, #f1f5f9);
  position: relative;
  margin-bottom: 8px;
}

.empty-illustration::before,
.empty-illustration::after {
  content: '';
  position: absolute;
  border-radius: 999px;
  background: #ffffff;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.06);
}

.empty-illustration::before {
  width: 42px;
  height: 26px;
  left: 14px;
  top: 22px;
}

.empty-illustration::after {
  width: 32px;
  height: 22px;
  right: 16px;
  bottom: 16px;
}

.empty-state h3 {
  margin: 0;
  font-size: 16px;
  color: #0f172a;
}

.empty-state p {
  margin: 0;
  font-size: 13px;
}

.empty-state ul {
  margin: 6px 0 0;
  padding-left: 18px;
  text-align: left;
  font-size: 12px;
}

.chat-message-row {
  display: flex;
  margin-bottom: 10px;
}

.chat-message-row.is-user {
  flex-direction: row-reverse;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  margin: 0 8px;
}

.avatar-user {
  background: rgba(59, 130, 246, 0.9);
}

.avatar-ai {
  background: rgba(16, 185, 129, 0.9);
}

.bubble {
  max-width: 70%;
}

.bubble-content {
  padding: 10px 12px;
  border-radius: 16px;
  background: #f1f5f9;
  border: 1px solid #e2e8f0;
  max-width: 100%;
  overflow-x: hidden;
}

.chat-message-row.is-user .bubble-content {
  background: rgba(59, 130, 246, 0.12);
  border-color: rgba(59, 130, 246, 0.25);
}

.message-text {
  white-space: pre-wrap;
  word-break: break-word;
  overflow-wrap: anywhere;
  line-height: 1.5;
}

.bubble-meta {
  margin-top: 4px;
  font-size: 11px;
  color: #64748b;
}

.chat-input-area {
  border-top: 1px solid #e2e8f0;
  padding: 10px 12px;
  background: #ffffff;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.chat-input {
  width: 100%;
  min-height: 70px;
  max-height: 160px;
  resize: vertical;
  border-radius: 12px;
  border: 1px solid #cbd5e1;
  padding: 8px 10px;
  background: #ffffff;
  color: #0f172a;
  font-size: 14px;
  outline: none;
}

.chat-input:focus {
  border-color: rgba(59, 130, 246, 0.65);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.chat-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.btn-primary,
.btn-ghost {
  border-radius: 999px;
  padding: 6px 16px;
  font-size: 14px;
  border: 1px solid transparent;
  cursor: pointer;
}

.btn-primary {
  background: rgba(59, 130, 246, 0.9);
  border-color: rgba(59, 130, 246, 1);
  color: white;
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-ghost {
  background: transparent;
  border-color: #cbd5e1;
  color: #0f172a;
}

.typing {
  font-size: 13px;
  color: #2563eb;
}

@media (max-width: 1024px) {
  .chat-page {
    height: auto;
    min-height: calc(100vh - 140px);
  }
}

@media (max-width: 768px) {
  .chat-page {
    padding-bottom: 4px;
  }

  .chat-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }

  .chat-header-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }

  .chat-messages {
    padding: 12px;
  }

  .chat-input {
    min-height: 60px;
  }
}
</style>

