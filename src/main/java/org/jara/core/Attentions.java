package org.jara.core;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Attentions {
    /*
        Название файла
     */
    private String nameFile;
    /*
        Позиция в коде
     */
    private int line;
    /*
        Дубликат кода в котором найдено дублирование
     */
    private String code;
    /*
        Описание проблемы
     */
    private String description;
}
