package com.ice.springai.chat.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MyEmController {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @RequestMapping("/embedding")
    public String embedding(String text) {
        float[] embedding = embeddingModel.embed(text);
        System.out.println(embedding.length + "====" + Arrays.toString(embedding));
        return Arrays.toString(embedding);
    }

    @RequestMapping("/embedding_distance")
    public String embeddingDistance() {
        String text1 = "我爱吃苹果";
        String text2 = "我爱吃芒果";
        String text3 = "我喜欢游泳";
        String text4 = "我喜欢跑步";
        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);
        float[] embedding3 = embeddingModel.embed(text3);
        float[] embedding4 = embeddingModel.embed(text4);

        double distance12 = enclideanDistance(embedding1, embedding2);
        double distance23 = enclideanDistance(embedding2, embedding3);
        double distance34 = enclideanDistance(embedding3, embedding4);
        double distance14 = enclideanDistance(embedding1, embedding4);
        double distance13 = enclideanDistance(embedding1, embedding3);

        System.out.println("距离12：" + distance12);
        System.out.println("距离23：" + distance23);
        System.out.println("距离34：" + distance34);
        System.out.println("距离14：" + distance14);
        System.out.println("距离13：" + distance13);

        return "OK";
    }

    private static double enclideanDistance(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must have the same length.");
        }
        double distance = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            distance += Math.pow(vectorA[i] - vectorB[i], 2);
        }
        return Math.sqrt(distance);
    }


    @RequestMapping("vector_add")
    public String vectorAdd() {
        List<Document> documents = List.of(
                new Document("洗澡就去龙华洗浴"),
                new Document("泡温泉就去龙碧水蓝天"),
                new Document("吃饭就去楼外楼"),
                new Document("理发就去TC")
                );
        vectorStore.add(documents);
        return "OK";
    }


    @RequestMapping("vector_search")
    public String vectorSearch(String question) {
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(2)
                        .build()
        );
        return documents.toString();
    }

}
