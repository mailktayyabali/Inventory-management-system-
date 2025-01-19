package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                    rs.getString("name"),
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
        String query = "INSERT INTO Invoice (invoice_number, customer_id, product_id, quantity, rate, amount, total_amount) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, invoice.getInvoiceNumber());
            stmt.setInt(2, invoice.getCustomerId());
            stmt.setString(3, invoice.getProductId());
            stmt.setInt(4, invoice.getQuantity());
            stmt.setDouble(5, invoice.getRate());
            stmt.setDouble(6, invoice.getAmount());
            stmt.setDouble(7, invoice.getTotalAmount());
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

    // Get the next invoice number
    public String getNextInvoiceNumber() throws SQLException {
        String query = "SELECT MAX(invoice_number) AS last_invoice_number FROM Invoice";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String lastInvoiceNumber = rs.getString("last_invoice_number");
                if (lastInvoiceNumber != null) {
                    int nextInvoiceNumber = Integer.parseInt(lastInvoiceNumber) + 1;
                    return String.format("%05d", nextInvoiceNumber);
                }
            }
        }
        return "00001"; // Default to 00001 if no invoices exist
    }

    public void saveInvoice(String invoiceNumber, String customerName, double totalAmount, List<Map<String, Object>> items) throws SQLException {
        String insertInvoiceQuery = "INSERT INTO Invoice (invoice_id, invoice_number, customer_id, customer_name, total_amount, invoice_date) VALUES (?, ?, ?, ?, ?, ?)";
        String insertInvoiceItemQuery = "INSERT INTO InvoiceItem (invoice_number, product_id, quantity, price, amount,name) VALUES (?,?, ?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            int customerId = getCustomerIdByName(customerName, conn);
            System.out.println("Customer ID: " + customerId);

            try (PreparedStatement invoiceStmt = conn.prepareStatement(insertInvoiceQuery, Statement.RETURN_GENERATED_KEYS)) {
                invoiceStmt.setNull(1, Types.INTEGER); // Assuming invoice_id is auto-incremented
                invoiceStmt.setString(2, invoiceNumber);
                invoiceStmt.setInt(3, customerId);
                invoiceStmt.setString(4, customerName);
                invoiceStmt.setDouble(5, totalAmount);
                invoiceStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis())); // Set current timestamp
                invoiceStmt.executeUpdate();
                System.out.println("Invoice saved with number: " + invoiceNumber);

                try (ResultSet generatedKeys = invoiceStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int invoiceId = generatedKeys.getInt(1);
                        System.out.println("Generated invoice ID: " + invoiceId);
                    } else {
                        throw new SQLException("Creating invoice failed, no ID obtained.");
                    }
                }
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(insertInvoiceItemQuery)) {
                for (Map<String, Object> item : items) {
                    System.out.println("Adding item: " + item);
                    String productName = (String) item.get("name");
                    int productId = getProductIdByName(productName, conn);
                    if (productId == 0) {
                        throw new SQLException("Product ID not found for product: " + productName);
                    }
                    itemStmt.setString(1, invoiceNumber);
                    itemStmt.setInt(2, productId); // Ensure product_id is set correctly
                    itemStmt.setInt(3, (Integer) item.get("quantity"));
                    itemStmt.setDouble(4, (Double) item.get("price"));
                    itemStmt.setDouble(5, (Double) item.get("amount"));
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
                System.out.println("Invoice items saved for invoice number: " + invoiceNumber);
            }

            conn.commit(); // Commit transaction
            System.out.println("Transaction committed for invoice number: " + invoiceNumber);
        } catch (SQLException e) {
            System.err.println("Error saving invoice: " + e.getMessage());
            try (Connection conn = DBconnection.getConnection()) {
                conn.setAutoCommit(false); // Ensure auto-commit is false before rollback
                conn.rollback(); // Rollback transaction on error
                System.err.println("Transaction rolled back for invoice number: " + invoiceNumber);
            }
            throw e;
        }
    }

    private int getProductIdByName(String productName, Connection conn) throws SQLException {
        String query = "SELECT product_id FROM products WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, productName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("product_id");
                } else {
                    return 0; // Product not found
                }
            }
        }
    }

    private int getCustomerIdByName(String customerName, Connection conn) throws SQLException {
        String query = "SELECT customer_id FROM customers WHERE customer_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("customer_id");
                } else {
                    throw new SQLException("Customer not found: " + customerName);
                }
            }
        }
    }

    // Get the total amount for an invoice
    public double getTotalAmount(int invoiceId) throws SQLException {
        String query = "SELECT SUM(amount) AS total_amount FROM Invoice WHERE invoice_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_amount");
                }
            }
        }
        return 0.0;
    }

    public Pointofsale searchProduct(String query) throws SQLException {
        String sql = "SELECT p.product_id, p.name AS name, p.description AS product_description, p.rate " +
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
                        rs.getString("name"), // productName
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