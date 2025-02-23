package org.jara;

import org.jara.core.Attentions;
import org.jara.engine.Engine;
import org.jara.mode.Settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;


public class Main {

    public static void main(String[] args){
        Settings settings = new Settings();

        settings.setInputDir("C:/Users/Ярик/Desktop/module");
        settings.setInputDir("J:/module/src/main/java/org/jara/mode/Settings.java");
        Engine engine = new Engine(settings);
        engine.scanOneFile();



    }
}
