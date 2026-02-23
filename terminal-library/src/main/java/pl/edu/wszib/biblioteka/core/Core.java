package pl.edu.wszib.biblioteka.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.authentication.IAuthenticator;
import pl.edu.wszib.biblioteka.database.IBookRepository;
import pl.edu.wszib.biblioteka.database.IUserRepository;
import pl.edu.wszib.biblioteka.exceptions.BookNotFoundException;
import pl.edu.wszib.biblioteka.exceptions.BookNotRentedException;
import pl.edu.wszib.biblioteka.exceptions.CanNotRentBookException;
import pl.edu.wszib.biblioteka.gui.IGUI;
import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Core implements ICore {
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;
    private final IGUI gui;
    private final IAuthenticator authenticator;

    @Override
    public void run() {
        User tempUser = gui.readLoginAndPassword();
        boolean isAuthenticated = authenticator.authenticate(tempUser.getLogin(), tempUser.getPassword());

        User user = userRepository.findUserByLogin(tempUser.getLogin());

        if (user == null) {
            System.out.println("Niepoprawny login.");
            return;
        }

        if (!isAuthenticated) {
            System.out.println("Niepoprawne hasło.");
            return;
        }

        boolean isAdmin = user.getRole().equalsIgnoreCase("ADMIN");

        while (isAuthenticated) {
            String choice = gui.showMenuAndReadChoose(isAdmin);

            switch (choice) {
                case "1":
                    gui.listBooks(bookRepository.getBooks());
                    break;

                case "2":
                    try {
                        int bookId = gui.readId();
                        bookRepository.rentBook(bookId, user.getId());
                        gui.showRentSuccessMessage(true);
                    } catch (CanNotRentBookException e) {
                        gui.showRentSuccessMessage(false);
                    }
                    break;

                case "3":
                    try {
                        int bookId = gui.readId();
                        bookRepository.returnBook(bookId, user.getId());
                        gui.showReturnSuccessMessage(true);
                    } catch (BookNotFoundException | BookNotRentedException e) {
                        gui.showReturnSuccessMessage(false);
                    }
                    break;

                case "4":
                    List<Book> byTitle = bookRepository.findByTitle(gui.readTitle());
                    if (byTitle.isEmpty()) {
                        gui.showFindTitleSuccessMessage(false);
                    } else {
                        gui.showFindTitleSuccessMessage(true);
                        gui.listBooks(byTitle);
                    }
                    break;

                case "5":
                    List<Book> byAuthor = bookRepository.findByAuthor(gui.readAuthor());
                    if (byAuthor.isEmpty()) {
                        gui.showFindAuthorSuccessMessage(false);
                    } else {
                        gui.showFindAuthorSuccessMessage(true);
                        gui.listBooks(byAuthor);
                    }
                    break;

                case "6":
                    return;

                case "7":
                    if (isAdmin) {
                        bookRepository.addBook(gui.readBookData());
                        gui.showAddBookMessage(true);
                    } else {
                        gui.showWrongOptionMessage();
                    }
                    break;

                case "8":
                    if (isAdmin) {
                        try {
                            Book edited = gui.readNewBookData();
                            bookRepository.editBook(
                                    edited.getId(),
                                    edited.getTitle(),
                                    edited.getAuthor(),
                                    edited.getYear()
                            );
                            gui.showEditedBookMessage(true);
                        } catch (BookNotFoundException e) {
                            gui.showBookNotFoundMessage();
                        }
                    } else {
                        gui.showWrongOptionMessage();
                    }
                    break;

                case "9":
                    if (isAdmin) {
                        try {
                            bookRepository.deleteBook(gui.readId());
                            gui.showDeletedBookMessage(true);
                        } catch (BookNotFoundException e) {
                            gui.showBookNotFoundMessage();
                        }
                    } else {
                        gui.showWrongOptionMessage();
                    }
                    break;

                default:
                    gui.showWrongOptionMessage();
                    break;
            }
        }
    }
}
