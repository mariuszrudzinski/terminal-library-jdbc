package pl.edu.wszib.biblioteka.database;

import pl.edu.wszib.biblioteka.model.Book;

import java.util.List;

public interface IBookRepository {
    List<Book> getBooks();

    void rentBook(int bookId, int userId);

    void returnBook(int bookId, int userId);

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    void addBook(Book book);

    void editBook(int id, String title, String author, int year);

    void deleteBook(int i);
}
