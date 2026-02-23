package pl.edu.wszib.biblioteka.database;

import org.springframework.stereotype.Component;
import pl.edu.wszib.biblioteka.configuration.DatabaseConnection;
import pl.edu.wszib.biblioteka.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepositoryJDBC implements IUserRepository {

    private final DatabaseConnection db;

    public UserRepositoryJDBC(DatabaseConnection db) {
        this.db = db;
    }

    @Override
    public User findUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
