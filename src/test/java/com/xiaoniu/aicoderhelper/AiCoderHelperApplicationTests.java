package com.xiaoniu.aicoderhelper;

import com.xiaoniu.aicoderhelper.ai.AiServiceHelper;
import com.xiaoniu.aicoderhelper.ai.QwenChat;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCoderHelperApplicationTests {

    @Resource
    private QwenChat qwenChat;

    @Resource
    private AiServiceHelper aiServiceHelper;

    @Test
    void userChat() {
        String result = qwenChat.userChart("你好，我是小牛");
        System.out.println(result);
    }

    @Test
    void userChatByImage() {
        String result = qwenChat.userChart("你好，请问图片中显示的是什么");
        System.out.println(result);
    }

    @Test
    void userChartMemory() {
        String result = aiServiceHelper.userChat("你好，你是谁？我是小牛");
        System.out.println(result);
        result = aiServiceHelper.userChat("我是谁来着？");
        System.out.println(result);
    }


    @Test
    void extractSuggestionFrom() {
        Result<AiServiceHelper.Suggestion> result = aiServiceHelper.extractSuggestionFrom("你好，给我一点编程学习的建议");
        System.out.println(result.content());
    }

    @Test
    void userChatWithRag() {
        String result = aiServiceHelper.userChat("你好，牛站助手，请给我提供一些编程学习建议");
        System.out.println(result);
    }


}
