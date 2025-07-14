package com.xiaoniu.aicoderhelper.ai.tools;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 牛站工具调用
 */
@Slf4j
public class NiuZhanTools {


    @Tool(
            name = "牛站视频条目名称获取工具",
            value = """
                    一个用于从牛站平台获取指定视频条目名称的工具。
                    """
    )
    public String getNiuZhanVideos(@P("应返回视频条目名称") String UserMessage) {
        ArrayList<String> videoNameItems = new ArrayList<>();
        String url = "http://bs.mh007.cc/prod-api/baseinfo/fronted/video/list";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                //response.body() 格式 "msg":,"code":,"data":[{},{},{}]
                String jsonData = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                jsonArray.forEach(item -> videoNameItems.add(item.getAsJsonObject().get("title").getAsString().trim()));
            }
        } catch (IOException e) {
            log.warn("牛站视频工具 API 请求出错: " + e.getMessage());
            return e.getMessage();
        }
        return String.join("\n", videoNameItems);
    }
}
