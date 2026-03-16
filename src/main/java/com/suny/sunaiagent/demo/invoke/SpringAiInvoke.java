package com.suny.sunaiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * Spring AI调用类，实现CommandLineRunner接口，在Spring Boot应用启动后执行特定逻辑
 */
//@Component
public class SpringAiInvoke implements CommandLineRunner {

    /**
     * 注入ChatModel类型的bean，用于与AI模型进行交互
     */
    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 实现CommandLineRunner接口的run方法，在Spring Boot应用启动后自动执行
     * @param args 命令行参数
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void run(String... args) throws Exception {
        // 创建提示词并发送给AI模型，获取AI助手的回复
        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("你好,我是suny")).getResult().getOutput();
        // 打印AI助手的回复内容
        System.out.println(assistantMessage.getText());
    }
}
