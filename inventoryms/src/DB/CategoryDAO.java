package DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Backend.Category;

public class CategoryDAO {
    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO categories (category_id, category_name) VALUES (?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getCategoryID());
            stmt.setString(2, category.getCategoryName());
            stmt.executeUpdate();
        }
    }
    public void deleteCategory(String categoryID) throws SQLException {
        String query = "DELETE FROM categories WHERE category_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, categoryID);
            stmt.executeUpdate();
        }
    }
    public void updateCategory(Category category) throws SQLException {
        String query = "UPDATE categories SET category_name = ? WHERE category_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getCategoryID());
            stmt.executeUpdate();
        }
    }
    public List<Category> getAllCategories() throws SQLException {
        String query = "SELECT * FROM categories";
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getString("category_id"),
                        rs.getString("category_name")
                ));
            }
        }
        return categories;
    }
    public Category getCategory(String categoryID) throws SQLException{
        String query="SELECT *FROM categories WHERE category_id=?";
        try( Connection conn =DBconnection.getConnection();)
        {
            PreparedStatement statement =conn.prepareStatement(query);
            statement.setString(1, categoryID);
            ResultSet resultSet =statement.executeQuery();
            if(resultSet.next())
            {
                return new Category(resultSet.getString("category_id"), resultSet.getString("category_name"));
            }
            return null;
        }
    }
}
