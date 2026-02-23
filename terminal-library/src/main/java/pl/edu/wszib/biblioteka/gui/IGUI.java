package pl.edu.wszib.biblioteka.gui;

import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.User;

import java.util.List;

public interface IGUI {
    String showMenuAndReadChoose(boolean isAdmin);

    int readId();

    String readTitle();

    String readAuthor();

    int readYear();

    void listBooks(List<Book> books);

    void showRentSuccessMessage(boolean success);


    void showReturnSuccessMessage(boolean success);

    void showFindTitleSuccessMessage(boolean success);

    void showFindAuthorSuccessMessage(boolean success);

    void showBookNotFoundMessage();

    void showAddBookMessage(boolean success);

    void showEditedBookMessage(boolean success);

    void showDeletedBookMessage(boolean success);

    void showWrongOptionMessage();
    User readLoginAndPassword();

    Book readBookData();

    Book readNewBookData();
}
