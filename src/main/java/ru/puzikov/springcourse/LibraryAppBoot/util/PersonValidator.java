package ru.puzikov.springcourse.LibraryAppBoot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.puzikov.springcourse.LibraryAppBoot.models.Person;
import ru.puzikov.springcourse.LibraryAppBoot.services.PeopleService;


@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.getPersonByFullName(person.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже существует");

//        if (peopleService.getPersonByDateOfBirth(person.getDateOfBirth()).isEmpty())
//            errors.rejectValue("dateOfBirth", "", "Укажите дату рождения в формате дд/мм/гггг");
    }
}
