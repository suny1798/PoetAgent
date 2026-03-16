package com.suny.sunaiagent.agent;


import cn.hutool.core.util.StrUtil;
import com.suny.sunaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public abstract class BaseAgent {

    //核心属性
    private String name;

    //提示词
    private String systemPrompt;
    private String nextStepPrompt;

    //代理状态
    private AgentState state = AgentState.IDLE;

    //执行步骤控制
    private int currentStep = 0;
    private int maxStep = 10;

    //LLM模型
    private ChatClient chatClient;

    //上下文记忆 需要自主维护
    private List<Message> messageList = new ArrayList<>();


    /**
     * 执行代理
     * @param userPrompt
     * @return
     */
    public String run(String userPrompt){
        //基础校验
        if (this.state != AgentState.IDLE){
            throw new RuntimeException("Cannot run agent from state: " + this.state);
        }
        if(StrUtil.isBlank(userPrompt)){
            throw new RuntimeException("Cannot run agent with empty userPrompt ");
        }
        //执行 ，更改状态
        this.state = AgentState.RUNNING;
        //记录上下文信息
        messageList.add(new UserMessage(userPrompt));
        //结果列表
        List<String> resultList = new ArrayList<>();
        //执行步骤
        try {
            for (int i = 0; i < maxStep && state != AgentState.FINISHED; i++) {
                int stepNum = i + 1;
                currentStep = stepNum;
                log.info("Agent step {}/{}", stepNum, maxStep);

                //单步执行
                String stepResult = step();
                String result = "Step " + stepNum + " result: " + stepResult;
                resultList.add(result);
            }
            //检查是否超出最大步骤
            if (currentStep >= maxStep) {
                state = AgentState.FINISHED;
                resultList.add("Agent finished after " + maxStep + " steps.");
            }
            return String.join("\n", resultList);
        }catch (Exception e){
            state = AgentState.ERROR;
            log.error("Agent error: " + e.getMessage(), e);
            return "Agent error: " + e.getMessage();
        }finally {
            //清理资源
            this.cleanup();
        }

    }

    /**
     * 执行单个步骤
     * @return
     */
    public abstract String step();


    /**
     * 清理资源
     */
    protected void cleanup(){
    }
}
