package com.ice.chat.controller;

import com.ice.chat.record.BookReview;
import com.ice.chat.record.TopicBooks;
import com.ice.chat.tool.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class MyAIChatController {

    @Autowired
    private ChatClient openAIChatClient;

    @Autowired
    private ChatClient ollamaChatClient;

    @Autowired
    private ChatClient javaChatClient;

    @Autowired
    private DateTimeTools dateTimeTools;

    @RequestMapping("/openai")
    public String openai(String question) {
        return openAIChatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    @RequestMapping(value = "/openai-flux", produces = "text/html;charset=UTF-8")
    public Flux<String> openaiFlux(String question) {
        return openAIChatClient.prompt()
                .user(question)
                .stream()
                .content();
    }


    @RequestMapping("/ollama")
    public String ollama(String question) {
        return ollamaChatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    @RequestMapping(value = "/ollama-flux", produces = "text/html;charset=UTF-8")
    public Flux<String> ollamaFlux(String question) {
        return ollamaChatClient.prompt()
                .user(question)
                .stream()
                .content();
    }

    @RequestMapping("/introduce")
    public String introduce(String topic) {
        PromptTemplate template = new PromptTemplate("介绍一下{topic}");
        Prompt prompt = template.create(Map.of("topic", topic));
        return openAIChatClient.prompt(prompt)
                .call()
                .content();
    }

    @RequestMapping(value = "/introduce-flux", produces = "text/html;charset=UTF-8")
    public Flux<String> introduceFlux(String topic) {
        return openAIChatClient.prompt()
                .system("你是一个专业的书评助手")
                .user(u -> u.text("请给我三本关于{topic}的书籍").param("topic", topic))
                .stream()
                .content();
    }

    @RequestMapping("/introduce-system")
    public String introduceSystem(String question) {
        return javaChatClient.prompt()
                .user(question)
                .call()
                .content();
    }


    @RequestMapping("/introduce-books")
    public String introduceBooks(String topic) {
        TopicBooks books = openAIChatClient.prompt()
                .system("你是一个专业的书评助手")
                .user(u -> u.text("请给我三本关于{topic}的书籍，只提供书名即可").param("topic", topic))
                .call()
                .entity(TopicBooks.class);
        return books.toString();
    }

    @RequestMapping("/introduce-review")
    public String introduceReview(String bookName) {
        List<BookReview> reviews = openAIChatClient.prompt()
                .user(u -> u.text("请给我三条关于{bookName}书籍的评价,50字以内").param("bookName", bookName))
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
        return reviews.toString();
    }

    @RequestMapping("/openai_with_memory")
    public String openai(String question, int convId) {
        return openAIChatClient.prompt()
                .user(question)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, convId))
                .call()
                .content();
    }

    @RequestMapping("/time_tool")
    public String timeTool(String question) {
        return openAIChatClient.prompt()
                .user(question)
                .tools(dateTimeTools)
                .call()
                .content();
    }
}
