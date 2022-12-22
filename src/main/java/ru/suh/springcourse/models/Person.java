package ru.suh.springcourse.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person{
    private int id;
    @NotEmpty(message = "Name should not be empty") // имя не дорлжно быть пустым
    @Size(min= 2, max = 30, message = "Name >2 and <30 char")
    private String name;
    @Min(value = 0, message = "age<0")
    @Max(value = 100, message = "age>0")
    private int age;
    @NotEmpty // емаил не д.б. пустым
    @Email (message = "Email should be valid")// емаил д.б емайлом
    private String email;


    // Страна, Город, Индекс (6 цифр)
    @Pattern (regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "address format = Russia, Yaroslavl, 150000")  // регулярка
    private String address; // Страна, Город, Индекс (6 цифр) - Russia, Yaroslavl, 150000

    public Person() { // пустой конструктор нужен для работы Spring, для автоматического создания экземпляра класса,
                      // и далбнейшего заполнения полей с помощью сеттеров
    }

    public Person(int id, String name, int age, String email, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
