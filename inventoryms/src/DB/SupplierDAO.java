package DB;

import Backend.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // Create a new supplier in the database
    public void addSupplier(Supplier supplier) throws SQLException {
        String query = "INSERT INTO suppliers (supplier_id,supplier_name, contact, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, supplier.getSupplierID());
            stmt.setString(2, supplier.getSupplierName());
            stmt.setString(3, supplier.getContact());
            stmt.setString(4, supplier.getAddress());
            
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
                        rs.getString("address")
                       
                );
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    // Update an existing supplier in the database
    public void updateSupplier(Supplier supplier) throws SQLException {
        String query = "UPDATE suppliers SET supplier_name = ?, contact = ?, address = ? WHERE supplier_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
                 stmt.setString(1, supplier.getSupplierName());
                 stmt.setString(2, supplier.getContact());
                 stmt.setString(3, supplier.getAddress());
                 stmt.setInt(4, supplier.getSupplierID());
            
            
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
