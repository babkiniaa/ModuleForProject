package org.jara.core;

import org.jara.engine.Engine;
import org.jara.mode.Mode;
import org.jara.mode.Settings;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Attentions> attentions;
        String path = "C:/Users/Ярик/Desktop/module";
        Mode mode = Mode.ReturnListAST;
        Settings config = new Settings();
        config.setInputDir(path);
        if (mode != null) {
            config.setMode(mode);
        }
        Engine engine = new Engine(config);

        engine.scan();
        attentions = engine.getAttentions();
        for(int i=0; i< attentions.size(); i++){
            System.out.println(attentions.get(i).getNameFile());
            System.out.println(attentions.get(i).getLine());
        }
    }
}
