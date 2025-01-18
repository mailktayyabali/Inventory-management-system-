package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Backend.Pointofsale;

public class InvoiceDAO {

    // Retrieve a single invoice by ID
    public Pointofsale getInvoiceById(int invoiceId) throws SQLException {
        String query = "SELECT i.*, p.name AS product_name, p.description AS product_description, c.customer_id, c.customer_name " +
                       "FROM Invoice i " +
                       "JOIN products p ON i.product_id = p.product_id " +
                       "JOIN customers c ON i.customer_id = c.customer_id " +
                       "WHERE i.invoice_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pointofsale(
                        rs.getInt("invoice_id"),
                        rs.getString("invoice_number"),
                        rs.getTimestamp("invoice_date").toLocalDateTime(),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getInt("quantity"),
                        rs.getDouble("rate"),
                        rs.getDouble("amount"),
                        rs.getDouble("total_amount")
                    );
                }
            }
        }
        return null;
    }

    // Retrieve all invoices
    public List<Pointofsale> getAllInvoices() throws SQLException {
        List<Pointofsale> invoices = new ArrayList<>();
        String query = "SELECT i.*, p.name AS product_name, p.description AS product_description, c.customer_id, c.customer_name " +
                       "FROM Invoice i " +
                       "JOIN products p ON i.product_id = p.product_id " +
                       "JOIN customers c ON i.customer_id = c.customer_id";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                invoices.add(new Pointofsale(
                    rs.getInt("invoice_id"),
                    rs.getString("invoice_number"),
                    rs.getTimestamp("invoice_date").toLocalDateTime(),
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getString("product_id"),
                    rs.getString("product_name"),
                    rs.getString("product_description"),
                    rs.getInt("quantity"),
                    rs.getDouble("rate"),
                    rs.getDouble("amount"),
                    rs.getDouble("total_amount")
                ));
            }
        }
        return invoices;
    }

    // Add a new invoice
    public void addInvoice(Pointofsale invoice) throws SQLException {
        String query = "INSERT INTO Invoice (invoice_number, invoice_date, customer_id, product_id, quantity, rate, amount, total_amount) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, invoice.getInvoiceNumber());
            stmt.setTimestamp(2, Timestamp.valueOf(invoice.getInvoiceDate()));
            stmt.setInt(3, invoice.getCustomerId());
            stmt.setString(4, invoice.getProductId());
            stmt.setInt(5, invoice.getQuantity());
            stmt.setDouble(6, invoice.getRate());
            stmt.setDouble(7, invoice.getAmount());
            stmt.setDouble(8, invoice.getTotalAmount());
            stmt.executeUpdate();
        }
    }

    // Update an existing invoice
    public void updateInvoice(Pointofsale invoice) throws SQLException {
        String query = "UPDATE Invoice SET invoice_number = ?, invoice_date = ?, customer_id = ?, product_id = ?, quantity = ?, rate = ?, amount = ?, total_amount = ? " +
                       "WHERE invoice_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, invoice.getInvoiceNumber());
            stmt.setTimestamp(2, Timestamp.valueOf(invoice.getInvoiceDate()));
            stmt.setInt(3, invoice.getCustomerId());
            stmt.setString(4, invoice.getProductId());
            stmt.setInt(5, invoice.getQuantity());
            stmt.setDouble(6, invoice.getRate());
            stmt.setDouble(7, invoice.getAmount());
            stmt.setDouble(8, invoice.getTotalAmount());
            stmt.setInt(9, invoice.getInvoiceId());
            stmt.executeUpdate();
        }
    }

    // Delete an invoice
    public void deleteInvoice(int invoiceId) throws SQLException {
        String query = "DELETE FROM Invoice WHERE invoice_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, invoiceId);
            stmt.executeUpdate();
        }
    }
    public Pointofsale searchProduct(String query) throws SQLException {
        String sql = "SELECT p.product_id, p.name AS product_name, p.description AS product_description, p.rate " +
                     "FROM products p " +
                     "WHERE p.product_id = ? OR p.name LIKE ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, query); // Exact match for product ID
            stmt.setString(2, "%" + query + "%"); // Partial match for product name
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pointofsale(
                        0, // invoiceId (not needed here)
                        null, // invoiceNumber
                        null, // invoiceDate
                        0, // customerId
                        null, // customerName
                        rs.getString("product_id"), // productId
                        rs.getString("product_name"), // productName
                        rs.getString("product_description"), // productDescription
                        0, // quantity (not needed)
                        rs.getDouble("rate"), // rate
                        0.0, // amount (not needed)
                        0.0 // totalAmount (not needed)
                    );
                }
            }
        }
        return null; // Return null if no product is found
    }
}