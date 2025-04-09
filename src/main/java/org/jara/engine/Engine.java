package org.jara.engine;

import lombok.Getter;
import lombok.Setter;
import org.jara.core.Attentions;
import org.jara.core.Core;
import org.jara.core.FileUtils;
import org.jara.mode.Mode;
import org.jara.mode.Settings;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.jara.core.FileUtils.writeReport;

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


    public Engine(Settings settings) {
        this.settings = settings;
        core = new Core(settings);
    }

    public Engine() {
        settings = new Settings();
    }

    /*
        Метод запускающий сканирование без лишней инициализации
        запускает анализ только 1 файла
     */
    public List<Attentions> scan(@NotNull String patch) {
        settings = new Settings();
        settings.setInputDir(patch);
        core = new Core(settings);

        return core.scanningOneFile();
    }

    /*
        Основной метод запуска для прохода по всем файлам
     */
    public void scan() {
        attentions = core.scanningStart();
        if ((settings.getMode().equals(Mode.WriteAST) || settings.getMode().equals(Mode.WriteHash))
                && settings.getOutputDir() != null) {
            FileUtils.writeReport(settings, attentions);
        }
    }

    /*
        Сканирование только 1 файла
     */
    public void scanOneFile() {
        attentions = core.scanningOneFile();
        if ((settings.getMode().equals(Mode.WriteAST) || settings.getMode().equals(Mode.WriteHash))
                && settings.getOutputDir() != null) {
            FileUtils.writeReport(settings, attentions);
        }
    }


}
