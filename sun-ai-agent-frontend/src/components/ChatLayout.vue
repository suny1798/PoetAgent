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

    <div class="chat-body">
      <div ref="scrollContainer" class="chat-messages">
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
              <pre>{{ msg.content }}</pre>
            </div>
            <div class="bubble-meta">
              <span>{{ msg.role === 'user' ? '用户' : '智能体' }}</span>
            </div>
          </div>
        </div>
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
  height: calc(100vh - 90px);
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
  color: #9ca3af;
}

.chat-meta {
  font-size: 12px;
  color: #9ca3af;
}

.badge {
  padding: 4px 8px;
  border-radius: 999px;
  background: rgba(15, 23, 42, 0.7);
  border: 1px solid rgba(148, 163, 184, 0.5);
}

.chat-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  border-radius: 18px;
  border: 1px solid rgba(148, 163, 184, 0.4);
  background: radial-gradient(circle at top, rgba(15, 118, 110, 0.2), transparent),
    radial-gradient(circle at bottom, rgba(59, 130, 246, 0.2), transparent);
  backdrop-filter: blur(8px);
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
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
  background: rgba(15, 23, 42, 0.9);
  border: 1px solid rgba(148, 163, 184, 0.5);
  white-space: pre-wrap;
  word-break: break-word;
}

.chat-message-row.is-user .bubble-content {
  background: rgba(37, 99, 235, 0.9);
}

.bubble-content pre {
  margin: 0;
  font-family: inherit;
}

.bubble-meta {
  margin-top: 4px;
  font-size: 11px;
  color: #9ca3af;
}

.chat-input-area {
  border-top: 1px solid rgba(148, 163, 184, 0.4);
  padding: 10px 12px;
  background: rgba(15, 23, 42, 0.8);
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
  border: 1px solid rgba(148, 163, 184, 0.6);
  padding: 8px 10px;
  background: rgba(15, 23, 42, 0.9);
  color: #e5e7eb;
  font-size: 14px;
  outline: none;
}

.chat-input:focus {
  border-color: rgba(59, 130, 246, 0.9);
  box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.5);
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
  border-color: rgba(148, 163, 184, 0.7);
  color: #e5e7eb;
}

.typing {
  font-size: 13px;
  color: #a5b4fc;
}
</style>

