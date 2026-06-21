package com.ice.springai.config;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextSplitterConfigration {

    @Bean
    public TokenTextSplitter  tokenTextSplitter() {
        return TokenTextSplitter.builder()
                .withChunkSize(500)
                .withMinChunkSizeChars(350)
                .withMaxNumChunks(1000)
                .withMinChunkLengthToEmbed(5)
                .withKeepSeparator(true)
                .build();
    }


}
