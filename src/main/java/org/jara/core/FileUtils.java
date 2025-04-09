package org.jara.core;

import org.jara.mode.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class FileUtils {

    /*
    Создание отчета(Если в режиме выбран)
    */
    public static void writeReport(Settings settings, List<Attentions> attentions) {
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
            throw new RuntimeException("Проблема при создании отчета");
        }

    }

    /*
        Записываем список из Внимания в строку для дальнейшей записи в файл
     */
    public static String stringAttentions(List<Attentions> attentions) {
        StringBuilder res = new StringBuilder();

        for (var attention : attentions) {
            res.append(attention.getNameFile() + "\r\n");
            res.append(attention.getCode() + "\r\n");
            res.append(attention.getDescription() + "\r\n");
            res.append('\n');
        }

        return res.toString();
    }

}
