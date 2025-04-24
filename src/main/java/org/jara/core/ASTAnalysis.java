package org.jara.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ASTAnalysis {

    private final MessageDigest digest;

    public ASTAnalysis() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 недоступен", e);
        }
    }

    public List<Attentions> startAnalysis(Map<String, String> classes) {
        Map<String, List<MethodInfo>> hashToMethods = new HashMap<>();

        JavaParser javaParser = new JavaParser();

        for (Map.Entry<String, String> entry : classes.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();

            try {
                ParseResult<CompilationUnit> parseResult = javaParser.parse(content);

                if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                    CompilationUnit cu = parseResult.getResult().get();
                    cu.findAll(MethodDeclaration.class).forEach(md -> {
                        String methodBody = md.getBody().map(Object::toString).orElse("");
                        String hash = hash(methodBody);
                        int line = md.getRange().map(r -> r.begin.line).orElse(-1);

                        hashToMethods.computeIfAbsent(hash, k -> new ArrayList<>())
                                .add(new MethodInfo(fileName, line, md.toString()));
                    });
                } else {
                    System.out.printf("Не удалось распарсить файл: %s%n", fileName);
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка в AST при анализе файла: " + fileName, e);
            }
        }

        return hashToMethods.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .flatMap(entry -> entry.getValue().stream()
                        .map(info -> new Attentions(
                                info.fileName,
                                info.lineNumber,
                                info.code,
                                String.format("Метод дублируется в %d местах", entry.getValue().size())
                        )))
                .collect(Collectors.toList());
    }

    private String hash(String text) {
        byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(hashBytes.length * 2);
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    private static class MethodInfo {
        final String fileName;
        final int lineNumber;
        final String code;

        MethodInfo(String fileName, int lineNumber, String code) {
            this.fileName = fileName;
            this.lineNumber = lineNumber;
            this.code = code;
        }
    }
}