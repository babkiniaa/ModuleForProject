package org.jara.core;

import lombok.Getter;
import lombok.Setter;
import org.jara.engine.Engine;
import org.jara.mode.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Setter
@Getter
public class Core {

    private HashMap<String, String> classes;

    public Engine scanningStart(Engine engine) {
        Settings settings = engine.getSettings();
        String dir = settings.getInputDir();
        File file = new File(dir);
        classes = HashMap.newHashMap(settings.getSize());

        classes = scanDir(file);

        return engine;
    }

    public HashMap<String, String> scanDir(File file) {
        File[] files = file.listFiles();

        if (files != null) {
            for (File fileDown : files) {
                if (fileDown.isDirectory()) {
                    scanDir(fileDown);
                } else if (fileDown.getName().endsWith(".java")) {
                    try {
                        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                        classes.put(file.getName(), content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return classes;
    }
    /*
     Поработать с настройками посидеть шириной окна (1 не интересный вариант работы)
     */
    public Engine analyzeDuplicates(Engine engine) {
        int windowSize = 5;
        HashMap<String, List<String>> hashToFiles = new HashMap<>();

        for (Map.Entry<String, String> entry : classes.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            List<String> lines = Arrays.asList(content.split("\n"));

            for (int i = 0; i <= lines.size() - windowSize; i++) {
                String block = String.join("\n", lines.subList(i, i + windowSize));
                String hash = hash(block);

                hashToFiles.putIfAbsent(hash, new ArrayList<>());
                hashToFiles.get(hash).add(fileName + ":" + (i + 1));
            }
        }

        for (Map.Entry<String, List<String>> entry : hashToFiles.entrySet()) {
            if (entry.getValue().size() > 1) {
                /*
                Надо придумать сущность в начале потом уже будет заносить ее нормально
                 */
                engine.setAttentions(new  Attentions[1]);
                System.out.println("Дубликат найден в: " + entry.getValue());
            }
        }
        return engine;
    }

    /*
        Метод Хэширования подумать над тем как лучше хешировать
        И вынос ошибки от сюда
     */
    private String hash(String block) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(block.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хеширования", e);
        }
    }

}
