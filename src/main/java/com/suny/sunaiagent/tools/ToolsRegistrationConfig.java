package com.suny.sunaiagent.tools;


import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsRegistrationConfig {

    @Value("${search-api.api-key}")
    private String searchAPI;

    @Value("${qq.auth_code}")
    private String qq_auth_code;

    @Value("${qq.from_mail}")
    private String from_mail;


    @Bean
    public ToolCallback[] allTools(){
        FileOperationTool fileOperationTool = new FileOperationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalTool terminalTool = new TerminalTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchAPI);
        WebScrapeTool webScrapeTool = new WebScrapeTool();
        QQMailTool qqMailTool = new QQMailTool(from_mail, qq_auth_code);
        CurrentTimeTool currentTimeTool = new CurrentTimeTool();
        TerminateTool terminateTool = new TerminateTool();
        return ToolCallbacks.from(
                fileOperationTool,
                resourceDownloadTool,
                terminalTool,
                webSearchTool,
                webScrapeTool,
                qqMailTool,
                currentTimeTool,
                terminateTool
        );
    }
}
