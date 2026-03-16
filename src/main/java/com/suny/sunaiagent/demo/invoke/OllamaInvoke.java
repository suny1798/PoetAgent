package com.suny.sunaiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


//@Component
public class OllamaInvoke implements CommandLineRunner {


    @Resource
    private ChatModel ollamaChatModel;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Ollama invoke");
        AssistantMessage assistantMessage = ollamaChatModel.call(new Prompt("你好,我是suny,你是谁,请写1000字的祝福")).getResult().getOutput();
        System.out.println(assistantMessage.getText());
    }
}
