package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryDashboard {

    private JFrame frame;
    private JTable productTable;
    private JTextField productCodeField, productNameField, dateField, quantityField, costField, sellingField;
    private JComboBox<String> supplierCombo, brandCombo;
    private JButton addButton, editButton, deleteButton, refreshButton, clearButton;
    private String[] columnNames = {"PRODUCTCODE", "PRODUCTNAME", "COSTPRICE", "SELLPRICE", "BRAND"};
    private Object[][] data = {
            {"prod1", "Laptop", 85000.0, 90000.0, "Dell"},
            {"prod2", "Laptop", 70000.0, 72000.0, "HP"},
            {"prod3", "Mobile", 50000.0, 64000.0, "Apple"},
            {"prod4", "Mobile", 50000.0, 51000.0, "Samsung"},
            {"prod5", "Charger", 2000.0, 2100.0, "Dell"},
            {"prod6", "Mouse", 1700.0, 1900.0, "Dell"},
            {"prod7", "Power Adapter", 3000.0, 3500.0, "Dell"},
            {"prod8", "Smart Watch", 15000.0, 17000.0, "Apple"}
    };

    public InventoryDashboard() {
        frame = new JFrame("Warehouse Inventory Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the window

        // Left Navigation Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(8, 1));
        leftPanel.setBackground(new Color(240, 240, 240));

        JButton homeButton = new JButton("Home");
        homeButton.setBackground(new Color(200, 200, 255));
        leftPanel.add(homeButton);

        String[] options = {"Products", "Current Stock", "Customers", "Suppliers", "Sales", "Purchase", "Users"};
        for (String option : options) {
            JButton optionButton = new JButton(option);
            leftPanel.add(optionButton);
        }

        frame.add(leftPanel, BorderLayout.WEST);

        // Center Panel (Content)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Product Table
        productTable = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Search Bar and Refresh Button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JTextField searchField = new JTextField(20);
        topPanel.add(searchField);

        refreshButton = new JButton("Refresh");
        topPanel.add(refreshButton);

        centerPanel.add(topPanel, BorderLayout.NORTH);

        // Right Panel (Product Details Form)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(9, 2));

        JLabel label = new JLabel("Enter Product Details");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        rightPanel.add(label);

        rightPanel.add(new JLabel("Product Code:"));
        productCodeField = new JTextField();
        rightPanel.add(productCodeField);

        rightPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        rightPanel.add(productNameField);

        rightPanel.add(new JLabel("Date:"));
        dateField = new JTextField();
        rightPanel.add(dateField);

        rightPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        rightPanel.add(quantityField);

        rightPanel.add(new JLabel("Cost Price:"));
        costField = new JTextField();
        rightPanel.add(costField);

        rightPanel.add(new JLabel("Selling Price:"));
        sellingField = new JTextField();
        rightPanel.add(sellingField);

        rightPanel.add(new JLabel("Brand:"));
        brandCombo = new JComboBox<>(new String[]{"Dell", "HP", "Apple", "Samsung"});
        rightPanel.add(brandCombo);

        rightPanel.add(new JLabel("Supplier:"));
        supplierCombo = new JComboBox<>(new String[]{"Dell Inc.", "HP Corp.", "Apple Ltd."});
        rightPanel.add(supplierCombo);

        // Buttons
        addButton = new JButton("Add");
        rightPanel.add(addButton);

        editButton = new JButton("Edit");
        rightPanel.add(editButton);

        deleteButton = new JButton("Delete");
        rightPanel.add(deleteButton);

        clearButton = new JButton("Clear");
        rightPanel.add(clearButton);

        centerPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add product logic
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Edit product logic
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete product logic
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear form fields
                productCodeField.setText("");
                productNameField.setText("");
                dateField.setText("");
                quantityField.setText("");
                costField.setText("");
                sellingField.setText("");
            }
        });

        // Set frame visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryDashboard();
            }
        });
    }
}
