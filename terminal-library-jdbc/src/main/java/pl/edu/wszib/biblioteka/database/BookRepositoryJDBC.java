package pl.edu.wszib.biblioteka.database;

import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.configuration.DatabaseConnection;
import pl.edu.wszib.biblioteka.exceptions.BookNotFoundException;
import pl.edu.wszib.biblioteka.exceptions.BookNotRentedException;
import pl.edu.wszib.biblioteka.exceptions.CanNotRentBookException;
import pl.edu.wszib.biblioteka.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookRepositoryJDBC implements IBookRepository {

    private final DatabaseConnection db;

    public BookRepositoryJDBC(DatabaseConnection db) {
        this.db = db;
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, publication_year, rented) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            ps.setBoolean(4, false);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    book.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editBook(int id, String title, String author, int year) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, year);
            ps.setInt(4, id);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new BookNotFoundException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            if (deleted == 0) throw new BookNotFoundException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + title.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRowToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(author) LIKE ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + author.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRowToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public void rentBook(int bookId, int userId) {

        String checkSql = "SELECT rented FROM books WHERE id = ?";
        String insertRentalSql = "INSERT INTO rentals (book_id, user_id) VALUES (?, ?)";
        String updateBookSql = "UPDATE books SET rented = ? WHERE id = ?";

        try (PreparedStatement check = db.getConnection().prepareStatement(checkSql)) {

            check.setInt(1, bookId);

            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) throw new BookNotFoundException();

                boolean rented = rs.getBoolean("rented");
                if (rented) throw new CanNotRentBookException();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement insert = db.getConnection().prepareStatement(insertRentalSql);
             PreparedStatement update = db.getConnection().prepareStatement(updateBookSql)) {

            insert.setInt(1, bookId);
            insert.setInt(2, userId);
            insert.executeUpdate();

            update.setBoolean(1, true);
            update.setInt(2, bookId);
            update.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void returnBook(int bookId, int userId) {
        String checkRental = "SELECT * FROM rentals WHERE book_id = ? AND user_id = ? AND return_date IS NULL";
        String updateRental = "UPDATE rentals SET return_date = CURRENT_DATE WHERE book_id = ? AND user_id = ? AND return_date IS NULL";
        String updateBook = "UPDATE books SET rented = FALSE WHERE id = ?";

        try (PreparedStatement check = db.getConnection().prepareStatement(checkRental)) {
            check.setInt(1, bookId);
            check.setInt(2, userId);
            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) throw new BookNotRentedException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement updateR = db.getConnection().prepareStatement(updateRental);
             PreparedStatement updateB = db.getConnection().prepareStatement(updateBook)) {

            updateR.setInt(1, bookId);
            updateR.setInt(2, userId);
            updateR.executeUpdate();

            updateB.setInt(1, bookId);
            updateB.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("publication_year"),
                rs.getBoolean("rented")
        );
    }
}

