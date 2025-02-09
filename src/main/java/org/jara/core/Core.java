package org.jara.core;

import lombok.Getter;
import lombok.Setter;
import org.jara.engine.Engine;
import org.jara.mode.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@Setter
@Getter
public class Core {


    public Engine scanningStart(Engine engine) {
        Settings settings = engine.getSettings();
        Attentions[] attentions = new Attentions[settings.getSize()];
        HashMap<String, String> classes = HashMap.newHashMap(settings.getSize());
        String dir = settings.getInputDir();
        File file = new File(dir);
        File[] files = file.listFiles();

        if (files != null) {
            for (File fileDown : files) {
                if (fileDown.isDirectory()) {
                    scanDir(fileDown, classes);
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

        return engine;
    }

    public void scanDir(File file, HashMap<String, String> classes) {
        File[] files = file.listFiles();

        if (files != null) {
            for (File fileDown : files) {
                if (fileDown.isDirectory()) {
                    scanDir(fileDown, classes);
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
    }

}
