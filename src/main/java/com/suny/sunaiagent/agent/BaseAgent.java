package com.suny.sunaiagent.agent;


import cn.hutool.core.util.StrUtil;
import com.suny.sunaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
     * 执行代理  流式输出 SSE Emitter
     * @param userPrompt
     * @return
     */
    public SseEmitter runStream(String userPrompt){

        //超时时间
        SseEmitter sseEmitter = new SseEmitter(300 * 1000L);  // 5分钟
        //使用线程异步处理
        CompletableFuture.runAsync(() ->{
            //基础校验
            try {
                if (this.state != AgentState.IDLE){
                    sseEmitter.send("错误： 无法从状态运行代理：" + this.state);
                    sseEmitter.complete();
                    return;
                }
                if(StrUtil.isBlank(userPrompt)){
                    sseEmitter.send("错误： 不能使用空提示词运行代理");
                    sseEmitter.complete();
                    return;
                }
            }catch (Exception e){
                sseEmitter.completeWithError(e);
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
                    String result = "Step " + stepNum + "\n result: " + stepResult;

                    //输出每一步的结果 推送SSE 到前端
                    sseEmitter.send(result);
                }
                //检查是否超出最大步骤
                if (currentStep >= maxStep) {
                    state = AgentState.FINISHED;
                    sseEmitter.send("执行结束：达到最大步骤 （"+ maxStep + "）");
                }
                sseEmitter.complete();
            }catch (Exception e){
                state = AgentState.ERROR;
                try {
                    sseEmitter.send("执行错误： " + e.getMessage());
                    sseEmitter.complete();
                } catch (IOException ex) {
                    sseEmitter.completeWithError(ex);
                }
            }finally {
                //清理资源
                this.cleanup();
            }
        });

        //设置超时回调
        sseEmitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timeout");
        });
        sseEmitter.onCompletion(()->{
            if (this.state == AgentState.RUNNING){
                this.state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });
        return sseEmitter;
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
