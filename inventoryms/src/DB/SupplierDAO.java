package DB;

import Backend.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // Create a new supplier in the database
    public void addSupplier(Supplier supplier) throws SQLException {
        String query = "INSERT INTO suppliers (supplier_id,supplier_name, contact, product, payment_status) VALUES (?, ?, ?, ?,?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, supplier.getSupplierID());
            stmt.setString(2, supplier.getSupplierName());
            stmt.setString(3, supplier.getContact());
            stmt.setString(4, supplier.getProduct());
            stmt.setString(5, supplier.getPaymentStatus());
            stmt.executeUpdate();
        }
    }

    // Retrieve all suppliers from the database
    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers";
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact"),
                        rs.getString("product"),
                        rs.getString("payment_status")
                );
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    // Update an existing supplier in the database
    public void updateSupplier(Supplier supplier) throws SQLException {
        String query = "UPDATE suppliers SET supplier_name = ?, contact = ?, product = ?, payment_status = ? WHERE supplier_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, supplier.getSupplierID());
            stmt.setString(2, supplier.getSupplierName());
            stmt.setString(3, supplier.getContact());
            stmt.setString(4, supplier.getProduct());
            stmt.setString(5, supplier.getPaymentStatus());
            
            stmt.executeUpdate();
        }
    }

    // Delete a supplier from the database
    public void deleteSupplier(int supplierID) throws SQLException {
        String query = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, supplierID);
            stmt.executeUpdate();
        }
    }
}
