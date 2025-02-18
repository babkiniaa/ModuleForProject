package org.jara;

import org.jara.core.Core;
import org.jara.engine.Engine;
import org.jara.mode.Settings;


public class Main {

    public static void main(String[] args){
        Settings settings = new Settings();
        //settings.setInputDir("C:/Users/Ярик/Desktop/module");
        settings.setInputDir("J:/module/src/main/java/org/jara/mode/Settings.java");
        Engine engine = new Engine(settings);
        engine.scanOneFile();
        System.out.println(engine.getAttentions());


//        String filePath = "path/to/YourClass.java";
//        CompilationUnit cu = JavaParser.parse(new FileInputStream(filePath));
//        cu.accept(new Core.VariableNormalizer(), null);
//        findAndPrintDuplicateMethods(cu, filePath);
    }
}
