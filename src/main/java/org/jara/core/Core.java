package org.jara.core;

import lombok.Getter;
import lombok.Setter;
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

    /*
        Структура для сохранения рассмотренных классов
     */
    private HashMap<String, String> classes;
    /*
        Ошибки при работе
     */
    private String error;

    /*
        Метод запускающий по этапно анализ
     */
    public List<Attentions> scanningStart(Settings settings, String InDir) {
        File file = new File(InDir);
        classes = HashMap.newHashMap(settings.getSize());

        classes = scanDir(file);
        List<Attentions> attentions = analyzeDuplicates();

        return attentions;
    }
    /*
        По этапный анализ только 1 файла
     */
    public List<Attentions> scanningOneFile(Settings settings, String InDir){
        File file = new File(InDir);
        classes = HashMap.newHashMap(settings.getSize());
        List<Attentions> attentions = null;

        if(file.getName().endsWith(".java")){
            readFile(file);
            attentions = analyzeDuplicates();
        }

        return attentions;
    }

    public void readFile(File file){
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            classes.put(file.getName(), content);
        } catch (IOException e) {
            error = e.toString();
            e.printStackTrace();
        }
    }
    /*
        Сканируются директории на наличие java файлов
        для последующего анализа
     */
    public HashMap<String, String> scanDir(File file) {
        File[] files = file.listFiles();

        if (files != null) {
            for (File fileDown : files) {
                if (fileDown.isDirectory()) {
                    scanDir(fileDown);
                } else if (fileDown.getName().endsWith(".java")) {
                    readFile(fileDown);
                }
            }

        }

        return classes;
    }

    /*
     Поработать с настройками посидеть шириной окна (1 не интересный вариант работы)
     Оптимальный по скорости вариант
     */
    public List<Attentions> analyzeDuplicates() {
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

        List<Attentions> attentions = addInEngin(hashToFiles);

        return attentions;
    }

    /*
        Список ошибок заносится в коллекцию
     */
    public List<Attentions> addInEngin(HashMap<String, List<String>> hashToFiles) {
        List<Attentions> attentions = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : hashToFiles.entrySet()) {
            if (entry.getValue().size() > 1) {
                for (String ent : entry.getValue()) {
                    attentions.add(
                            new Attentions(
                                    ent.substring(0, ent.indexOf(':')),
                                    Integer.valueOf(ent.substring(ent.indexOf(':') + 1, ent.length())),
                                    " ",
                                    "Можно попробовать вынести"));
                }
            }
        }

        return attentions;
    }

    /*
        Метод Хэширования подумать над тем как лучше хэшировать
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
            error = e.toString();

            return "";
        }

    }

}
