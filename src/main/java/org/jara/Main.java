package org.jara;

import org.jara.engine.Engine;
import org.jara.mode.Settings;

public class Main {

    public static void main(String[] args){
        Settings settings = new Settings();
        settings.setInputDir("C:/Users/Ярик/Desktop/module");
        Engine engine = new Engine(settings);
        engine.scan();
    }
}
