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
import java.util.Random;

public class ProductManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, descriptionField, priceField, stockQuantityField;
    private JComboBox<String> categoryComboBox;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ProductManagementGUI() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();

        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Product Name/ID:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        
        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Description", "Price", "Stock Quantity", "Category Name"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        nameField = new JTextField();
        descriptionField = new JTextField();
        priceField = new JTextField();
        stockQuantityField = new JTextField();
        categoryComboBox = new JComboBox<>();

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

        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton clearButton = new JButton("Clear Fields");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(inputPanel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);

        
        searchButton.addActionListener(new SearchButtonListener());
        addButton.addActionListener(new AddButtonListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(new RefreshButtonListener());

        
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                int selectedRow = productTable.getSelectedRow();

                
                String name = tableModel.getValueAt(selectedRow, 1).toString();
                String description = tableModel.getValueAt(selectedRow, 2).toString();
                String price = tableModel.getValueAt(selectedRow, 3).toString();
                String stockQuantity = tableModel.getValueAt(selectedRow, 4).toString();
                String categoryName = tableModel.getValueAt(selectedRow, 5).toString();

                nameField.setText(name);
                descriptionField.setText(description);
                priceField.setText(price);
                stockQuantityField.setText(stockQuantity);
                categoryComboBox.setSelectedItem(categoryName);
            }
        });

        
        loadCategories();

        
        loadProducts();

        
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);
    }

    private void clearFields() {
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

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty() ||
                descriptionField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty() ||
                stockQuantityField.getText().trim().isEmpty() ||
                categoryComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid decimal number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(stockQuantityField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock Quantity must be an integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private String generateSixDigitID() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); 
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
            if (!validateFields()) return;

            try {
                
                String productID = generateSixDigitID();
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stockQuantity = Integer.parseInt(stockQuantityField.getText().trim());
                String categoryName = categoryComboBox.getSelectedItem().toString();

                Product product = new Product(productID, name, description, price, stockQuantity, categoryName);
                productDAO.addProduct(product);

                loadProducts();
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product added successfully!");
                clearFields();
            } catch (SQLException ex) {
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

            if (!validateFields()) return;

            try {
                String productID = tableModel.getValueAt(selectedRow, 0).toString();
                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stockQuantity = Integer.parseInt(stockQuantityField.getText().trim());
                String categoryName = categoryComboBox.getSelectedItem().toString();

                Product product = new Product(productID, name, description, price, stockQuantity, categoryName);
                productDAO.updateProduct(product);

                loadProducts();
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product updated successfully!");
                clearFields();
            } catch (SQLException ex) {
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

    private class RefreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadProducts();
            loadCategories();
            JOptionPane.showMessageDialog(ProductManagementGUI.this, "Data refreshed successfully!");
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
