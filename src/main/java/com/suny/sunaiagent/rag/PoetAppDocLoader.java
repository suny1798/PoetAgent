package com.suny.sunaiagent.rag;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
/**
 * 读取文本文件
 */
public class PoetAppDocLoader {

    private final ChatModel dashcopeChatModel;

    private final ResourcePatternResolver resourcePatternResolver;

    PoetAppDocLoader(ChatModel dashcopeChatModel, ResourcePatternResolver resourcePatternResolver) {
        this.dashcopeChatModel = dashcopeChatModel;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 读取文本文件
     * @return
     */
    List<Document> loadText() {
        Resource resource = resourcePatternResolver.getResource("classpath:doc/poem.txt");
        log.info("Loading text from {}", resource.getFilename());
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("filename", resource.getFilename());
        textReader.getCustomMetadata().put("status","情感");
        List<Document> documents = textReader.read();
        List<Document> splitCustomized = splitCustomized(documents);
        List<Document> enrichedDocuments = enrichDocuments(splitCustomized);
        return enrichedDocuments;
    }

    /**
     * 自定义切割文本
     * @param documents
     * @return
     */
    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(100, 20, 10, 5000, true);
        return splitter.apply(documents);
    }

    /**
     * 增加关键词 到meta信息中
     * @param documents
     * @return
     */
    public  List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(dashcopeChatModel)
                .keywordCount(2)
                .build();

        return enricher.apply(documents);
    }
}