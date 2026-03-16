package com.suny.sunaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.suny.sunaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理工具调用 具体数实现了think 和act方法
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent{

    //可用工具列表
    private final ToolCallback[] availTools;

    //保存工具调用信息返回结果 ---- 要调用的实际工具
    private ChatResponse toolCallChatResponse;

    //工具调用管理者
    private final ToolCallingManager toolCallingManager;

    //禁用Spring AI 内部的工具调用机制，自己维护工具调用
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availTools) {
        super();
        this.availTools = availTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                .withInternalToolExecutionEnabled(false)
                .build();
    }

    @Override
    public boolean think() {
        //校验提示词，拼接用户提示词
        if(StrUtil.isNotBlank(getNextStepPrompt())){
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }
        List<Message> messageList = getMessageList();
        Prompt prompt = new Prompt(messageList, chatOptions);
        //调用AI大模型 获取工具调用结果
        try {
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .toolCallbacks(availTools)
                    .call()
                    .chatResponse();

            //记录相应，用于act
            this.toolCallChatResponse = chatResponse;

            // 解析工具调用结果，获取要调用的工具
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            String result = assistantMessage.getText();

            //处理结果
            log.info(getName() + "的思考结果：" + result);
            log.info(getName() + "的调用工具列表：" + toolCallList);

            //工具调用列表为空，则直接返回结果
            if (toolCallList.isEmpty()) {
                getMessageList().add(assistantMessage);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.info(getName() + "调用大模型失败：" + e.getMessage());
            getMessageList().add(new AssistantMessage("处理时遇到错误" + e.getMessage()));
            return false;
        }

    }

    /**
     * 执行工具并返回结果
     * @return
     */
    @Override
    public String act() {
        if (!toolCallChatResponse.hasToolCalls()) {
            return "没有工具需要调用";
        }
        //调用工具  手动
        Prompt prompt = new Prompt(getMessageList(), this.chatOptions);
        ToolExecutionResult result = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);
        //记录工具调用结果
        setMessageList(result.conversationHistory());
        log.info(getName() + "调用工具结果：" + result);
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(result.conversationHistory());
        //判断是否调用了终止工具
        boolean doTerminate = toolResponseMessage.getResponses().stream()
                .anyMatch(response -> response.name().equals("doTerminate"));
        if (doTerminate) {
            //调用终止工具，则结束
            setState(AgentState.FINISHED);
        }
        //获取最后工具执行结果
        String results = toolResponseMessage.getResponses().stream()
                .map(response -> response.name() + "返回的结果"+ response.responseData())
                .collect(Collectors.joining("\n"));

        log.info(results);
        return results;
    }
}