package com.ice.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyMcpController {

    @Autowired
    private ChatClient openAIChatClient;

    @Autowired
    private SyncMcpToolCallbackProvider mcpToolCallbackProvider;


    @RequestMapping("/ai_mcp")
    public String aiWithMcp(){
        String question = "查询一下杭州天气";
        String result = openAIChatClient.prompt()
                .system("用户询问天气时，你必须调用工具查询后再用简短中文回答。")
                .user(question)
                .tools(mcpToolCallbackProvider)
                .call()
                .content();
        System.out.println(result);
        return result;
    }




}
