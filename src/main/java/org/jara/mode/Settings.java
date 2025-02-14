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
        Порог правдоподобия(Симметричности) 1-10
     */
    private float similarityThreshold;
    /*
        Режим работы модуля
     */
    private Mode mode;
    /*
        Величина Стека сбора ошибок
     */
    private final int size;

    public Settings(){
        this.mode = Mode.ReturnList;
        this.deep = 1000;
        this.size = 1000;
        this.similarityThreshold = 10;
    }

    public Settings(Mode mode){
        this.mode = mode;
        this.deep = 1000;
        this.size = 1000;
        this.similarityThreshold = 10;
    }

    public Settings(Mode mode, String outputDir, String inputDir){
        this.mode = mode;
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.deep = 1000;
        this.size = 1000;
        this.similarityThreshold = 10;
    }


}
