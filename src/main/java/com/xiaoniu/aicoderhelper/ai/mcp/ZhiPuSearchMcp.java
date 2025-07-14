package com.xiaoniu.aicoderhelper.ai.mcp;


import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 利用mcp协议 调用mcp服务端的智谱搜索工具
 */
@Component
public class ZhiPuSearchMcp {

    @Value("${bigmodel.api-key}")
    public String apiKey;


    @Bean
    public ToolProvider mcpToolProvider() {
        //MCP 传输
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("https://open.bigmodel.cn/api/mcp/web_search/sse?Authorization=" + apiKey)
                .logRequests(true) // 如果你想在日志中查看流量
                .logResponses(true)
                .build();
        //从传输创建 MCP 客户端
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
        //从客户端创建 MCP 工具提供者：
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
        return toolProvider;
    }


}
