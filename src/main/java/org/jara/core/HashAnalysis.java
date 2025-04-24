package org.jara.core;

import org.jara.mode.Settings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HashAnalysis {

    private final CodeNormalizer normalizer;

    public HashAnalysis() {
        this.normalizer = new CodeNormalizer();
    }

    public List<Attentions> startAnalysis(Settings settings, Map<String, String> classes) {
        int windowSize = settings.getWindowSize();
        int minDuplicateCount = settings.getMinDuplicateCount();

        Map<String, List<Attentions>> hashToLocations = new ConcurrentHashMap<>();

        classes.entrySet().parallelStream().forEach(entry -> {
            String fileName = entry.getKey();
            String content = entry.getValue();
            String normalizedContent = normalizer.normalize(content);
            List<String> lines = Arrays.asList(normalizedContent.split("\n"));

            for (int i = 0; i <= lines.size() - windowSize; i++) {
                String block = String.join("\n", lines.subList(i, i + windowSize));
                String hash = hash(block);

                hashToLocations
                        .computeIfAbsent(hash, k -> Collections.synchronizedList(new ArrayList<>()))
                        .add(new Attentions(fileName, i + 1, block, " "));
            }
        });

        return createAttentions(hashToLocations, minDuplicateCount);
    }

    private String hash(String block) {
        try {
            MessageDigest localDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = localDigest.digest(block.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(hashBytes.length * 2);

            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private List<Attentions> createAttentions(Map<String, List<Attentions>> hashToLocations, int minDuplicateCount) {
        return hashToLocations.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= minDuplicateCount)
                .flatMap(entry -> entry.getValue().stream()
                        .map(loc -> new Attentions(
                                loc.getNameFile(),
                                loc.getLine(),
                                loc.getCode(),
                                String.format("Дублирующийся код найден в %d местах", entry.getValue().size())
                        )))
                .collect(Collectors.toList());
    }

    private static class CodeNormalizer {
        String normalize(String code) {
            code = code.replaceAll("(?s)/\\*.*?\\*/", "");
            code = code.replaceAll("[ \\t]*\\n", "\n");

            List<String> lines = Arrays.stream(code.split("\n"))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());

            return String.join("\n", lines);
        }
    }

}
