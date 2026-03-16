package com.suny.sunaiagent.rag;

import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PoetAppVectorStoreConfig {

    @Resource
    private PoetAppDocLoader poetAppDocLoader;

    /**
     * 创建一个名为poetAppVectorStore的Bean，用于存储诗人应用相关的向量数据
     *
     * @param dashscopeEmbeddingModel 阿里云Dashscope的嵌入模型，用于文本向量化
     * @return 返回配置好的SimpleVectorStore实例，已加载诗人应用文档数据
     */
    @Bean
    VectorStore poetAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        // 创建一个简单的向量存储实例，使用提供的嵌入模型进行初始化
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 从文档加载器中加载文本文档
        List<Document> documentList = poetAppDocLoader.loadText();

        //自定义高级查询
//        SearchRequest searchRequest = SearchRequest.builder()
//                .query("思家")
//                .topK(5)
//                .similarityThreshold(0.5)
//                .build();
//        List<Document> documentList1 = simpleVectorStore.similaritySearch(searchRequest);

        // 将加载的文档添加到向量存储中
        simpleVectorStore.add(documentList);

        // 返回配置完成的向量存储实例
        return simpleVectorStore;
    }

}
