package org.jara.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jara.core.Attentions;
import org.jara.core.Core;
import org.jara.mode.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Setter
@Getter
public class Engine {
    /*
        Класс определяющий настройки модуля
     */
    private Settings settings;
    /*
        Список мест, к которым надо обратить внимание
     */
    private List<Attentions> attentions;
    /*
        Ошибка в работе, если таковая имеется
     */
    private String error;
    /*
        Основная двигающая сила
     */
    private Core core;

    public Engine(Settings settings){
        this.settings = settings;
    }
    /*
        Метод запускающий сканирование без лишней инициализации
     */
    public List<Attentions> scan(@NotNull String patch){
        return scan(patch);
    }
    /*
        Основной метод запуска
     */
    public void scan(){
        Engine engine = new Engine(settings);

        engine = core.scanningStart(engine);
        writeReport(engine.getAttentions());
    }
    /*
        Создание отчета(Если в режиме выбран)
     */
    public void writeReport(Attentions[] attentions){

    }

}
