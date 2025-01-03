package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Backend.User;

public class UserDAO {
    // Add a user to the database
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (user_id, name, address, contact_number, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getuserid());
            stmt.setString(2, user.getname());
            stmt.setString(3, user.getaddress());
            stmt.setString(4, user.getcontactNumber());
            stmt.setString(5, user.getusername());
            stmt.setString(6, user.getpassword());
            stmt.executeUpdate();
        }
    }

    // Update user details
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, address = ?, contact_number = ?, username = ?, password = ? WHERE user_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getname());
            stmt.setString(2, user.getaddress());
            stmt.setString(3, user.getcontactNumber());
            stmt.setString(4, user.getusername());
            stmt.setString(5, user.getpassword());
            stmt.setInt(6, user.getuserid());
            stmt.executeUpdate();
        }
    }

    // Delete a user by ID
    public void deleteUser(int userID) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        }
    }

    // Retrieve a user by ID
    public User getUserByID(int userID) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("contact_number")
                );
            }
        }
        return null; // Return null if the user is not found
    }

    // Retrieve all users from the database
    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("contact_number")
                ));
            }
        }
        return users;
    }

    // Check if a user exists (useful for login)
    public boolean validateUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If a match is found, the user exists
        }
    }
}
