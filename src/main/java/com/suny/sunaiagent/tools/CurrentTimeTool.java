package com.suny.sunaiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.util.Date;

@Slf4j
public class CurrentTimeTool {


    @Tool(description = "Get the current system date and time. Useful when the user asks for the current time or date.")
    public String getCurrentTime() {
        log.info("✅ 获取当前系统时间工具调用成功！");
        return new Date().toString();
    }
}
