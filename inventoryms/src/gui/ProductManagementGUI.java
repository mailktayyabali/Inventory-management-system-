package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductManagementGUI extends JPanel { // Extending JPanel instead of JFrame
    private JTextField searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField productIDField, nameField, descriptionField, priceField, stockQuantityField, categoryIDField;

    public ProductManagementGUI() {
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Category:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table
        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Description", "Price", "Stock Quantity", "Category ID"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        productIDField = new JTextField();
        nameField = new JTextField();
        descriptionField = new JTextField();
        priceField = new JTextField();
        stockQuantityField = new JTextField();
        categoryIDField = new JTextField();

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
        inputPanel.add(new JLabel("Category ID:"));
        inputPanel.add(categoryIDField);

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
        categoryIDField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Placeholder for search functionality
            JOptionPane.showMessageDialog(ProductManagementGUI.this, "Search functionality coming soon!");
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get product details from input fields
            String productID = productIDField.getText();
            String name = nameField.getText();
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stockQuantity = Integer.parseInt(stockQuantityField.getText());
            String categoryID = categoryIDField.getText();

            // Add product details to the table
            tableModel.addRow(new Object[]{productID, name, description, price, stockQuantity, categoryID});
            JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product added successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                String productID = productIDField.getText();
                String name = nameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stockQuantity = Integer.parseInt(stockQuantityField.getText());
                String categoryID = categoryIDField.getText();

                tableModel.setValueAt(productID, selectedRow, 0);
                tableModel.setValueAt(name, selectedRow, 1);
                tableModel.setValueAt(description, selectedRow, 2);
                tableModel.setValueAt(price, selectedRow, 3);
                tableModel.setValueAt(stockQuantity, selectedRow, 4);
                tableModel.setValueAt(categoryID, selectedRow, 5);

                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Please select a product to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Product deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(ProductManagementGUI.this, "Please select a product to delete.");
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
