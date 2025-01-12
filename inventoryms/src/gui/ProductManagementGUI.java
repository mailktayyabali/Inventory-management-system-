package gui;

import Backend.Product;
import Backend.Category;
import DB.ProductDAO;
import DB.CategoryDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ProductManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField productIDField, nameField, descriptionField, priceField, stockQuantityField;
    private JComboBox<String> categoryComboBox;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ProductManagementGUI() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Product Name/ID:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table
        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Description", "Price", "Stock Quantity", "Category Name"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        productIDField = new JTextField();
        nameField = new JTextField();
        descriptionField = new JTextField();
        priceField = new JTextField();
        stockQuantityField = new JTextField();
        categoryComboBox = new JComboBox<>();

        inputPanel.add(new JLabel("Product ID:")); 
        inputPanel.add(productIDField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Stock Quantity:"));
        inputPanel.add(stockQuantityField);
        inputPanel.add(new JLabel("Category Name:"));
        inputPanel.add(categoryComboBox);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton clearButton = new JButton("Clear Fields");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Create a panel that combines input fields and buttons, and make it scrollable
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(inputPanel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);

        // Add action listeners
        searchButton.addActionListener(new SearchButtonListener());
        addButton.addActionListener(new AddButtonListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        clearButton.addActionListener(e -> clearFields());

        // Load categories dynamically
        loadCategories();

        // Load products dynamically
        loadProducts();

        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);
    }

    private void clearFields() {
        productIDField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        stockQuantityField.setText("");
        categoryComboBox.setSelectedIndex(0);
    }

    private void loadCategories() {
        try {
            categoryComboBox.removeAllItems();
            List<Category> categories = categoryDAO.getAllCategories();
            for (Category category : categories) {
                categoryComboBox.addItem(category.getCategoryName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadProducts() {
        try {
            tableModel.setRowCount(0);
            List<Product> products = productDAO.getAllProducts();
            for (Product product : products) {
                tableModel.addRow(new Object[]{
                        product.getProductID(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        product.getCategoryName()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            boolean found = false;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productID = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String productName = tableModel.getValueAt(i, 1).toString().toLowerCase();

                if (productID.contains(searchTerm) || productName.contains(searchTerm)) {
                    productTable.setRowSelectionInterval(i, i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "No matching product found.");
                productTable.clearSelection();
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String productID = productIDField.getText().trim();
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stockQuantity = Integer.parseInt(stockQuantityField.getText().trim());
                String categoryName = categoryComboBox.getSelectedItem().toString();
    
                if (productID.isEmpty() || name.isEmpty() || description.isEmpty() || categoryName.isEmpty()) {
                    JOptionPane.showMessageDialog(ProductManagementGUI.this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                // Debugging log
                System.out.println("Adding Product: " + productID + ", " + name);
    
                Product product = new Product(productID, name, description, price, stockQuantity, categoryName);
                productDAO.addProduct(product);
    
                // Debugging log
                System.out.println("Product Added Successfully!");
    
                loadProducts();  // Reload the products
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product added successfully!");
                clearFields();
            } catch (SQLException | NumberFormatException ex) {
                // Debugging log
                System.out.println("Error: " + ex.getMessage());
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Please select a product to edit.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String productID = productIDField.getText().trim();
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stockQuantity = Integer.parseInt(stockQuantityField.getText().trim());
                String categoryName = categoryComboBox.getSelectedItem().toString();

                if (productID.isEmpty() || name.isEmpty() || description.isEmpty() || categoryName.isEmpty()) {
                    JOptionPane.showMessageDialog(ProductManagementGUI.this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Product product = new Product(productID, name, description, price, stockQuantity, categoryName);
                productDAO.updateProduct(product);
                loadProducts();
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product updated successfully!");
                clearFields();
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String productID = tableModel.getValueAt(selectedRow, 0).toString();
            try {
                productDAO.deleteProduct(productID);
                loadProducts();
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Product Management");
            frame.setContentPane(new ProductManagementGUI());
            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
