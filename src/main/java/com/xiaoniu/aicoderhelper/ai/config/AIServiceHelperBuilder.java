package com.xiaoniu.aicoderhelper.ai.config;


import com.xiaoniu.aicoderhelper.ai.AiServiceHelper;
import com.xiaoniu.aicoderhelper.ai.tools.NiuZhanTools;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 采用声明式的方式构造ai服务对象，代码更加简洁。
 * AI 服务处理最常见的操作：为 LLM 格式化输入 解析 LLM 的输出
 * 它们还支持更高级的功能：
 * 1.聊天记忆
 * 2.工具
 * 3.RAG
 */
@Configuration
public class AIServiceHelperBuilder {

    @Resource
    private ChatModel qwenChatModel;

    @Resource
    private ChatModel myQwenChatModel;

    @Resource
    private StreamingChatModel qwenStreamingChatModel;

    @Resource
    private ToolProvider mcpToolProvider;

    @Resource
    private EmbeddingModel qwenEmbeddingModel;

    @Bean
    public AiServiceHelper aiServiceHelper() {
        //1.读取数据库文件
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/database");
        //2.将文件内容分片，并且转为embedding的向量
        //300：表示每个文档片段的最大长度（以字符或标记为单位，具体取决于实现）。
        //0：表示片段之间的重叠长度（overlap），这里设置为0，意味着片段之间没有重叠。
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        //分段切割
        DocumentByParagraphSplitter documentByParagraphSplitter = new DocumentByParagraphSplitter(300,100);
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByParagraphSplitter)
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(documents);
        //3.声明一个ContentRetriever，指定检索的个数，以及相似度阈值,构造rag
        //检索的个数，默认为5
        //相似度阈值，默认为0.6
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(qwenEmbeddingModel)
                .maxResults(5)
                .minScore(0.6)
                .build();
        //目的是提高检索质量，避免冗长的对话历史干扰语义检索，优化用户对于检索数据的搜索
        QueryTransformer queryTransformer = new CompressingQueryTransformer(qwenChatModel);

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .contentRetriever(contentRetriever)
                .build();

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(AiServiceHelper.class)
                .chatMemory(chatMemory)   //回话记忆
                .chatModel(myQwenChatModel)
                .streamingChatModel(qwenStreamingChatModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.withMaxMessages(10)) // 每个会话独立存储
                .retrievalAugmentor(retrievalAugmentor) //rag
                .tools(new NiuZhanTools()) //工具调用
                .toolProvider(mcpToolProvider) //mcp调用
                .build();
    }

}
