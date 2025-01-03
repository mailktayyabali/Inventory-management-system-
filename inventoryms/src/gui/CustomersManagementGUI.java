package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomersManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField customerNameField, addressField, contactField;

    public CustomersManagementGUI() {
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Name:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table inside a scroll pane
        tableModel = new DefaultTableModel(new String[]{"Customer Name", "Address", "Contact"}, 0);
        customerTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(customerTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        customerNameField = new JTextField();
        addressField = new JTextField();
        contactField = new JTextField();

        inputPanel.add(new JLabel("Customer Name:"));
        inputPanel.add(customerNameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Contact:"));
        inputPanel.add(contactField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10)); // Adjusted layout to fit more buttons
        JButton addButton = new JButton("Add Customer");
        JButton editButton = new JButton("Edit Customer");
        JButton deleteButton = new JButton("Delete Customer");
        JButton clearButton = new JButton("Clear Fields");
        JButton generateReportButton = new JButton("Generate Report");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(generateReportButton);

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
        generateReportButton.addActionListener(new GenerateReportButtonListener());

        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);
    }

    private void clearFields() {
        customerNameField.setText("");
        addressField.setText("");
        contactField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String customerName = tableModel.getValueAt(i, 0).toString().toLowerCase();
                if (customerName.contains(searchTerm)) {
                    customerTable.setRowSelectionInterval(i, i);
                    break;
                } else {
                    customerTable.clearSelection();
                }
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get customer details from input fields
            String customerName = customerNameField.getText();
            String address = addressField.getText();
            String contact = contactField.getText();

            // Add customer details to the table
            tableModel.addRow(new Object[]{customerName, address, contact});
            JOptionPane.showMessageDialog(CustomersManagementGUI.this, "Customer added successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                // Update customer details in the table
                tableModel.setValueAt(customerNameField.getText(), selectedRow, 0);
                tableModel.setValueAt(addressField.getText(), selectedRow, 1);
                tableModel.setValueAt(contactField.getText(), selectedRow, 2);

                JOptionPane.showMessageDialog(CustomersManagementGUI.this, "Customer updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(CustomersManagementGUI.this, "Please select a customer to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove customer from the table
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(CustomersManagementGUI.this, "Customer deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(CustomersManagementGUI.this, "Please select a customer to delete.");
            }
        }
    }

    private class GenerateReportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(CustomersManagementGUI.this, "Generate Report button clicked.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Customer Management");
            frame.setContentPane(new CustomersManagementGUI());
            frame.setSize(800, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
