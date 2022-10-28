package ru.puzikov.springcourse.LibraryAppBoot.services;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.puzikov.springcourse.LibraryAppBoot.models.Book;
import ru.puzikov.springcourse.LibraryAppBoot.models.Person;
import ru.puzikov.springcourse.LibraryAppBoot.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    public List<Person> searchByPhone(String query){
       return peopleRepository.findByPhoneStartingWith(query);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }
    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }
    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName){
        return peopleRepository.findByFullName(fullName);
    }

    public Optional<Person> getPersonByDateOfBirth(Date dateOfBirth){
        return peopleRepository.findByDateOfBirth(dateOfBirth);
    }

    public List<Book>getBooksByPersonId(int id) {
        Optional<Person> person =peopleRepository.findById(id);

        if (person.isPresent()){
            Hibernate.initialize(person.get().getBooks());

            // проверка просроченности книг
            person.get().getBooks().forEach(book -> {
                long diffInMills = Math.abs(book.getTaken_at().getTime() - new Date().getTime());
                if (diffInMills > 864000000)
                    book.setExpired(true);
            });

            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
