package org.jara.core;

import org.jara.mode.Settings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class HashAnalysis {

    private final MessageDigest digest;
    private final CodeNormalizer normalizer;

    public HashAnalysis() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
            this.normalizer = new CodeNormalizer();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public List<Attentions> startAnalysis(Settings settings, Map<String, String> classes) {
        int windowSize = settings.getWindowSize();
        Map<String, List<CodeLocation>> hashToLocations = new HashMap<>();

        classes.entrySet().parallelStream().forEach(entry -> {
            String fileName = entry.getKey();
            String normalizedContent = normalizer.normalize(entry.getValue());
            List<String> lines = Arrays.asList(normalizedContent.split("\n"));

            for (int i = 0; i <= lines.size() - windowSize; i++) {
                String block = String.join("\n", lines.subList(i, i + windowSize));
                String hash = hash(block);

                synchronized (hashToLocations) {
                    hashToLocations.computeIfAbsent(hash, k -> new ArrayList<>())
                            .add(new CodeLocation(fileName, i + 1, block));
                }
            }
        });

        return createAttentions(hashToLocations, settings.getMinDuplicateCount());
    }

    private String hash(String block) {
        byte[] hashBytes = digest.digest(block.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(hashBytes.length * 2);

        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    private List<Attentions> createAttentions(Map<String, List<CodeLocation>> hashToLocations, int minDuplicateCount) {
        return hashToLocations.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= minDuplicateCount)
                .flatMap(entry -> entry.getValue().stream()
                        .map(loc -> new Attentions(
                                loc.fileName,
                                loc.lineNumber,
                                loc.codeBlock,
                                String.format("Дублирующийся код найден в %d местах", entry.getValue().size())
                        )))
                .collect(Collectors.toList());
    }

    private static class CodeLocation {
        final String fileName;
        final int lineNumber;
        final String codeBlock;

        CodeLocation(String fileName, int lineNumber, String codeBlock) {
            this.fileName = fileName;
            this.lineNumber = lineNumber;
            this.codeBlock = codeBlock;
        }
    }

    private static class CodeNormalizer {
        String normalize(String code) {
            return code
                    .replaceAll("//.*|/\\*.*?\\*/", "")
                    .replaceAll("\\s+", " ")
                    .replaceAll("[{};]", "")
                    .toLowerCase()
                    .trim();
        }
    }

}
