package com.ice.chat.controller;

import com.ice.chat.util.DocumentParseUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyRagController {

    @Autowired
    private TokenTextSplitter splitter;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatClient openAIChatClient;

    @RequestMapping("/addFile")
    public String addFile() {
        addChunckedDocuments("/Users/lihe/Documents/code/bl-exe/springai/upload/test.txt", "test.txt");
        addChunckedDocuments("/Users/lihe/Documents/code/bl-exe/springai/upload/【动手实操】12.12 AI 原生应用开源开发者沙龙-杭州站.docx", "【动手实操】12.12 AI 原生应用开源开发者沙龙-杭州站.docx");
        addChunckedDocuments("/Users/lihe/Documents/code/bl-exe/springai/upload/突击班-架构师基础.md", "突击班-架构师基础.md");
        addChunckedDocuments("/Users/lihe/Documents/code/bl-exe/springai/upload/程序员面试宝典-阿里出品.pdf", "程序员面试宝典-阿里出品.pdf");
        return "OK";
    }

    private void addChunckedDocuments(String filePath, String label) {
        List<Document> raw = DocumentParseUtil.parse(filePath);
        List<Document> chunks = splitter.apply(raw);

        int batchSize = 10;
        for (int i = 0; i < chunks.size(); i += batchSize) {
            int end = Math.min(i + batchSize, chunks.size());
            List<Document> batch = chunks.subList(i, end);
            vectorStore.add(batch);
        }

        System.out.println(label + "向量存储添加成功 共 " + chunks.size() + " 块");
    }


    @RequestMapping("search_file")
    public String searchFile(String question) {
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(3)
                        .build()
        );
        return documents.toString();
    }

    @RequestMapping("/rag")
    public String rag(String question) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.7)
                        .topK(3)
                        .build())
                .build();
        String result = openAIChatClient.prompt()
                .advisors(advisor)
                .user(question)
                .call()
                .content();
        return result;
    }
}
