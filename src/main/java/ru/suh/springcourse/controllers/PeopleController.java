package ru.suh.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.suh.springcourse.dao.PersonDAO;
import ru.suh.springcourse.models.Person;
import ru.suh.springcourse.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people" ) // все наши адреса будут начинаться со /people
public class PeopleController {


    private final PersonDAO personDAO;
    public final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    // @GetMapping() пустой . К нему попадаем по адресу http://localhost:8080/people
    @GetMapping() // в методе получим всех людей из DAO и передадим на отображение в представление
    public String index(Model model){
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}") // получим одного человека по id из DAO и передадим его в отображение
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("person",personDAO.show(id)); // получаем человека и вставляем его в модель
        model.addAttribute("books", personDAO.getBooksByPersonId(id)); // получаем книги этого человека

        return "people/show";
    }

    @GetMapping("/new") // по данному адресу (http://localhost:8080/people/new) вернется форма для нового чела
    public String newPerson(@ModelAttribute("person") Person person){
         // model создает нового человека с пустым конструктором
        return  "people/new"; // вернем таимлив шаблон по созданию человека
    }

    @PostMapping ()// создание нового человека из формы, добавление его в БД
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){ // создание нового чел, добавление ему полей с формы и добавление в модель
                             //  @Valid проверяет валидные ли поля, BindingResult bindingResult - в этот объект  запишутся ошибки валидации

        personValidator.validate(person, bindingResult); // проверка на E-mail, в bindingResult - хранятся ошибки всех валидаций

        if(bindingResult.hasErrors())
            return "people/new"; // если будут ошибки -> вернем форму создания нового человека, но в это форме будут показаны ошибки

        personDAO.save(person);
        return "redirect:/people"; // вернемся к списку людей
    }

    @GetMapping("/{id}/edit")  // http://localhost:8080/people/id/edit == http://localhost:8080/people/2/edit
    public String edit(Model model, @PathVariable("id") int id){  // на вход модель и id
        model.addAttribute("person", personDAO.show(id)); // в модель внедряем поля текущего человека
        return "people/edit"; // возвращает страницу с редактированием
    }

    @PatchMapping("/{id}") // метод примет запрос на изменение человека
    public String update(@ModelAttribute("person") @Valid  Person person, BindingResult bindingResult, @PathVariable("id") int id){


        personValidator.validate(person, bindingResult); // проверка на E-mail, в bindingResult - хранятся ошибки всех валидаций

        if(bindingResult.hasErrors()) {
            return "people/edit"; // если будут ошибки -> вернем форму создания нового человека, но в это форме будут показаны ошибки
        }

        personDAO.update(id, person); // запускаем метод класса personDAO, кот обновит данные по человеку

        return "redirect:/people"; // вернемся к списку людей
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
