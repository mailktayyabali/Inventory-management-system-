package DB;

import Backend.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection getConnection() throws SQLException {
        return DBconnection.getConnection();
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, c.category_name " +
                       "FROM products p JOIN categories c ON p.category_id = c.category_id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("category_name")
                ));
            }
        }
        return products;
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (product_id, name, description, price, stock_quantity, category_id) " +
                       "VALUES (?, ?, ?, ?, ?, (SELECT category_id FROM categories WHERE category_name = ?))";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getProductID());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getStockQuantity());
            stmt.setString(6, product.getCategoryName());
            stmt.executeUpdate();
        }
    }

    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, " +
                       "category_id = (SELECT category_id FROM categories WHERE category_name = ?) " +
                       "WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setString(5, product.getCategoryName());
            stmt.setString(6, product.getProductID());
            stmt.executeUpdate();
        }
    }

    public void deleteProduct(String productID) throws SQLException {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productID);
            stmt.executeUpdate();
        }
    }

    public Product getProductById(String id) throws SQLException {
        String query = "SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, c.category_name " +
                       "FROM products p JOIN categories c ON p.category_id = c.category_id WHERE p.product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("category_name")
                );
            }
        }
        return null;
    }

    public List<Product> searchProductsByName(String name) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, p.name, p.description, p.price, p.stock_quantity, c.category_name " +
                       "FROM products p JOIN categories c ON p.category_id = c.category_id WHERE p.name LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("category_name")
                ));
            }
        }
        return products;
    }
}