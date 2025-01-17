package gui;

import Backend.Purchase;
import DB.PurchaseDAO;
import DB.SupplierDAO;
import Backend.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;

public class PurchaseManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable purchaseTable;
    private DefaultTableModel purchaseTableModel;
    private JTextField purchaseIdField, productNameField, quantityField, purchasePriceField, salePriceField;
    private JComboBox<String> supplierComboBox; // Supplier dropdown
    private List<Supplier> suppliers; // To store Supplier objects

    private PurchaseDAO purchaseDAO = new PurchaseDAO();
    private SupplierDAO supplierDAO = new SupplierDAO();

    public PurchaseManagementGUI() {
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Product Name:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table inside a scroll pane
        purchaseTableModel = new DefaultTableModel(new String[]{"Puchase ID", "Product Name", "Quantity", "Purchase Price", "Sale Price", "Supplier Name"}, 0);
        purchaseTable = new JTable(purchaseTableModel);
        JScrollPane tableScrollPane = new JScrollPane(purchaseTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        purchaseIdField = new JTextField();
        productNameField = new JTextField();
        quantityField = new JTextField();
        purchasePriceField = new JTextField();
        salePriceField = new JTextField();
        supplierComboBox = new JComboBox<>(); // Supplier dropdown

        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Purchase Price:"));
        inputPanel.add(purchasePriceField);
        inputPanel.add(new JLabel("Sale Price:"));
        inputPanel.add(salePriceField);
        inputPanel.add(new JLabel("Supplier Name:"));
        inputPanel.add(supplierComboBox);

        // Load supplier data into the combo box
        loadSuppliers();
        loadPurchases();

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JButton addButton = new JButton("Add Purchase");
        JButton editButton = new JButton("Edit Purchase");
        JButton deleteButton = new JButton("Delete Purchase");
        JButton clearButton = new JButton("Clear Fields");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);

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
        refreshButton.addActionListener(new refreshButtonListener());

        // Add row selection listener to populate fields when selecting a row
        purchaseTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = purchaseTable.getSelectedRow();
                if (row != -1) {
                    // Fill the input fields with the data from the selected row
                    purchaseIdField.setText(purchaseTableModel.getValueAt(row, 0).toString());
                    productNameField.setText(purchaseTableModel.getValueAt(row, 1).toString());
                    quantityField.setText(purchaseTableModel.getValueAt(row, 2).toString());
                    purchasePriceField.setText(purchaseTableModel.getValueAt(row, 3).toString());
                    salePriceField.setText(purchaseTableModel.getValueAt(row, 4).toString());
                    supplierComboBox.setSelectedItem(purchaseTableModel.getValueAt(row, 5).toString());
                }
            }
        });

        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);

        // Load purchases into the table
        loadPurchases();
    }

    private void loadSuppliers() {
        try {
            suppliers = supplierDAO.getAllSuppliers(); // Get all suppliers as objects
            supplierComboBox.removeAllItems(); // Clear any existing items
            for (Supplier supplier : suppliers) {
                supplierComboBox.addItem(supplier.getSupplierName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPurchases() {
        try {
            purchaseTableModel.setRowCount(0); // Clear the table
            List<Purchase> purchases = purchaseDAO.getAllPurchaseItems();
            for (Purchase purchase : purchases) {
                purchaseTableModel.addRow(new Object[]{
                        purchase.getPurchaseID(),
                        purchase.getProductName(),
                        purchase.getQuantity(),
                        purchase.getPurchasePrice(),
                        purchase.getSalePrice(),
                        purchase.getSupplierName() // Fetch supplier name from supplierID
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading purchases: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        purchaseIdField.setText("");
        productNameField.setText("");
        quantityField.setText("");
        purchasePriceField.setText("");
        salePriceField.setText("");
        supplierComboBox.setSelectedIndex(0);
    }

    private boolean isFieldsEmpty() {
        return productNameField.getText().trim().isEmpty() ||
                quantityField.getText().trim().isEmpty() ||
                purchasePriceField.getText().trim().isEmpty() ||
                salePriceField.getText().trim().isEmpty() ||
                supplierComboBox.getSelectedIndex() == -1;
    }
    private class refreshButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            loadPurchases();
            loadSuppliers();
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < purchaseTableModel.getRowCount(); i++) {
                String productName = purchaseTableModel.getValueAt(i, 1).toString().toLowerCase(); // Searching in the Product Name column
                if (productName.contains(searchTerm)) {
                    purchaseTable.setRowSelectionInterval(i, i);
                    break;
                } else {
                    purchaseTable.clearSelection();
                }
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Validate that fields are not empty
                if (isFieldsEmpty()) {
                    JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Generate a 6-digit purchase ID
                String purchaseID = generateSixDigitID();

                // Get purchase details from input fields
                String productName = productNameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double purchasePrice = Double.parseDouble(purchasePriceField.getText());
                double salePrice = Double.parseDouble(salePriceField.getText());
                String supplierName = suppliers.get(supplierComboBox.getSelectedIndex()).getSupplierName();

                // Create purchase object and add to the database
                Purchase purchase = new Purchase(purchaseID, productName, quantity, purchasePrice, salePrice, supplierName);
                purchaseDAO.addPurchase(purchase);

                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Purchase added successfully!");
                loadPurchases();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Error adding purchase: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Generate 6-digit purchase ID using random number
        private String generateSixDigitID() {
            Random random = new Random();
            return String.format("%06d", random.nextInt(1000000)); // Generate a number between 000000 and 999999
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = purchaseTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get purchase details from input fields
                    String purchaseID = purchaseIdField.getText(); // Fetch from text field
                    String productName = productNameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double purchasePrice = Double.parseDouble(purchasePriceField.getText());
                    double salePrice = Double.parseDouble(salePriceField.getText());
                    String supplierName = suppliers.get(supplierComboBox.getSelectedIndex()).getSupplierName();
    
                    // Update the purchase in the database
                    Purchase purchase = new Purchase(purchaseID, productName, quantity, purchasePrice, salePrice, supplierName);
                    purchaseDAO.updatePurchase(purchase);
    
                    JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Purchase updated successfully!");
                    loadPurchases(); // Refresh table data
                    clearFields();   // Clear input fields
                } else {
                    JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Please select a purchase to edit.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Error updating purchase: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = purchaseTable.getSelectedRow();
                if (selectedRow != -1) {
                    String purchaseID = (String) purchaseTableModel.getValueAt(selectedRow, 0); // Correct column for purchaseID
                    purchaseDAO.deletePurchase(purchaseID);

                    JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Purchase deleted successfully!");
                    loadPurchases();
                } else {
                    JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Please select a purchase to delete.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Error deleting purchase: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Purchase Management");
            frame.setContentPane(new PurchaseManagementGUI());
            frame.setSize(900, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
