package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductPanel {

    private JFrame frame;
    private JTable productTable;
    private JTextField searchField, productCodeField, productNameField, dateField, quantityField, costPriceField, sellPriceField;
    private JComboBox<String> supplierComboBox, brandComboBox;
    private JButton addButton, editButton, deleteButton, clearButton, refreshButton;

    private DefaultTableModel tableModel;

    public ProductPanel() {
        // Main Frame
        frame = new JFrame("Products");
        frame.setSize(900, 500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main Content Panel (Top-Level Container)
        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        // 1. TOP Panel - Search Bar and Refresh Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(15);
        refreshButton = new JButton("REFRESH");

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(refreshButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 2. CENTER Panel - Table to Display Products
        String[] columnNames = {"PRODUCTCODE", "PRODUCTNAME", "COSTPRICE", "SELLPRICE", "BRAND"};
        Object[][] data = {
                {"prod1", "Laptop", 85000.0, 90000.0, "Dell"},
                {"prod2", "Laptop", 70000.0, 72000.0, "HP"},
                {"prod3", "Mobile", 50000.0, 64000.0, "Apple"},
                {"prod4", "Mobile", 50000.0, 51000.0, "Samsung"},
                {"prod5", "Charger", 2000.0, 2100.0, "Apple"},
                {"prod6", "Mouse", 1700.0, 1900.0, "Dell"},
                {"prod7", "Power Adapter", 3000.0, 3500.0, "Dell"},
                {"prod8", "Smart Watch", 15000.0, 17000.0, "Apple"}
        };

        tableModel = new DefaultTableModel(data, columnNames);
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setPreferredSize(new Dimension(500, 400));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // 3. RIGHT Panel - Product Details Form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setPreferredSize(new Dimension(350, 400));

        JLabel supplierLabel = new JLabel("Supplier:");
        supplierComboBox = new JComboBox<>(new String[]{"Dell Inc.", "HP Corp.", "Apple Ltd.", "Samsung Ltd."});
        JButton addSupplierButton = new JButton("Click to add a New Supplier");

        JLabel productCodeLabel = new JLabel("Product Code:");
        productCodeField = new JTextField();

        JLabel productNameLabel = new JLabel("Product Name:");
        productNameField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        dateField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        JLabel costPriceLabel = new JLabel("Cost Price:");
        costPriceField = new JTextField();

        JLabel sellPriceLabel = new JLabel("Selling Price:");
        sellPriceField = new JTextField();

        JLabel brandLabel = new JLabel("Brand:");
        brandComboBox = new JComboBox<>(new String[]{"Dell", "HP", "Apple", "Samsung"});

        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("CLEAR");

        // Positioning Labels and Fields
        supplierLabel.setBounds(10, 10, 100, 20);
        supplierComboBox.setBounds(120, 10, 200, 20);
        addSupplierButton.setBounds(10, 40, 310, 25);

        productCodeLabel.setBounds(10, 80, 100, 20);
        productCodeField.setBounds(120, 80, 200, 20);

        productNameLabel.setBounds(10, 110, 100, 20);
        productNameField.setBounds(120, 110, 200, 20);

        dateLabel.setBounds(10, 140, 100, 20);
        dateField.setBounds(120, 140, 200, 20);

        quantityLabel.setBounds(10, 170, 100, 20);
        quantityField.setBounds(120, 170, 200, 20);

        costPriceLabel.setBounds(10, 200, 100, 20);
        costPriceField.setBounds(120, 200, 200, 20);

        sellPriceLabel.setBounds(10, 230, 100, 20);
        sellPriceField.setBounds(120, 230, 200, 20);

        brandLabel.setBounds(10, 260, 100, 20);
        brandComboBox.setBounds(120, 260, 200, 20);

        addButton.setBounds(10, 300, 70, 25);
        editButton.setBounds(90, 300, 70, 25);
        deleteButton.setBounds(170, 300, 70, 25);
        clearButton.setBounds(250, 300, 80, 25);

        // Add Components to Right Panel
        rightPanel.add(supplierLabel);
        rightPanel.add(supplierComboBox);
        rightPanel.add(addSupplierButton);
        rightPanel.add(productCodeLabel);
        rightPanel.add(productCodeField);
        rightPanel.add(productNameLabel);
        rightPanel.add(productNameField);
        rightPanel.add(dateLabel);
        rightPanel.add(dateField);
        rightPanel.add(quantityLabel);
        rightPanel.add(quantityField);
        rightPanel.add(costPriceLabel);
        rightPanel.add(costPriceField);
        rightPanel.add(sellPriceLabel);
        rightPanel.add(sellPriceField);
        rightPanel.add(brandLabel);
        rightPanel.add(brandComboBox);
        rightPanel.add(addButton);
        rightPanel.add(editButton);
        rightPanel.add(deleteButton);
        rightPanel.add(clearButton);

        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Add Action Listeners for Buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Show Frame
        frame.setVisible(true);
    }

    // Add Product to Table
    private void addProduct() {
        String productCode = productCodeField.getText();
        String productName = productNameField.getText();
        String costPrice = costPriceField.getText();
        String sellPrice = sellPriceField.getText();
        String brand = (String) brandComboBox.getSelectedItem();

        tableModel.addRow(new Object[]{productCode, productName, costPrice, sellPrice, brand});
        clearForm();
    }

    // Edit Selected Product in Table
    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.setValueAt(productCodeField.getText(), selectedRow, 0);
            tableModel.setValueAt(productNameField.getText(), selectedRow, 1);
            tableModel.setValueAt(costPriceField.getText(), selectedRow, 2);
            tableModel.setValueAt(sellPriceField.getText(), selectedRow, 3);
            tableModel.setValueAt(brandComboBox.getSelectedItem(), selectedRow, 4);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a product to edit!");
        }
    }

    // Delete Selected Product from Table
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a product to delete!");
        }
    }

    // Clear Form Fields
    private void clearForm() {
        productCodeField.setText("");
        productNameField.setText("");
        dateField.setText("");
        quantityField.setText("");
        costPriceField.setText("");
        sellPriceField.setText("");
        brandComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductPanel());
    }
}
