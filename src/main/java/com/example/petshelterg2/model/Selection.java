package com.example.petshelterg2.model;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Класс представляет собой "серую базу", в которую сохраняются абсолютно все пользователи которые хоть раз взаимодействовали с ботом дальше кнопки выбора приюта. <p>
 * <p>
 * <b>chatId</b> (primary key) хранит id чата пользователя <p>
 * <b>selection</b>  хранить Boolean значение: <p>
 * <u>false</u> - кошки <p>
 * <u>true</u> - собаки <p>
 */
@Data
@Entity(name = "Selection")
public class Selection {

    @Id
    private Long chatId;            //чат id как primary key

    private Boolean selection;      //переменная выбора приюта кошки - false (0), собаки true (1)
    private Integer counter;        //переменная для этапов отчета
}
