package pl.edu.wszib.biblioteka.gui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.model.Book;
import pl.edu.wszib.biblioteka.model.User;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class GUI implements IGUI {
    private final Scanner scanner;

    @Override
    public String showMenuAndReadChoose(boolean isAdmin) {
        System.out.println("\n1. List books");
        System.out.println("2. Rent book");
        System.out.println("3. Return book");
        System.out.println("4. Find book by title");
        System.out.println("5. Find book by author");
        System.out.println("6. Exit");

        if (isAdmin) {
            System.out.println("7. Add book");
            System.out.println("8. Edit book");
            System.out.println("9. Delete book");
        }
        return this.scanner.nextLine();
    }

    @Override
    public int readId() {
        System.out.println("Podaj ID:");

        return Integer.parseInt(this.scanner.nextLine());
    }

    @Override
    public String readTitle() {
        System.out.println("Podaj tytuł:");

        return this.scanner.nextLine();
    }

    @Override
    public String readAuthor() {
        System.out.println("Podaj autora:");

        return this.scanner.nextLine();
    }

    @Override
    public int readYear()
    {
        System.out.println("Podaj rok:");

        return Integer.parseInt(this.scanner.nextLine());
    }

    @Override
    public void listBooks(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public Book readNewBookData(){
        int id = readId();
        String title = readTitle();
        String author = readAuthor();
        int year = readYear();

        return new Book(id, title, author, year);
    }
    public Book readBookData() {
        String title = readTitle();
        String author = readAuthor();
        int year = readYear();

        return new Book(title, author, year);
    }


    @Override
    public void showRentSuccessMessage(boolean success) {
        System.out.println(
                success ?
                        "\nBook rented successfully." :
                        "\nCannot rent the book.");
    }

    @Override
    public void showReturnSuccessMessage(boolean success) {
        System.out.println(
                success ?
                        "\nBook returned successfully." :
                        "\nCannot return this book."
        );
    }

    @Override
    public void showFindTitleSuccessMessage(boolean success) {
        System.out.println(
                success ?
                        "\nBooks with this title: " :
                        "\nNo books found with this title.");
    }

    @Override
    public void showFindAuthorSuccessMessage(boolean success) {
        System.out.println(
                success ?
                        "\nBooks with this author: " :
                        "\nNo books found with this author.");
    }

    @Override
    public void showBookNotFoundMessage() {
        System.out.println("\nBook not found.");
    }

    @Override
    public void showAddBookMessage(boolean success) {
        System.out.println(
                success ?
                        "\nBook added successfully!" :
                        "\nFailed to add the book."
        );
    }

    @Override
    public void showEditedBookMessage(boolean success) {
        System.out.println(
                success ?
                        "\nBook edited successfully!" :
                        "\nFailed to edit the book."
        );
    }

    @Override
    public void showDeletedBookMessage(boolean success) {
        System.out.println(
                success ?
                        "\nBook deleted successfully!" :
                        "\nFailed to delete the book."
        );
    }

    @Override
    public void showWrongOptionMessage() {
        System.out.println("\nWrong option. Please try again.");
    }


    @Override
    public User readLoginAndPassword() {
        System.out.println("Login: ");
        String login = this.scanner.nextLine();
        System.out.println("Password: ");
        String password = this.scanner.nextLine();
        return new User(0, login, password, null);
    }
}
