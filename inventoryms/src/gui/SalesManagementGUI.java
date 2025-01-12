package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalesManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable saleTable;
    private DefaultTableModel tableModel;
    private JTextField productNameField, quantitySoldField, revenueField;
   
    public SalesManagementGUI(CurrentStockGUI currentStockGUI) {
        
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Product:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table inside a scroll pane
        tableModel = new DefaultTableModel(new String[]{"Product Name", "Quantity Sold", "Revenue Generated"}, 0);
        saleTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(saleTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        productNameField = new JTextField();
        quantitySoldField = new JTextField();
        revenueField = new JTextField();

        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Quantity Sold:"));
        inputPanel.add(quantitySoldField);
        inputPanel.add(new JLabel("Revenue Generated:"));
        inputPanel.add(revenueField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Sale");
        JButton editButton = new JButton("Edit Sale");
        JButton deleteButton = new JButton("Delete Sale");
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
        quantitySoldField.setText("");
        revenueField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productName = tableModel.getValueAt(i, 0).toString().toLowerCase();
                if (productName.contains(searchTerm)) {
                    saleTable.setRowSelectionInterval(i, i);
                    break;
                } else {
                    saleTable.clearSelection();
                }
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get sale details from input fields
            String productName = productNameField.getText();
            int quantitySold = Integer.parseInt(quantitySoldField.getText());
            double revenueGenerated = Double.parseDouble(revenueField.getText());

            // Add sale details to the table
            tableModel.addRow(new Object[]{productName, quantitySold, revenueGenerated});

            JOptionPane.showMessageDialog(SalesManagementGUI.this, "Sale added and stock updated successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = saleTable.getSelectedRow();
            if (selectedRow != -1) {
                // Update sale details in the table
                tableModel.setValueAt(productNameField.getText(), selectedRow, 0);
                tableModel.setValueAt(Integer.parseInt(quantitySoldField.getText()), selectedRow, 1);
                tableModel.setValueAt(Double.parseDouble(revenueField.getText()), selectedRow, 2);

                JOptionPane.showMessageDialog(SalesManagementGUI.this, "Sale updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(SalesManagementGUI.this, "Please select a sale to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = saleTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove sale from the table
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(SalesManagementGUI.this, "Sale deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(SalesManagementGUI.this, "Please select a sale to delete.");
            }
        }
    }
}

