package ru.puzikov.springcourse.LibraryAppBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.puzikov.springcourse.LibraryAppBoot.models.Person;


import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByFullName(String fullName);
    Optional<Person> findByDateOfBirth(Date dateOfBirth);
    List<Person> findByPhoneStartingWith(String phone);
}
