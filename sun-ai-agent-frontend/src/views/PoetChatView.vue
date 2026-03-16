<template>
  <ChatLayout
    title="AI 诗歌生成大师"
    subtitle="与 AI 对话，共同创作优美诗篇。"
    :chat-id="chatId"
    :messages="messages"
    :loading="loading"
    @send="handleSend"
    @clear="handleClear"
  />
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue';
import ChatLayout from '../components/ChatLayout.vue';

const API_BASE = 'http://localhost:8888/api';

const chatId = ref(createChatId());
const messages = ref([]);
const loading = ref(false);
let eventSource = null;

function createChatId() {
  return 'poet-' + Date.now().toString(36) + '-' + Math.random().toString(16).slice(2, 8);
}

const closeStream = () => {
  if (eventSource) {
    eventSource.close();
    eventSource = null;
  }
};

const handleSend = (text) => {
  if (!text || loading.value) return;

  closeStream();

  const userMsg = {
    id: `u-${Date.now()}`,
    role: 'user',
    content: text
  };
  messages.value.push(userMsg);

  const aiMsg = {
    id: `a-${Date.now()}`,
    role: 'ai',
    content: ''
  };
  messages.value.push(aiMsg);

  loading.value = true;

  const url = new URL(`${API_BASE}/ai/poet_app/chat/sse`);
  url.searchParams.set('message', text);
  url.searchParams.set('chatId', chatId.value);

  eventSource = new EventSource(url.toString());

  eventSource.onmessage = (event) => {
    if (event.data === '[DONE]' || event.data === 'DONE') {
      loading.value = false;
      closeStream();
      return;
    }
    aiMsg.content += event.data;
  };

  eventSource.onerror = () => {
    loading.value = false;
    closeStream();
  };
};

const handleClear = () => {
  closeStream();
  messages.value = [];
  chatId.value = createChatId();
};

onBeforeUnmount(() => {
  closeStream();
});
</script>

