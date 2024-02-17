package ru.novikov.library.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.novikov.library.models.Book;
import ru.novikov.library.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> indexBook() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Book showBook(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
    }

    @Transactional
    public void saveBook(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.save(book);
    }

    @Transactional
    public void updateBook(int id, Book updatedBook) {
        Session session = sessionFactory.getCurrentSession();
        Book bookToBeUpdated = session.get(Book.class, id);

        bookToBeUpdated.setNameBook(updatedBook.getNameBook());
        bookToBeUpdated.setNameAuthor(updatedBook.getNameAuthor());
        bookToBeUpdated.setYear(updatedBook.getYear());
    }
    @Transactional
    public void deleteBook(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Book.class, id));
    }
    @Transactional(readOnly = true)
    public Optional<Person> getBookOwner(int id) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);
        if (book != null) {
            return Optional.ofNullable(book.getOwner());
        } else {
            return Optional.empty();
        }
    }
    @Transactional
    public void release(int id) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);
        if (book != null) {
            book.setOwner(null);
            session.update(book);
        }

    }
    @Transactional
    public void assign(int id, Person selectedPerson) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);
        if (book != null) {
            book.setOwner(selectedPerson);
            session.update(book);
        }

    }
}
