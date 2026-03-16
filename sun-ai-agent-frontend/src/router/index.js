import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import PoetChatView from '../views/PoetChatView.vue';
import ManusChatView from '../views/ManusChatView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {
      title: 'Sun AI Agent - AI 诗歌生成大师 & 超级智能体',
      description:
        'Sun AI Agent 首页，选择 AI 诗歌生成大师或 AI 超级智能体，体验基于大模型的智能创作与任务编排。'
    }
  },
  {
    path: '/poet',
    name: 'poet',
    component: PoetChatView,
    meta: {
      title: 'AI 诗歌生成大师 - Sun AI Agent',
      description:
        '与 AI 诗歌生成大师进行聊天式创作，支持多轮对话与 SSE 流式输出，轻松生成个性化诗歌内容。'
    }
  },
  {
    path: '/manus',
    name: 'manus',
    component: ManusChatView,
    meta: {
      title: 'AI 超级智能体对话 - Sun AI Agent',
      description:
        '使用 AI 超级智能体进行多步骤推理与任务编排，通过 SSE 实时查看智能体每个思考步骤的输出。'
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.afterEach((to) => {
  const defaultTitle = 'Sun AI Agent - AI 对话应用';
  const defaultDesc =
    'Sun AI Agent 提供 AI 诗歌生成大师与 AI 超级智能体两大应用，支持 SSE 流式多轮对话。';

  if (typeof document !== 'undefined') {
    document.title = to.meta.title || defaultTitle;
    const descTag = document.querySelector('meta[name="description"]');
    if (descTag) {
      descTag.setAttribute('content', to.meta.description || defaultDesc);
    }
  }
});

export default router;

