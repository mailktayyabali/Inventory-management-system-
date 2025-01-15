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
        setLayout(new BorderLayout(10, 10));

        // Top panel for invoice details
        JPanel topPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Invoice Details"));
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

        // Center panel for product details and table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Product details table
        productTableModel = new DefaultTableModel(new String[]{"Code", "Product Title", "Unit", "Description", "Quantity", "Rate", "Amount"}, 0);
        productTable = new JTable(productTableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Products"));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Product entry panel
        JPanel productEntryPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        productEntryPanel.setBorder(BorderFactory.createTitledBorder("Product Entry"));
        productEntryPanel.add(new JLabel("Product Code:"));
        productCodeField = new JTextField();
        productEntryPanel.add(productCodeField);
        productEntryPanel.add(new JLabel("Product Title:"));
        productTitleField = new JTextField();
        productEntryPanel.add(productTitleField);
        productEntryPanel.add(new JLabel("Unit:"));
        unitField = new JTextField();
        productEntryPanel.add(unitField);
        productEntryPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        productEntryPanel.add(descriptionField);
        productEntryPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        productEntryPanel.add(quantityField);
        productEntryPanel.add(new JLabel("Rate:"));
        rateField = new JTextField();
        productEntryPanel.add(rateField);
        productEntryPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        productEntryPanel.add(amountField);
        centerPanel.add(productEntryPanel, BorderLayout.EAST);

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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 7, 10, 10));
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

        // Action listeners for buttons
        newButton.addActionListener(new NewButtonListener());
        savePreviewButton.addActionListener(new SavePreviewButtonListener());
        searchButton.addActionListener(new SearchButtonListener());
        printPreviewButton.addActionListener(new PrintPreviewButtonListener());
        refreshButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> System.exit(0));
        deleteButton.addActionListener(new DeleteButtonListener());

        // Calculate total amount whenever the product table is updated
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
            JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Save Preview button clicked.");
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Search button clicked.");
        }
    }

    private class PrintPreviewButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(PointOfSaleGUI.this, "Print Preview button clicked.");
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
            try {
                double amount = Double.parseDouble(productTableModel.getValueAt(i, 6).toString());
                totalAmount += amount;
            } catch (NumberFormatException | NullPointerException ex) {
                // Ignore invalid or empty amount values
            }
        }
        totalAmountField.setText(String.format("%.2f", totalAmount));
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
