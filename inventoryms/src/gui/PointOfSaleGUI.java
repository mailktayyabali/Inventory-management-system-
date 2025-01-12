package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PointOfSaleGUI extends JPanel {
    private JTextField invoiceNumberField, invoiceDateField, customerNameField;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField productCodeField, productTitleField, unitField, descriptionField, quantityField, rateField, amountField;
    private JTextField totalAmountField;

    public PointOfSaleGUI() {
        setLayout(new BorderLayout());

        // Create top panel for invoice details
        JPanel topPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        topPanel.add(new JLabel("Invoice #:"));
        invoiceNumberField = new JTextField();
        topPanel.add(invoiceNumberField);
        topPanel.add(new JLabel("Invoice Date:"));
        invoiceDateField = new JTextField();
        topPanel.add(invoiceDateField);
        topPanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField();
        topPanel.add(customerNameField);
        add(topPanel, BorderLayout.NORTH);

        // Create main panel for product details
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Product details table
        productTableModel = new DefaultTableModel(new String[]{"Code", "Product Title", "Unit", "Description", "Quantity", "Rate", "Amount"}, 0);
        productTable = new JTable(productTableModel);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        mainPanel.add(productScrollPane, BorderLayout.CENTER);
        
        // Product entry fields
        JPanel productEntryPanel = new JPanel(new GridLayout(2, 7, 10, 10));
        productCodeField = new JTextField();
        productTitleField = new JTextField();
        unitField = new JTextField();
        descriptionField = new JTextField();
        quantityField = new JTextField();
        rateField = new JTextField();
        amountField = new JTextField();
        
        productEntryPanel.add(new JLabel("Product ID:"));
        productEntryPanel.add(productCodeField);
        productEntryPanel.add(new JLabel("Product Title:"));
        productEntryPanel.add(productTitleField);
        productEntryPanel.add(new JLabel("Unit:"));
        productEntryPanel.add(unitField);
        productEntryPanel.add(new JLabel("Description:"));
        productEntryPanel.add(descriptionField);
        productEntryPanel.add(new JLabel("Quantity:"));
        productEntryPanel.add(quantityField);
        productEntryPanel.add(new JLabel("Rate:"));
        productEntryPanel.add(rateField);
        productEntryPanel.add(new JLabel("Amount:"));
        productEntryPanel.add(amountField);

        mainPanel.add(productEntryPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Create bottom panel for total amount and action buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Total amount
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("Total Amount:"));
        totalAmountField = new JTextField(10);
        totalPanel.add(totalAmountField);
        bottomPanel.add(totalPanel, BorderLayout.NORTH);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton newButton = new JButton("New");
        JButton savePreviewButton = new JButton("Save Preview");
        JButton searchButton = new JButton("Search");
        JButton printPreviewButton = new JButton("Print Preview");
        JButton refreshButton = new JButton("Refresh");
        JButton exitButton = new JButton("Exit");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(newButton);
        buttonPanel.add(savePreviewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(printPreviewButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(deleteButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        newButton.addActionListener(new NewButtonListener());
        savePreviewButton.addActionListener(new SavePreviewButtonListener());
        searchButton.addActionListener(new SearchButtonListener());
        printPreviewButton.addActionListener(new PrintPreviewButtonListener());
        refreshButton.addActionListener(new RefreshButtonListener());
        exitButton.addActionListener(e -> System.exit(0));
        deleteButton.addActionListener(new DeleteButtonListener());

        // Calculate total amount whenever a product is added or updated
        productTableModel.addTableModelListener(e -> calculateTotalAmount());
    }

    private class NewButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearFields();
        }
    }

    private class SavePreviewButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement the functionality to save and preview the invoice
            JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Save Preview button clicked.");
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement the functionality to search for invoices
            JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Search button clicked.");
        }
    }

    private class PrintPreviewButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement the functionality to print preview the invoice
            JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Print Preview button clicked.");
        }
    }

    private class RefreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearFields();
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                productTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Please select a product to delete.");
            }
        }
    }

    private void clearFields() {
        invoiceNumberField.setText("");
        invoiceDateField.setText("");
        customerNameField.setText("");
        productCodeField.setText("");
        productTitleField.setText("");
        unitField.setText("");
        descriptionField.setText("");
        quantityField.setText("");
        rateField.setText("");
        amountField.setText("");
        totalAmountField.setText("");
        productTableModel.setRowCount(0);
    }

    private void calculateTotalAmount() {
        double totalAmount = 0.0;
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            double amount = (double) productTableModel.getValueAt(i, 6);
            totalAmount += amount;
        }
        totalAmountField.setText(String.valueOf(totalAmount));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Point of Sale");
            frame.setContentPane(new PointOfSaleGUI());
            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
