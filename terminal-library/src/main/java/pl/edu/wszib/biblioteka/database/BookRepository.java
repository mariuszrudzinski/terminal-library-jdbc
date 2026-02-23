package pl.edu.wszib.biblioteka.database;

import pl.edu.wszib.biblioteka.exceptions.BookNotFoundException;
import pl.edu.wszib.biblioteka.exceptions.BookNotRentedException;
import pl.edu.wszib.biblioteka.exceptions.CanNotRentBookException;
import pl.edu.wszib.biblioteka.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository implements IBookRepository {
    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        this.books.add(new Book(1, "1984", "George Orwell", 1949));
        this.books.add(new Book(2, "Folwark zwierzęcy", "George Orwell", 1945));
        this.books.add(new Book(3, "Hobbit", "J.R.R. Tolkien", 1937));
        this.books.add(new Book(4, "Władca Pierścieni", "J.R.R. Tolkien", 1954));
        this.books.add(new Book(5, "Zbrodnia i kara", "Fiodor Dostojewski", 1866));
        this.books.add(new Book(6, "Mistrz i Małgorzata", "Michaił Bułhakow", 1967));
        this.books.add(new Book(7, "Duma i uprzedzenie", "Jane Austen", 1813));
        this.books.add(new Book(8, "Sto lat samotności", "Gabriel García Márquez", 1967));
        this.books.add(new Book(9, "Imię róży", "Umberto Eco", 1980));
        this.books.add(new Book(10, "Proces", "Franz Kafka", 1925));
    }
    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public void rentBook(int bookId, int userId) {
        for(Book c : this.books) {
            if(c.getId() == bookId && !c.isRent()) {
                c.setRent(true);
                return;
            }
        }
        throw new CanNotRentBookException();
    }

    @Override
    public void returnBook(int bookId, int userId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                if (!book.isRent()) {
                    throw new BookNotRentedException();
                }
                book.setRent(false);
                return;
            }
        }
        throw new BookNotFoundException();
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public void addBook(Book book) {
        book.setId(generateId());
        books.add(book);
    }

    public int generateId() {
        return this.books.stream().mapToInt(Book::getId).max().orElse(0) + 1;
    }

    @Override
    public void editBook(int id, String title, String author, int year) {
        for (Book book : books) {
            if (book.getId() == id) {
                book.setTitle(title);
                book.setAuthor(author);
                book.setYear(year);
                return;
            }
        }
        throw new BookNotFoundException();
    }

    @Override
    public void deleteBook(int id) {
        boolean removed = this.books.removeIf(c -> c.getId() == id);

        if (!removed) {
            throw new BookNotFoundException();
        }
    }
}
