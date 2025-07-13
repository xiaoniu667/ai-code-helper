package com.xiaoniu.aicoderhelper;

import com.xiaoniu.aicoderhelper.ai.QwenChat;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCoderHelperApplicationTests {

    @Resource
    private QwenChat qwenChat;

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
        String result = qwenChat.userChartMemory("你好，我是程序员小牛");
        System.out.println(result);
        result = qwenChat.userChartMemory("我是谁?");
        System.out.println(result);
    }

}
