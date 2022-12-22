package ru.suh.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.suh.springcourse.dao.PersonDAO;
import ru.suh.springcourse.models.Person;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO; // внедряем personDAO для возможности работы с БД

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) { // в этом методе нужно дать понять спрингу к какому классу относится этот валидатор
        return Person.class.equals(aClass); // класс Person д. == классу передаваемому в параметрах (aClass)
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person)o; // делаем даункаст
        // проверяем есть ли человек с таким же email в БД

        if (personDAO.show(person.getEmail()).isPresent()) { // если из метода вернулся не null, значит человек
                                                        // с таким email-ом есть в таблице Person,
                                                       // значит - это ошибка
            errors.rejectValue("email", "", "This email is already taken");
            // 1 - причина ошибки, 2 - код ошибки (пока пустое), 3 - сообщение об ошибке
        }



    }
}
