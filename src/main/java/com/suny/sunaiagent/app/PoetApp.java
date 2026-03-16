package com.suny.sunaiagent.app;


import com.suny.sunaiagent.advisor.MyLoggerAdvisor;
import com.suny.sunaiagent.advisor.MyReReadingAdvisor;
import com.suny.sunaiagent.chatmemory.FileBaseChatMemory;
import com.suny.sunaiagent.rag.QueryWriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;
import org.springframework.ai.tool.ToolCallbackProvider;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class PoetApp {

    // 注入向量存储
    @Resource
    private VectorStore poetAppVectorStore;

    @Resource
    private QueryWriter queryWriter;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

//    @Resource
//    private VectorStore pgVectorStore;

    private final ChatClient chatClient;

    private static final String DEFAULT_SYSTEM_PROMPT = """
            你是「诗歌大师」，一位精通中国古典诗词与现代诗创作的诗人型 AI，擅长根据用户需求创作高质量诗歌。
            在创作前，请先理解用户需求。如果信息不足，先提出1–2个简短问题，了解以下内容：
            * 诗歌主题或情感
            * 诗歌类型（如五言绝句、七言律诗、现代诗等）
            * 是否包含特定意象或关键词
            * 是否有结构要求（如藏头诗）
            当需求明确后再生成诗歌。诗歌应语言优美、意象清晰、情感自然；若为古典诗词，应尽量保持基本格律和押韵。
            生成后可简要询问用户是否需要修改主题、风格或结构，并继续优化作品。
            """;

    public PoetApp(ChatModel dashcopeChatModel) {

        //基于文件的对话记忆
//        String fileDir = System.getProperty("user.dir") + "/tmp/chat_memory";
//        ChatMemory chatMemory = new FileBaseChatMemory(fileDir);

        //基于内存的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
        chatClient = ChatClient.builder(dashcopeChatModel)
                .defaultSystem(DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        //日志拦截器
                        new MyLoggerAdvisor()
//                        自定义 Re 2拦截器
//                        , new MyReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话（支持多轮对话） 同步方法
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String text = null;
        if (chatResponse != null) {
            text = chatResponse.getResult().getOutput().getText();
        }
//        log.info("content: {}",text);
        return text;
    }

    /**
     * AI 基础对话（支持多轮对话） SSE异步传输  其他方式的对话（RAG Tool MAC 实现方式一样）
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId){
        //响应式传输
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();

//        content1.subscribe(content-> log.info("content: {}",content));

    }

    record PoetReport(String title, String content){}

    /**
     * AI 基础对话 结构化输出
     * @param message
     * @param chatId
     * @return
     */
    public PoetReport doChatWithReport(String message, String chatId){
        PoetReport poetReport = chatClient.prompt()
                .user(message)
                .system(DEFAULT_SYSTEM_PROMPT + "每次对话结束后都要生成诗歌创作结果，标题为{用户}的需求简洁版，内容为生成的诗歌文本")
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(PoetReport.class);

        log.info("poetReport: {}",poetReport);
        return poetReport;
    }

    /**
     * AI 基础对话 + 向量存储
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRAG(String message, String chatId){

        //自定义高级RAG检索增强
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.5)
                        .vectorStore(poetAppVectorStore)

                        //查询过滤器
                        .filterExpression(new FilterExpressionBuilder()
                                .eq("status", "情感")
                                .build())
                        .build())

                //空上下文处理器增强
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
//                        .emptyContextPromptTemplate(NULL_PROMPT)
                        .build())

                .build();


        //查询重写
        String writeQuery = queryWriter.writeQuery(message);
        //todo 查询翻译

        ChatResponse chatResponse = chatClient.prompt()
                .user(writeQuery)
                .system(DEFAULT_SYSTEM_PROMPT)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                //自定义初级RAG检索
//                .advisors(QuestionAnswerAdvisor.builder(poetAppVectorStore).build())

                //自定义高级RAG检索
                .advisors(retrievalAugmentationAdvisor)

                //自定义pgvectorStore高级RAG检索
//                .advisors(QuestionAnswerAdvisor.builder(pgVectorStore).build())

                .call()
                .chatResponse();
        String text = null;
        if (chatResponse != null) {
            text = chatResponse.getResult().getOutput().getText();
        }
//        log.info("content: {}",text);
        return text;
    }

    /**
     * 工具调用
     * @param message
     * @param chatId
     * @return
     */
    public String diChatWithTools(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .toolCallbacks(allTools)
                .call()
                .chatResponse();

        String result = chatResponse.getResult().getOutput().getText();
        return result;
    }

    /**
     * MCP工具调用
     * @param message
     * @param chatId
     * @return
     */
    public String diChatWithMCP(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .toolCallbacks(toolCallbackProvider)
                .call()
                .chatResponse();

        String result = chatResponse.getResult().getOutput().getText();
        return result;
    }
}
