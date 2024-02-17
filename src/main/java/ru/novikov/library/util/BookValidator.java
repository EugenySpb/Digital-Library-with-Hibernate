package ru.novikov.library.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.novikov.library.dao.BookDAO;
import ru.novikov.library.models.Book;

@Component
public class BookValidator implements Validator {
    private final BookDAO bookDAO;

    @Autowired
    public BookValidator(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals((aClass));
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        if (!Character.isUpperCase(book.getNameAuthor().codePointAt(0))) {
            errors.rejectValue("name_author", "", "Name should start with a capital letter");
        }

    }
}
