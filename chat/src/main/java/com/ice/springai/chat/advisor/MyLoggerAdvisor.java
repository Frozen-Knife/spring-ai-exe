package com.ice.springai.chat.advisor;

import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MyLoggerAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public @NonNull ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        System.out.println("发送请求前: " + request);
        ChatClientResponse response = chain.nextCall(request);
        System.out.println("接收到响应: " + response);
        return response;
    }

    @Override
    public @NonNull Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        System.out.println("发送流式请求前: " + request);
        return chain.nextStream(request)
                .doOnNext(response -> System.out.println("接收到流式响应: " + response));
    }

    @Override
    public @NonNull String getName() {
        return "MyLoggerAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
