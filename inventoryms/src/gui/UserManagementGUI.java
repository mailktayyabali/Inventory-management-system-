package gui;

import Backend.User;
import DB.UserDAO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class UserManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField userIDField, nameField, addressField, contactNumberField, usernameField;
    private JPasswordField passwordField; // Use JPasswordField for security
    private UserDAO userDAO; // UserDAO instance for database operations

    public UserManagementGUI() {
        // Initialize UserDAO
        userDAO = new UserDAO();

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
        passwordField = new JPasswordField();

        // Add KeyListener to restrict userIDField to numeric input
        userIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume(); // Ignore non-numeric input
                }
            }
        });

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

        // Add a ListSelectionListener to populate input fields when a table row is selected
        userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    userIDField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    addressField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    contactNumberField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    usernameField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    passwordField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                }
            }
        });

        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);

        // Load initial data from the database
        loadUsersFromDatabase();
    }

    private void clearFields() {
        userIDField.setText("");
        nameField.setText("");
        addressField.setText("");
        contactNumberField.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }

    private void loadUsersFromDatabase() {
        try {
            List<User> users = userDAO.getAllUsers();
            tableModel.setRowCount(0); // Clear the table
            for (User user : users) {
                tableModel.addRow(new Object[]{
                        user.getuserid(),
                        user.getname(),
                        user.getaddress(),
                        user.getcontactNumber(),
                        user.getusername(),
                        user.getpassword()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            try {
                String userIDText = userIDField.getText().trim();
                String nameText = nameField.getText().trim();
            
                String addressText = addressField.getText().trim();
                String contactNumberText = contactNumberField.getText().trim();
                String usernameText = usernameField.getText().trim();
                String passwordText = new String(passwordField.getPassword());
                if (userIDText.isEmpty() || !userIDText.matches("\\d+")|| nameText.isEmpty() || addressText.isEmpty() || contactNumberText.isEmpty() || !contactNumberText.matches("\\d{11}") || usernameText.isEmpty() || passwordText.isEmpty()) {
                    JOptionPane.showMessageDialog(UserManagementGUI.this, "Filled in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User newUser = new User(
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        Integer.parseInt(userIDText),
                        nameField.getText(),
                        addressField.getText(),
                        contactNumberField.getText()
                );
                userDAO.addUser(newUser);
                tableModel.addRow(new Object[]{
                        newUser.getuserid(),
                        newUser.getname(),
                        newUser.getaddress(),
                        newUser.getcontactNumber(),
                        newUser.getusername(),
                        newUser.getpassword()
                });
                JOptionPane.showMessageDialog(UserManagementGUI.this, "User added successfully!");
                clearFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Error adding user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    String userIDText = userIDField.getText().trim();
                    String nameText = nameField.getText().trim();
                    String addressText = addressField.getText().trim();
                    String contactNumberText = contactNumberField.getText().trim();
                    String usernameText = usernameField.getText().trim();
                    String passwordText = new String(passwordField.getPassword());
    
                    // Validate inputs
                    if (userIDText.isEmpty() || !userIDText.matches("\\d+")
                            || nameText.isEmpty() || addressText.isEmpty()
                            || contactNumberText.isEmpty() || usernameText.isEmpty()
                            || passwordText.isEmpty()) {
                        JOptionPane.showMessageDialog(UserManagementGUI.this,
                                "Please fill in all fields correctly.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
    
                    // Create updated User object
                    User updatedUser = new User(
                            usernameText,
                            passwordText,
                            Integer.parseInt(userIDText),
                            nameText,
                            addressText,
                            contactNumberText
                    );
    
                    // Update in the database
                    userDAO.updateUser(updatedUser);
    
                    // Reload table data
                    loadUsersFromDatabase();
    
                    // Reselect the row
                    userTable.setRowSelectionInterval(selectedRow, selectedRow);
    
                    JOptionPane.showMessageDialog(UserManagementGUI.this,
                            "User updated successfully!");
                    clearFields();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(UserManagementGUI.this,
                            "Error updating user: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(UserManagementGUI.this,
                        "Please select a user to edit.");
            }
        }
    }
    

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int userID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                try {
                    userDAO.deleteUser(userID);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(UserManagementGUI.this, "User deleted successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(UserManagementGUI.this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
