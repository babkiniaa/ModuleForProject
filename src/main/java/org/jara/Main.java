package org.jara;

import org.jara.engine.Engine;
import org.jara.mode.Settings;

public class Main {

    public static Settings settings = new Settings();
    public static Engine engine = new Engine(settings);

    public static void main(String[] args){


//        settings.setInputDir("C:/Users/Ярик/Desktop/module");
        settings.setInputDir("J:/module/src/main/java/org/jara/mode/Settings.java");
        Engine engine = new Engine(settings);
        engine.scan();


    }

}
