package com.xiaoniu.aicoderhelper.ai;


import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QwenChat {

    @Resource
    private ChatLanguageModel qwenChatModel;

    public String userChart(String message) {
        String result = qwenChatModel.chat(message);
        log.info("模型输出的内容为:" + result);
        return result;
    }

    public String userImage(String message) {
        UserMessage userMessage = UserMessage.from(
                TextContent.from(message),
                ImageContent.from("https://xiaoniu-test.oss-cn-" +
                        "hangzhou.aliyuncs.com/picture/01d68e8392b1f753994bd56cb4f015a.jpg"));
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("模型输入的内容为:" + aiMessage.text());
        return aiMessage.text();
    }

    //TODO
    public String userChartMemory(String message) {
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(5);
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(qwenChatModel)
                .chatMemory(messageWindowChatMemory)
                .build();
        return chain.execute(message);
    }

}
