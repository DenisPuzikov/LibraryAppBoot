package ru.puzikov.springcourse.LibraryAppBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.puzikov.springcourse.LibraryAppBoot.models.Book;
import ru.puzikov.springcourse.LibraryAppBoot.models.Person;
import ru.puzikov.springcourse.LibraryAppBoot.repositories.BooksRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear){
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }
    public Book findOne(int id){
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public List<Book> searchByTitle(String query){
        return booksRepository.findByTitleStartingWith(query);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }
    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }
    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(updatedBook);
    }
    @Transactional
    public void release(int id){
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTaken_at(null);
        });
    }

    public Person getBookOwner(int id){
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void assign(int id, Person selectedPerson){
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTaken_at(new Date());
        });
    }
}

