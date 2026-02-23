package pl.edu.wszib.biblioteka.configuration;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInit {

    public DatabaseInit(DatabaseConnection db) {
        init(db.getConnection());
    }

    private void init(Connection connection) {
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "author VARCHAR(255), " +
                    "publication_year INT, " +
                    "rented BOOLEAN DEFAULT FALSE" +
                    ")");

            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "login VARCHAR(100), " +
                    "password VARCHAR(100), " +
                    "role VARCHAR(10)" +
                    ")");

            st.execute("CREATE TABLE IF NOT EXISTS rentals (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "book_id INT NOT NULL, " +
                    "user_id INT NOT NULL, " +
                    "rent_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "return_date TIMESTAMP NULL, " +
                    "FOREIGN KEY (book_id) REFERENCES books(id), " +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")");


            st.execute("INSERT INTO users (login, password, role) VALUES " +
                    "('admin', '21232f297a57a5a743894a0e4a801fc3', 'ADMIN')," +
                    "('user', 'ee11cbb19052e40b07aac0ca060c23ee', 'USER')");

            st.execute("INSERT INTO books (title, author, publication_year, rented) VALUES " +
                    "('1984', 'George Orwell', 1949, FALSE)," +
                    "('Folwark zwierzęcy', 'George Orwell', 1945, FALSE)," +
                    "('Hobbit', 'J.R.R. Tolkien', 1937, FALSE)," +
                    "('Władca Pierścieni', 'J.R.R. Tolkien', 1954, FALSE)," +
                    "('Zbrodnia i kara', 'Fiodor Dostojewski', 1866, FALSE)," +
                    "('Mistrz i Małgorzata', 'Michaił Bułhakow', 1967, FALSE)," +
                    "('Duma i uprzedzenie', 'Jane Austen', 1813, FALSE)," +
                    "('Sto lat samotności', 'Gabriel García Márquez', 1967, FALSE)," +
                    "('Imię róży', 'Umberto Eco', 1980, FALSE)," +
                    "('Proces', 'Franz Kafka', 1925, FALSE)");

            System.out.println("Baza danych zainicjalizowana!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

