package com.example.petshelterg2.model;

//класс для информации о пользователях кошек

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
@Entity(name = "CatOwners")
public class CatOwners {
    @Id
    private Long chatId; // (Primary Key)

    private String lastName;
    private String firstName;
    private String userName;
    private String phoneNumber;

//    Как только период в 30 дней заканчивается, волонтеры принимают решение о том, остается животное у хозяина или нет.
//    Испытательный срок может быть пройден, может быть продлен на срок еще 14 или 30 дней, а может быть не пройден.
//    Нам нужно реализовать это задание через этот параметр. Параметр скорее всего будет хранить конечный день (30й) испытательного срока
//    скорее всего нужно будет добавить метод по шедулду который проверяет наступил ли этот день или нет, и если да, отправляет сообщения о прохождении
//    и еще пару методов которые добавляют 14 или 30 дней к испытательному сроку
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "catOwners")
    private Set<CatReport> catReports;

    public CatOwners(Long chatId, String lastName, String firstName, String userName, String phoneNumber, LocalDateTime dateTime, Set<CatReport> catReports) {
        this.chatId = chatId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.dateTime = dateTime;
        this.catReports = catReports;
    }

    public CatOwners() {
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<CatReport> getCatReports() {
        return catReports;
    }

    public void setCatReports(Set<CatReport> catReports) {
        this.catReports = catReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatOwners catOwners = (CatOwners) o;
        return Objects.equals(getChatId(), catOwners.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId());
    }

}
