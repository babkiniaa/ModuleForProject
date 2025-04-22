package org.jara.core;

import lombok.Getter;
import lombok.Setter;
import org.jara.mode.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private Settings settings;

    private ASTAnalysis astAnalysis;

    private HashAnalysis hashAnalysis;

    private EncapsulationAnalysis encapsulationAnalysis;

    public Core(Settings settings) {
        this.settings = settings;
        astAnalysis = new ASTAnalysis();
        hashAnalysis = new HashAnalysis();
        encapsulationAnalysis = new EncapsulationAnalysis();
    }

    /*
        В зависимости от выбранного режима выбор анализа и отчета
     */
    public List<Attentions> selectMode() {
        List<Attentions> attentions = new ArrayList<>();

        switch (settings.getMode()) {
            case ReturnListHash:
                attentions = hashAnalysis.startAnalysis(settings, classes);
                break;
            case WriteHash:
                attentions = hashAnalysis.startAnalysis(settings, classes);
                break;
            case ReturnListAST:
                attentions = astAnalysis.startAnalysis(classes);
                break;
            case WriteAST:
                attentions = astAnalysis.startAnalysis(classes);
                break;
            case WriteEncapsulation:
                attentions = encapsulationAnalysis.startAnalysis(classes);
                break;
            case ReturnEncapsulation:
                attentions = encapsulationAnalysis.startAnalysis(classes);
                break;
        }

        return attentions;
    }

    /*
        Метод запускающий по этапно анализ с выбором анализа (AST или метод ХЭШ)
     */
    public List<Attentions> scanningStart() {
        File file = new File(settings.getInputDir());
        classes = new HashMap<>();
        List<Attentions> attentions;

        classes = scanDir(file);
        attentions = selectMode();

        return attentions;
    }

    /*
        По этапный анализ только 1 файла
     */
    public List<Attentions> scanningOneFile() {
        File file = new File(settings.getInputDir());
        classes = new HashMap<>();
        List<Attentions> attentions = null;

        if (file.getName().endsWith(".java")) {
            readFile(file);
            attentions = selectMode();
        }

        return attentions;
    }

    /*
        Чтение и занесение данных из файла
     */
    public void readFile(File file) {
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

}
