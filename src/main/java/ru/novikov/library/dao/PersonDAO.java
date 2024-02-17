package ru.novikov.library.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.novikov.library.models.Book;
import ru.novikov.library.models.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select p from Person p", Person.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Person showPerson(int person_id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, person_id);
    }

    @Transactional
    public void savePerson(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }
    @Transactional
    public void updatePerson(int person_id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        Person personToBeUpdated = session.get(Person.class, person_id);

        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
    }

    @Transactional
    public void deletePerson(int person_id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Person.class, person_id));
    }

    //Для валидации уникальности ФИО
    @Transactional(readOnly = true)
    public Optional<Person> getPersonName(String name) {
        Session session = sessionFactory.getCurrentSession();

        List<Person> persons = session.createQuery("select p from Person p where p.name = :name", Person.class)
                .setParameter("name", name)
                .getResultList();

        return persons.stream().findAny();
    }

    @Transactional
    public List<Book> getBooksByPersonId(int person_id) {
        Session session = sessionFactory.getCurrentSession();

        Person person = session.get(Person.class, person_id);
        if (person != null) {
            return new ArrayList<>(person.getBooks());
        } else {
            return Collections.emptyList();
        }
    }
}
