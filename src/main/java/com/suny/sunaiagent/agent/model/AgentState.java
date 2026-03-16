package com.suny.sunaiagent.agent.model;

/**
 * Agent状态枚举
 * 用于表示Agent在不同阶段的状态
 */
public enum AgentState {

    /**
     * 空闲状态
     * Agent处于等待状态，尚未开始执行任务
     */
    IDLE,

    /**
     * 运行状态
     * Agent正在执行任务
     */
    RUNNING,

    /**
     * 完成状态
     * Agent已完成任务执行
     */
    FINISHED,

    /**
     * 错误状态
     * Agent在执行过程中发生错误
     */
    ERROR
}
