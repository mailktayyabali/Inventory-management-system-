package gui;

import Backend.Supplier;
import DB.SupplierDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class SupplierManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JTextField supplierNameField, addressField, contactField;
      private SupplierDAO supplierDAO;

    public SupplierManagementGUI() {
        // Initialize SupplierDAO
        supplierDAO = new SupplierDAO();

        setLayout(new BorderLayout());

        // Top panel with search functionality
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Name/ID:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh Data");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);

        // Set up the table model and table
        tableModel = new DefaultTableModel(new String[]{"Supplier ID", "Name", "Address", "Contact"}, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(supplierTable);

        // Input fields for adding/editing suppliers
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));  // Updated layout to accommodate 2 checkboxes
        supplierNameField = new JTextField();
        addressField = new JTextField();
        contactField = new JTextField();
        

        inputPanel.add(new JLabel("Supplier Name:"));
        inputPanel.add(supplierNameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(contactField);
        

        // Buttons for operations
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Supplier");
        JButton editButton = new JButton("Edit Supplier");
        JButton deleteButton = new JButton("Delete Supplier");
        JButton clearButton = new JButton("Clear Fields");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Combine input fields and buttons
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(inputPanel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);

        // Action listeners for buttons
        searchButton.addActionListener(new SearchButtonListener());
        refreshButton.addActionListener(e -> loadSuppliersFromDatabase()); // Refresh data
        addButton.addActionListener(new AddButtonListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        clearButton.addActionListener(e -> clearFields());

        // Populate fields on table row selection
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                supplierNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                contactField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                
            }
        });

        // Add components to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);

        // Load initial supplier data
        loadSuppliersFromDatabase();
    }

    private void clearFields() {
        supplierNameField.setText("");
        addressField.setText("");
        contactField.setText("");
        
        supplierTable.clearSelection(); // Clear table selection
    }

    private void loadSuppliersFromDatabase() {
        try {
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            tableModel.setRowCount(0); // Clear the table
            for (Supplier supplier : suppliers) {
                tableModel.addRow(new Object[]{
                        supplier.getSupplierID(),
                        supplier.getSupplierName(),
                        supplier.getAddress(),
                        supplier.getContact()
                        
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateAutoSupplierID() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999)); // Generate 6-digit ID
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String supplierNameText = supplierNameField.getText().trim();
                String addressText = addressField.getText().trim();
                String contactText = contactField.getText().trim();
                
                if (supplierNameText.isEmpty() || addressText.isEmpty() || contactText.isEmpty() || !contactText.matches("\\d{11}")) {
                    JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ensure that only one checkbox (Paid or Pending) is selected
                
                
                String newSupplierID = generateAutoSupplierID(); // Generate a new 6-digit ID

                Supplier newSupplier = new Supplier(
                        Integer.parseInt(newSupplierID),
                        supplierNameText,
                        contactText,
                        addressText
                );

                supplierDAO.addSupplier(newSupplier);
                tableModel.addRow(new Object[]{
                        newSupplier.getSupplierID(),
                        newSupplier.getSupplierName(),
                        newSupplier.getAddress(),
                        newSupplier.getContact()
                });
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Supplier added successfully!");
                clearFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Error adding supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String supplierNameText = supplierNameField.getText().trim();
                    String addressText = addressField.getText().trim();
                    String contactText = contactField.getText().trim();
                    
                    if (supplierNameText.isEmpty() || addressText.isEmpty() || contactText.isEmpty() || !contactText.matches("\\d{11}")) {
                        JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Ensure that only one checkbox (Paid or Pending) is selected
                    
                    Supplier updatedSupplier = new Supplier(
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()),
                            supplierNameText,
                            contactText,
                            addressText
                    );

                    supplierDAO.updateSupplier(updatedSupplier);
                    loadSuppliersFromDatabase();
                    supplierTable.setRowSelectionInterval(selectedRow, selectedRow);
                    JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Supplier updated successfully!");
                    clearFields();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Error updating supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                int confirmation = JOptionPane.showConfirmDialog(SupplierManagementGUI.this,
                        "Are you sure you want to delete this supplier?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        int supplierID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                        supplierDAO.deleteSupplier(supplierID);
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Supplier deleted successfully!");
                        clearFields();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Error deleting supplier: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Please select a supplier to delete.");
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "Please enter a name or ID to search.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String supplierID = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String name = tableModel.getValueAt(i, 1).toString().toLowerCase();

                if (supplierID.contains(searchTerm) || name.contains(searchTerm)) {
                    supplierTable.setRowSelectionInterval(i, i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(SupplierManagementGUI.this, "No supplier found with the given search term.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                supplierTable.clearSelection();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Supplier Management");
            frame.setContentPane(new SupplierManagementGUI());
            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
