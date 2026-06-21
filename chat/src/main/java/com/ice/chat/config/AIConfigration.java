package com.ice.chat.config;

import com.ice.chat.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfigration {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean
    public ChatClient openAIChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultAdvisors(new SimpleLoggerAdvisor()
//                        , MessageChatMemoryAdvisor.builder(chatMemory).build()
                ).build();
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();
    }

    @Bean
    public ChatClient javaChatClient(OpenAiChatModel model) {
        String systemPrompt = """
                你是一个资深的Java顾问。
                禁止回答任何非技术问题，例如天气 娱乐和八卦。
                代码示例必须符合 Java 17+ 规范。
                回答需要符合以下格式：首先一句话概括问题核心，然后提供代码示例，最后补充注意事项。
                如果自己不确定可以说"关于这个问题，我目前没有明确的信息"，禁止编造内容。
                """;
        return ChatClient.builder(model)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

}
