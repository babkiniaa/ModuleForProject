package org.jara.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jara.core.Attentions;
import org.jara.mode.Mode;
import org.jara.mode.Setings;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class Engine {
    /*
        Класс определяющий настройки модуля
     */
    private final Setings setings;
    /*
        Класс определяющий режим работы модуля
        либо для анализа, либо для советов
     */
    private final Mode mode;
    /*
        Список мест, к которым надо обратить внимание
     */
    private Attentions[] attentions;
    /*
        Ошибка в работе, если таковая имеется
     */
    private String error;
    /*
        Метод запускающий сканирование
     */
    public Engine(Mode mode){
        this.mode = mode;
        this.setings = new Setings();

    }
    /*
        Метод запускающий сканирование без лишней инициализации
     */
    public List<Attentions> scan(@NotNull String patch){
        return scan(patch);
    }
    /*
        Создание отчета
     */
    public void writeReport(){

    }

}
