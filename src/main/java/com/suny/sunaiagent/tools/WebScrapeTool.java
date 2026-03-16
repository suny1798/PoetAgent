package com.suny.sunaiagent.tools;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * 网页抓取工具
 */
@Slf4j
public class WebScrapeTool {

    @Tool(description = "Scrape web page")
    public String scrap(@ToolParam(description = "URL of the web page to scrape") String url){
        try {
            Document elements = Jsoup.connect(url).get();
            log.info("✅ 网页爬取工具调用成功！");
            return elements.html();
        } catch (IOException e) {
            return "Error scrape the web page: " + e.getMessage();
        }

    }
}
