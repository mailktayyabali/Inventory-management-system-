package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField userIDField, nameField, addressField, contactNumberField, usernameField;
    private JPasswordField passwordField; // Use JPasswordField for security

    public UserManagementGUI() {
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Name/ID:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table
        tableModel = new DefaultTableModel(new String[]{"User ID", "Name", "Address", "Contact Number", "Username", "Password"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        userIDField = new JTextField();
        nameField = new JTextField();
        addressField = new JTextField();
        contactNumberField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField(); // For security, use JPasswordField

        inputPanel.add(new JLabel("User ID:"));
        inputPanel.add(userIDField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Contact Number:"));
        inputPanel.add(contactNumberField);
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");
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
        userIDField.setText("");
        nameField.setText("");
        addressField.setText("");
        contactNumberField.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String userID = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String name = tableModel.getValueAt(i, 1).toString().toLowerCase();
                if (userID.contains(searchTerm) || name.contains(searchTerm)) {
                    userTable.setRowSelectionInterval(i, i);
                    break;
                } else {
                    userTable.clearSelection();
                }
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get user details from input fields
            String userID = userIDField.getText();
            String name = nameField.getText();
            String address = addressField.getText();
            String contactNumber = contactNumberField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Add user details to the table
            tableModel.addRow(new Object[]{userID, name, address, contactNumber, username, password});
            JOptionPane.showMessageDialog(UserManagementGUI.this, "User added successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                // Update user details in the table
                tableModel.setValueAt(userIDField.getText(), selectedRow, 0);
                tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                tableModel.setValueAt(addressField.getText(), selectedRow, 2);
                tableModel.setValueAt(contactNumberField.getText(), selectedRow, 3);
                tableModel.setValueAt(usernameField.getText(), selectedRow, 4);
                tableModel.setValueAt(new String(passwordField.getPassword()), selectedRow, 5);

                JOptionPane.showMessageDialog(UserManagementGUI.this, "User updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Please select a user to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove user from the table
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(UserManagementGUI.this, "User deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Please select a user to delete.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("User Management");
            frame.setContentPane(new UserManagementGUI());
            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
