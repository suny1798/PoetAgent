package com.suny.sunaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.suny.sunaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ReActAgent extends BaseAgent{

    /**
     * 这是一个抽象方法，用于表示思考行为
     * 该方法需要在子类中实现具体逻辑
     *
     * @return 返回一个布尔值，表示思考的结果执行或不执行
     */
    public abstract boolean think();

    /**
     * 这是一个抽象方法，由子类实现具体行为
     * 该方法没有参数，返回一个字符串类型的执行结果
     *
     * @return 执行后返回的字符串
     */
    public abstract String act();

    /**
     * 这是一个重写的方法，用于实现ReActAgent的行为
     * @return
     */
    @Override
    public String step() {
        try {
            //先思考
            boolean shouldAct = think();
            if (!shouldAct) {
                setState(AgentState.FINISHED);
                String string = this.getMessageList().getLast().getText();
                return string;
            }
            //再执行
            return act();
        } catch (Exception e) {
            e.printStackTrace();
            return "思考或执行失败" + e.getMessage();
        }

    }
}
