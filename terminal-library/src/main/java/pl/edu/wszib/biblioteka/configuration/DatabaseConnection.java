package pl.edu.wszib.biblioteka.configuration;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DatabaseConnection {

    @Getter
    private Connection connection;
    private final String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private final String user = "sa";
    private final String password = "";

    public DatabaseConnection() {
        try {
            connect();
        } catch (SQLException e) {
            throw new RuntimeException("Nie udało się połączyć z bazą danych!", e);
        }
    }

    private void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
