package ru.suh.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.suh.springcourse.models.Book;
import ru.suh.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// класс будет осуществлять логику между контроллером и представлением
@Component // Spring создаст объект этого класса
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate; // объявляем переменную JdbcTemplate

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) { // внедряем JdbcTemplate в конструкторе с помощью @Autowired
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Person> index() {// вернет список
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class)); // сам создаст маппер на основе класса
    }

    public Person show(int id) { // вернет человека по id
        return jdbcTemplate.query("SELECT * FROM Person WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null); // лямда - если человек в списке Object[] найден вернуть его если не найден вернуть нул
    }

    public Optional <Person> show (String email) {  //  перезагрузка метода, в качестве агрумента принимает Email и возвращает человека с указанным Email,
                                                 // если он есть в БД
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?", new Object[]{email},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void save(Person person) { // метод сохраняет нового человека в БД
        jdbcTemplate.update("INSERT INTO Person (name, age, email, address )VALUES (?,?,?,?)",
                person.getName(), person.getAge(), person.getEmail(), person.getAddress());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=?, address=?  WHERE id=?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getAddress(),  id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    // Для валидации уникальности ФИО
    public Optional<Person> getPersonName(String name) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE name=?", new Object[]{name},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    // Здесь Join не нужен. И так уже получили человека с помощью отдельного метода
    public List<Book> getBooksByPersonId(int id) {  // метод показывает список книг, которые взял человек
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}

