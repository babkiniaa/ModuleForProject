package org.jara.engine;

import lombok.Getter;
import lombok.Setter;
import org.jara.core.Attentions;
import org.jara.core.Core;
import org.jara.mode.Mode;
import org.jara.mode.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
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

    public Engine(Settings settings) {
        this.settings = settings;
        core = new Core();
    }

    /*
        Метод запускающий сканирование без лишней инициализации
     */
    public List<Attentions> scan(@NotNull String patch) {
        return scan(patch);
    }

    /*
        Основной метод запуска для прохода по всем файлам
     */
    public void scan() {
        attentions = core.scanningStart(settings, settings.getInputDir());
        if (settings.getMode().equals(Mode.Write) && settings.getOutputDir() != null) {
            writeReport(attentions);
        }
    }
    /*
        Сканирование только 1 файла
     */
    public void scanOneFile() {
        attentions = core.scanningOneFile(settings, settings.getInputDir());
        if (settings.getMode().equals(Mode.Write) && settings.getOutputDir() != null) {
            writeReport(attentions);
        }
    }

    /*
        Создание отчета(Если в режиме выбран)
     */
    public void writeReport(List<Attentions> attentions) {
        try {
            File file = new File(settings.getOutputDir());
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = stringAttentions(attentions).getBytes();

            fileOutputStream.write(buffer);
            fileOutputStream.close();

        } catch (Exception e) {
            error = e.getMessage();
        }

    }

    /*
        Записываем список из Внимания в строку для дальнейшей записи в файл
     */
    public String stringAttentions(List<Attentions> attentions) {
        String res = "";

        for (var attention : attentions) {
            res += attention.getNameFile() + "\r\n";
            res += attention.getLine() + "\r\n";
            res += attention.getCode() + "\r\n";
            res += attention.getDescription() + "\r\n";
            res += '\n';
        }

        return res;
    }

}
