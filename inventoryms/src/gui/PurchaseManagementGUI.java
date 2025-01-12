package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PurchaseManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable purchaseTable;
    private DefaultTableModel purchaseTableModel;
    private JTextField productNameField, quantityField, purchasePriceField, salePriceField;

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
        purchaseTableModel = new DefaultTableModel(new String[]{"Product Name", "Quantity", "Purchase Price", "Sale Price"}, 0);
        purchaseTable = new JTable(purchaseTableModel);
        JScrollPane tableScrollPane = new JScrollPane(purchaseTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        productNameField = new JTextField();
        quantityField = new JTextField();
        purchasePriceField = new JTextField();
        salePriceField = new JTextField();

        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Purchase Price:"));
        inputPanel.add(purchasePriceField);
        inputPanel.add(new JLabel("Sale Price:"));
        inputPanel.add(salePriceField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Purchase");
        JButton editButton = new JButton("Edit Purchase");
        JButton deleteButton = new JButton("Delete Purchase");
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
        productNameField.setText("");
        quantityField.setText("");
        purchasePriceField.setText("");
        salePriceField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < purchaseTableModel.getRowCount(); i++) {
                String productName = purchaseTableModel.getValueAt(i, 0).toString().toLowerCase();
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
            // Get purchase details from input fields
            String productName = productNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double purchasePrice = Double.parseDouble(purchasePriceField.getText());
            double salePrice = Double.parseDouble(salePriceField.getText());

            // Add purchase details to the table
            purchaseTableModel.addRow(new Object[]{productName, quantity, purchasePrice, salePrice});
            JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Purchase added successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = purchaseTable.getSelectedRow();
            if (selectedRow != -1) {
                // Update purchase details in the table
                purchaseTableModel.setValueAt(productNameField.getText(), selectedRow, 0);
                purchaseTableModel.setValueAt(Integer.parseInt(quantityField.getText()), selectedRow, 1);
                purchaseTableModel.setValueAt(Double.parseDouble(purchasePriceField.getText()), selectedRow, 2);
                purchaseTableModel.setValueAt(Double.parseDouble(salePriceField.getText()), selectedRow, 3);

                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Purchase updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Please select a purchase to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = purchaseTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove purchase from the table
                purchaseTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Purchase deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(PurchaseManagementGUI.this, "Please select a purchase to delete.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Purchase Management");
            frame.setContentPane(new PurchaseManagementGUI());
            frame.setSize(800, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
