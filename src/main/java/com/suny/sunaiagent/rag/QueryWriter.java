package com.suny.sunaiagent.rag;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;


/**
 * 查询重写器
 */
@Component
public class QueryWriter {

    private final QueryTransformer queryTransformer;

    public QueryWriter(ChatModel dashcopeChatModel) {
        ChatClient.Builder builder = ChatClient.builder(dashcopeChatModel);
        queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    public String writeQuery(String prompt) {
        Query query = new Query(prompt);
        return queryTransformer.transform(query).text();
     }
}
