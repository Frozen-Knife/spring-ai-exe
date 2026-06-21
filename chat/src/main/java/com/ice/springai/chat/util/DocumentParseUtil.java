package com.ice.springai.chat.util;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

public class DocumentParseUtil {

    public static List<Document> parse(String filePath) {
        File file = new File(filePath);
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
        Resource resource = new FileSystemResource(file);
        DocumentReader reader = switch (suffix){
            case "pdf", "doc", "docx", "txt", "text" -> new TikaDocumentReader(resource);
            case "md", "markdown" -> new MarkdownDocumentReader(file.toURI().toString());
            default -> throw new IllegalArgumentException("不支持的文件格式" + suffix);
        };
        return reader.get();
    }
}
