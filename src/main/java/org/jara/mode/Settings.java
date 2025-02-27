package org.jara.mode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Settings {
    /*
        Директория в которую будет создаваться отчет
     */
    private String outputDir;
    /*
        Директория, которую будет обследовать анализатор
     */
    private String inputDir;
    /*
        Глубина погружения, может влиять на скорость работы.
     */
    private int deep;
    /*
        Режим работы модуля
     */
    private Mode mode;
    /*
        Величина Стека сбора ошибок
     */
    private final int size;
    /*
        Параметр отвечающий за величину окна в режиме ХЭШ
     */
    private final int windowSize;

    public Settings(){
        this.mode = Mode.ReturnListHash;
        this.deep = 1000;
        this.size = 1000;
        this.windowSize = 5;
    }

    public Settings(Mode mode){
        this.mode = mode;
        this.deep = 1000;
        this.size = 1000;
        this.windowSize = 5;
    }

    public Settings(Mode mode, String outputDir, String inputDir){
        this.mode = mode;
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.deep = 1000;
        this.size = 1000;
        this.windowSize = 5;
    }


}
