package com.xiaoniu.aicoderhelper.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AiServiceHelper {

    @SystemMessage(fromResource = "systemPrompt.txt")
    String userChat(String userMessage);

    record Suggestion(String name, List<Suggestion> suggestions){}


    @SystemMessage(fromResource = "systemPrompt.txt")
    Result<Suggestion> extractSuggestionFrom(String userMessage);


    // 流式对话
    @SystemMessage(fromResource = "systemPrompt.txt")
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}
