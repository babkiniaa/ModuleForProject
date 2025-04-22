package org.jara.core;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
