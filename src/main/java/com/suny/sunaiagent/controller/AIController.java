package com.suny.sunaiagent.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.suny.sunaiagent.agent.PoetManus;
import com.suny.sunaiagent.app.PoetApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AIController {
    @Resource
    private PoetApp poetApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;


    /**
     * AI 基础对话（支持多轮对话）同步方法
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/poet_app/chat/sync")
    public String doChatWithPoetAppSync(String message, String chatId) {
        //可以自选多种对话方式，这里选取简单对话
        return poetApp.doChat(message, chatId);
    }

    /**
     * AI 基础对话（支持多轮对话）SSE方法  s
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/poet_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithPoetAppSSE(String message, String chatId) {
        //可以自选多种对话方式，这里选取简单对话
        return poetApp.doChatByStream(message, chatId);
    }

    /**
     * 流式调用超级智能体
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        PoetManus poetManus = new PoetManus(allTools, dashscopeChatModel);
        return poetManus.runStream(message);
    }
}
