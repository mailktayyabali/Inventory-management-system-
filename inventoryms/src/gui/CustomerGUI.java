package gui;

import Backend.Customer;
import DB.CustomerDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class CustomerGUI extends JPanel {
    private JTextField searchField;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, addressField, contactNumberField;
    private CustomerDAO customerDAO;

    public CustomerGUI() {
        // Initialize CustomerDAO
        customerDAO = new CustomerDAO();

        setLayout(new BorderLayout());

        // Top panel with search functionality
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Name/ID:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh Data"); // Refresh button
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);

        // Set up the table model and table
        tableModel = new DefaultTableModel(new String[]{"Customer ID", "Name", "Address", "Contact Number"}, 0);
        customerTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(customerTable);

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        nameField = new JTextField();
        addressField = new JTextField();
        contactNumberField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Contact Number:"));
        inputPanel.add(contactNumberField);

        // Buttons for operations
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Customer");
        JButton editButton = new JButton("Edit Customer");
        JButton deleteButton = new JButton("Delete Customer");
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
        refreshButton.addActionListener(e -> loadCustomersFromDatabase()); // Refresh data
        addButton.addActionListener(new AddButtonListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        clearButton.addActionListener(e -> clearFields());

        // Populate fields on table row selection
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                contactNumberField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        // Add components to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);

        // Load initial customer data
        loadCustomersFromDatabase();
    }

    private void clearFields() {
        nameField.setText("");
        addressField.setText("");
        contactNumberField.setText("");
        customerTable.clearSelection(); // Clear table selection
    }

    private void loadCustomersFromDatabase() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            tableModel.setRowCount(0); // Clear the table
            for (Customer customer : customers) {
                tableModel.addRow(new Object[]{
                        customer.getCustomerID(),
                        customer.getCustomerName(),
                        customer.getAddress(),
                        customer.getcontact()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateAutoCustomerID() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999)); // Generate 6-digit ID
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String nameText = nameField.getText().trim();
                String addressText = addressField.getText().trim();
                String contactNumberText = contactNumberField.getText().trim();

                if (nameText.isEmpty() || addressText.isEmpty() || contactNumberText.isEmpty()
                        || !contactNumberText.matches("\\d{11}")) { // Validate 11-digit phone number
                    JOptionPane.showMessageDialog(CustomerGUI.this,
                            "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String newCustomerID = generateAutoCustomerID(); // Generate a new 6-digit ID

                Customer newCustomer = new Customer(
                        Integer.parseInt(newCustomerID),
                        nameText,
                        addressText,
                        contactNumberText
                );

                customerDAO.addCustomer(newCustomer);
                tableModel.addRow(new Object[]{
                        newCustomer.getCustomerID(),
                        newCustomer.getCustomerName(),
                        newCustomer.getAddress(),
                        newCustomer.getcontact()
                });
                JOptionPane.showMessageDialog(CustomerGUI.this, "Customer added successfully!");
                clearFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(CustomerGUI.this, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String nameText = nameField.getText().trim();
                    String addressText = addressField.getText().trim();
                    String contactNumberText = contactNumberField.getText().trim();

                    if (nameText.isEmpty() || addressText.isEmpty() || contactNumberText.isEmpty()
                            || !contactNumberText.matches("\\d{11}")) { // Validate 11-digit phone number
                        JOptionPane.showMessageDialog(CustomerGUI.this,
                                "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Customer updatedCustomer = new Customer(
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()), // Get Customer ID from selected row
                            nameText,
                            addressText,
                            contactNumberText
                    );

                    customerDAO.updateCustomer(updatedCustomer);

                    // Reload table data to reflect changes
                    loadCustomersFromDatabase();

                    // Reselect the edited row
                    customerTable.setRowSelectionInterval(selectedRow, selectedRow);

                    JOptionPane.showMessageDialog(CustomerGUI.this, "Customer updated successfully!");
                    clearFields();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(CustomerGUI.this,
                            "Error updating customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(CustomerGUI.this, "Please select a customer to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirmation = JOptionPane.showConfirmDialog(CustomerGUI.this,
                        "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        int customerID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

                        // Delete customer from database
                        customerDAO.deleteCustomer(customerID);

                        // Remove customer from table
                        tableModel.removeRow(selectedRow);

                        JOptionPane.showMessageDialog(CustomerGUI.this, "Customer deleted successfully!");
                        clearFields();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(CustomerGUI.this,
                                "Error deleting customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(CustomerGUI.this, "Please select a customer to delete.");
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase(); // Get search term and normalize
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(CustomerGUI.this,
                        "Please enter a name or ID to search.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String customerID = tableModel.getValueAt(i, 0).toString().toLowerCase(); // Get Customer ID
                String name = tableModel.getValueAt(i, 1).toString().toLowerCase();   // Get Name

                // Check if search term matches either Customer ID or Name
                if (customerID.contains(searchTerm) || name.contains(searchTerm)) {
                    customerTable.setRowSelectionInterval(i, i); // Select the matching row
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(CustomerGUI.this,
                        "No customer found with the given search term.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                customerTable.clearSelection(); // Clear any previous selections if nothing is found
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Customer Management");
            frame.setContentPane(new CustomerGUI());
            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
