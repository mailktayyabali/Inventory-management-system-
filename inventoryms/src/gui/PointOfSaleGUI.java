package gui;

import Backend.Pointofsale;
import DB.InvoiceDAO;
import Backend.Category;
import Backend.Customer;
import DB.CustomerDAO;
import DB.ProductDAO;
import Backend.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class PointOfSaleGUI extends JPanel {
    private JTextField productIdField, productNameField;
    private JLabel invoiceNumberLabel;
    private JComboBox<String> customerComboBox;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField totalAmountField;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    public PointOfSaleGUI() {
        setLayout(new BorderLayout(10, 10));

        customerDAO = new CustomerDAO();
        productDAO = new ProductDAO();

        // Top panel for invoice details
        JPanel topPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Invoice Details"));
        topPanel.add(new JLabel("Search Product by Id:"));
        productIdField = new JTextField();
        topPanel.add(productIdField);
        topPanel.add(new JLabel("Search Product by Name:"));
        productNameField = new JTextField();
        topPanel.add(productNameField);
        topPanel.add(new JLabel("Invoice #:"));
        invoiceNumberLabel = new JLabel();
        topPanel.add(invoiceNumberLabel);
        topPanel.add(new JLabel("Customer:"));

        // Customer dropdown
        customerComboBox = new JComboBox<>();
        topPanel.add(customerComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for product details and table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Product details table
        productTableModel = new DefaultTableModel(new String[]{"Code", "Product Title", "Description", "Quantity", "Rate", "Amount"}, 0);
        productTable = new JTable(productTableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Products"));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        //non-editable table
        productTable.setDefaultEditor(Object.class, null);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        // Total amount panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("Total Amount:"));
        totalAmountField = new JTextField(10);
        totalAmountField.setEditable(false);
        totalPanel.add(totalAmountField);
        bottomPanel.add(totalPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        JButton newButton = new JButton("New");
        JButton savePreviewButton = new JButton("Save");
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(newButton);
        buttonPanel.add(savePreviewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners for buttons
        newButton.addActionListener(e -> {
            System.out.println("New button clicked");
            clearForm();
        });
        savePreviewButton.addActionListener(new SaveButtonListener());
        searchButton.addActionListener(new SearchButtonListener());
        refreshButton.addActionListener(e -> {
            System.out.println("Refresh button clicked");
            loadCustomer();
        });
        deleteButton.addActionListener(new DeleteButtonListener());

        // Key bindings for Enter key
        productIdField.addActionListener(e -> searchButton.doClick());
        productNameField.addActionListener(e -> searchButton.doClick());

        // Calculate total amount whenever the product table is updated
        productTableModel.addTableModelListener(e -> calculateTotalAmount());

        // Load customers into the dropdown
        loadCustomer();
    }

    private void loadCustomer() {
        customerComboBox.removeAllItems();
        List<Customer> customers = new ArrayList<>();
        try {
            customers = customerDAO.getAllCustomers();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage());
        }
        for (Customer customer : customers) {
            customerComboBox.addItem(customer.getCustomerName());
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (!productIdField.getText().isEmpty()) {
                    Product product = productDAO.getProductById(productIdField.getText());
                    if (product != null) {
                        addProductToTable(product);
                    } else {
                        JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Product not found!");
                    }
                } else if (!productNameField.getText().isEmpty()) {
                    List<Product> products = productDAO.searchProductsByName(productNameField.getText());
                    if (!products.isEmpty()) {
                        Product selectedProduct = (Product) JOptionPane.showInputDialog(
                            PointOfSaleGUI.this,
                            "Select a product:",
                            "Product Selection",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            products.toArray(new Product[0]),
                            products.get(0)
                        );
                        if (selectedProduct != null) {
                            addProductToTable(selectedProduct);
                        }
                    } else {
                        JOptionPane.showMessageDialog(PointOfSaleGUI.this, "No products found!");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Error searching product: " + ex.getMessage());
            }
            productIdField.setText("");
            productNameField.setText("");
        }
    }

    private void addProductToTable(Product product) {
        String quantity = JOptionPane.showInputDialog(
            this,
            "Enter quantity (available: " + product.getStockQuantity() + "):",
            "1"
        );
    
        if (quantity != null) {
            try {
                int qty = Integer.parseInt(quantity);
                if (qty > 0 && qty <= product.getStockQuantity()) {
                    double amount = qty * product.getPrice();
                    productTableModel.addRow(new Object[]{
                        product.getProductID(),
                        product.getName(),
                        product.getDescription(),
                        qty,
                        product.getPrice(),
                        amount
                    });
                    calculateTotalAmount();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid quantity!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity format!");
            }
        }
    }
    
    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Save button clicked");
            try {
                if (productTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Please add products first!");
                    return;
                }
    
                String selectedCustomer = (String) customerComboBox.getSelectedItem();
                if (selectedCustomer == null) {
                    JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Please select a customer!");
                    return;
                }
    
                // Generate invoice number
                String invoiceNumber = generateInvoiceNumber();
                invoiceNumberLabel.setText(invoiceNumber);
                System.out.println("Generated invoice number: " + invoiceNumber);
    
                // Save invoice details
    
                JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Invoice saved successfully!");
                clearForm();
            } catch (SQLException ex) {
                System.err.println("Error saving invoice: " + ex.getMessage());
                JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Error saving invoice: " + ex.getMessage());
            }
        }
    }
    
    private String generateInvoiceNumber() throws SQLException {
        // Get the last invoice number from database and increment
        String invoiceNumber = invoiceDAO.getNextInvoiceNumber();
        System.out.println("Next invoice number: " + invoiceNumber);
        return invoiceNumber;
    }
    
    private void saveInvoice(String invoiceNumber, String customerName) throws SQLException {
        // Save invoice header and details to database
        System.out.println("Saving invoice with number: " + invoiceNumber + " for customer: " + customerName);
        invoiceDAO.saveInvoice(invoiceNumber, customerName, getTotalAmount(), getInvoiceItems());
        System.out.println("Invoice saved successfully in database");
    }
    
    private void clearForm() {
        productTableModel.setRowCount(0);
        totalAmountField.setText("0.00");
        invoiceNumberLabel.setText("");
        System.out.println("Form cleared");
    }
    
    private List<Map<String, Object>> getInvoiceItems() {
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            Map<String, Object> item = new HashMap<>();
            String productId = (String) productTableModel.getValueAt(i, 0);
            if (productId == null || productId.isEmpty()) {
                throw new IllegalArgumentException("Product ID is missing for row: " + i);
            }
            item.put("product_id", productId); // Ensure product_id is set correctly
            item.put("quantity", productTableModel.getValueAt(i, 3));
            item.put("price", productTableModel.getValueAt(i, 4));
            item.put("amount", productTableModel.getValueAt(i, 5));
            items.add(item);
            System.out.println("Invoice item added: " + item);
        }
        return items;
    }
    
    private double getTotalAmount() {
        double totalAmount = 0.0;
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            try {
                double amount = Double.parseDouble(productTableModel.getValueAt(i, 5).toString());
                totalAmount += amount;
            } catch (NumberFormatException | NullPointerException ex) {
                // Ignore invalid or empty values
            }
        }
        System.out.println("Total amount calculated: " + totalAmount);
        return totalAmount;
    }

    

    private void calculateTotalAmount() {
        double totalAmount = 0.0;
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            try {
                double amount = Double.parseDouble(productTableModel.getValueAt(i, 5).toString());
                totalAmount += amount;
            } catch (NumberFormatException | NullPointerException ex) {
                // Ignore invalid or empty values
            }
        }
        totalAmountField.setText(String.format("%.2f", totalAmount));
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Delete button clicked");
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                productTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Please select a product to delete.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Point of Sale");
            frame.setContentPane(new PointOfSaleGUI());
            frame.setSize(1200, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}