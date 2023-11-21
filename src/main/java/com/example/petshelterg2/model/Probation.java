package com.example.petshelterg2.model;

/**
 * Enum отвечающий за статусы испытательного срока. <p>
 * Хранит в себе 8 вариантов по индексам: <p>
 * 0)Ещё не назначен - NOT_ASSIGNED <p>
 * 1)В процессе - IN_PROGRESS  <p>
 * 2)Пройден - PASSED <p>
 * 3)Не пройден - FAILED <p>
 * 4)Продлен на 14 дней - EXTENDED_14 <p>
 * 5)Продлен на 30 дней - EXTENDED_30 <p>
 * 6)Завершен c провалом - COMPLETED_FAILED <p>
 * 7)Завершен с успехом - COMPLETED_SUCCESS <p>
 */
public enum Probation {
    NOT_ASSIGNED, IN_PROGRESS, PASSED, FAILED, EXTENDED_14 , EXTENDED_30 , COMPLETED_FAILED, COMPLETED_SUCCESS
}
