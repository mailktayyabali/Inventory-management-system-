package DB;

import Backend.Purchase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {

    
    public List<Purchase> getAllPurchaseItems() throws SQLException {
        List<Purchase> purchaseItems = new ArrayList<>();
        String query = "SELECT pi.purchase_id, pi.product_name, pi.quantity, pi.purchase_price, pi.sale_price, s.supplier_name " +
                       "FROM purchase_items pi JOIN suppliers s ON pi.supplier_id = s.supplier_id";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                purchaseItems.add(new Purchase(
                        rs.getString("purchase_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("purchase_price"),
                        rs.getDouble("sale_price"),
                        rs.getString("supplier_name")
                ));
            }
        }
        return purchaseItems;
    }

    
    public void addPurchase(Purchase purchaseItem) throws SQLException {
        String query = "INSERT INTO purchase_items (purchase_id,product_name, quantity, purchase_price, sale_price, supplier_id) " +
                       "VALUES (?,?, ?, ?, ?, (SELECT supplier_id FROM suppliers WHERE supplier_name = ?))";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, purchaseItem.getPurchaseID());
            stmt.setString(2, purchaseItem.getProductName());
            stmt.setInt(3, purchaseItem.getQuantity());
            stmt.setDouble(4, purchaseItem.getPurchasePrice());
            stmt.setDouble(5, purchaseItem.getSalePrice());
            stmt.setString(6, purchaseItem.getSupplierName());  
            stmt.executeUpdate();
        }
    }

    
    public void updatePurchase(Purchase purchaseItem) throws SQLException {
        String query = "UPDATE purchase_items SET product_name = ?, quantity = ?, purchase_price = ?, sale_price = ?, " +
                       "supplier_id = (SELECT supplier_id FROM suppliers WHERE supplier_name = ?) " +
                       "WHERE purchase_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, purchaseItem.getProductName());
            stmt.setInt(2, purchaseItem.getQuantity());
            stmt.setDouble(3, purchaseItem.getPurchasePrice());
            stmt.setDouble(4, purchaseItem.getSalePrice());
            stmt.setString(5, purchaseItem.getSupplierName());
            stmt.setString(6, purchaseItem.getPurchaseID());
            stmt.executeUpdate();
        }
    }

    
    public void deletePurchase(String purchaseID) throws SQLException {
        String query = "DELETE FROM purchase_items WHERE purchase_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, purchaseID);
            stmt.executeUpdate();
        }
    }
    public void updateProductQuantity(String productID, int quantitySold) throws SQLException {
        String query = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, quantitySold);  // Quantity sold to be subtracted from stock
            stmt.setString(2, productID);  // Product ID for the product whose stock needs to be updated

            // Execute the update query
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Failed to update product quantity. Product not found or insufficient stock.");
            }
        }
    }

}
