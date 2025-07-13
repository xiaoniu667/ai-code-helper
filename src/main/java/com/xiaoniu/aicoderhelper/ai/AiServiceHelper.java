package com.xiaoniu.aicoderhelper.ai;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

import java.util.List;

public interface AiServiceHelper {

    @SystemMessage(fromResource = "systemPrompt.txt")
    String userChat(String userMessage);

    record Suggestion(String name, List<Suggestion> suggestions){}


    @SystemMessage(fromResource = "systemPrompt.txt")
    Result<Suggestion> extractSuggestionFrom(String userMessage);

}
