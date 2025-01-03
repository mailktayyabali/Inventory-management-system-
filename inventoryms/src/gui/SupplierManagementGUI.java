package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupplierManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JTextField supplierNameField, contactField, productField;
    private JComboBox<String> paymentStatusComboBox;

    public SupplierManagementGUI() {
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Name/Product:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table inside a scroll pane
        tableModel = new DefaultTableModel(new String[]{"Supplier Name", "Contact", "Product", "Payment Status"}, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(supplierTable); // Adding table to scroll pane
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scroll bar

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        supplierNameField = new JTextField();
        contactField = new JTextField();
        productField = new JTextField();
        paymentStatusComboBox = new JComboBox<>(new String[]{"Paid", "Pending", "Overdue"});

        inputPanel.add(new JLabel("Supplier Name:"));
        inputPanel.add(supplierNameField);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(contactField);
        inputPanel.add(new JLabel("Product:"));
        inputPanel.add(productField);
        inputPanel.add(new JLabel("Payment Status:"));
        inputPanel.add(paymentStatusComboBox);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Supplier");
        JButton editButton = new JButton("Edit Supplier");
        JButton deleteButton = new JButton("Delete Supplier");
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
        add(tableScrollPane, BorderLayout.CENTER); // Add scroll pane instead of the table directly
        add(rightScrollPane, BorderLayout.EAST);
    }

    private void clearFields() {
        supplierNameField.setText("");
        contactField.setText("");
        productField.setText("");
        paymentStatusComboBox.setSelectedIndex(0);
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String supplierName = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String product = tableModel.getValueAt(i, 2).toString().toLowerCase();
                if (supplierName.contains(searchTerm) || product.contains(searchTerm)) {
                    supplierTable.setRowSelectionInterval(i, i);
                    break;
                } else {
                    supplierTable.clearSelection();
                }
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get supplier details from input fields
            String supplierName = supplierNameField.getText();
            String contact = contactField.getText();
            String product = productField.getText();
            String paymentStatus = paymentStatusComboBox.getSelectedItem().toString();

            // Add supplier details to the table
            tableModel.addRow(new Object[]{supplierName, contact, product, paymentStatus});
            JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Supplier added successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                // Update supplier details in the table
                tableModel.setValueAt(supplierNameField.getText(), selectedRow, 0);
                tableModel.setValueAt(contactField.getText(), selectedRow, 1);
                tableModel.setValueAt(productField.getText(), selectedRow, 2);
                tableModel.setValueAt(paymentStatusComboBox.getSelectedItem().toString(), selectedRow, 3);

                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Supplier updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Please select a supplier to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove supplier from the table
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Supplier deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Please select a supplier to delete.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Supplier Management");
            frame.setContentPane(new SupplierManagementGUI());
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
