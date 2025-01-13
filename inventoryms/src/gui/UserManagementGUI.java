package gui;

import Backend.User;
import DB.UserDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class UserManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField  nameField, addressField, contactNumberField, usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private UserDAO userDAO;
    

    public UserManagementGUI() {
        // Initialize UserDAO
        userDAO = new UserDAO();

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
        tableModel = new DefaultTableModel(new String[]{"User ID", "Name", "Address", "Contact Number", "Username", "Password"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
    
        nameField = new JTextField();
        addressField = new JTextField();
        contactNumberField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        showPasswordCheckbox = new JCheckBox("Show Password");

        // Toggle password visibility
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('*'); // Hide password
            }
        });

        
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
        inputPanel.add(new JLabel(""));
        inputPanel.add(showPasswordCheckbox);

        // Buttons for operations
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");
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
        refreshButton.addActionListener(e -> loadUsersFromDatabase()); // Refresh data
        addButton.addActionListener(new AddButtonListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        clearButton.addActionListener(e -> clearFields()); 

        // Populate fields on table row selection   
        userTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                contactNumberField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                usernameField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                passwordField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });

        // Add components to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);

        // Load initial user data
        loadUsersFromDatabase();
    }

    private void clearFields() {
        
        nameField.setText("");
        addressField.setText("");
        contactNumberField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        userTable.clearSelection(); // Clear table selection
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

    private String generateAutoUserID() {
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
                String usernameText = usernameField.getText().trim();
                String passwordText = new String(passwordField.getPassword());

                if (nameText.isEmpty() || addressText.isEmpty() || contactNumberText.isEmpty() 
                        || !contactNumberText.matches("\\d{11}") // Validate 10-digit phone number
                        || usernameText.isEmpty() || passwordText.isEmpty()) {
                    JOptionPane.showMessageDialog(UserManagementGUI.this, 
                            "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String newUserID = generateAutoUserID(); // Generate a new 6-digit ID

                User newUser = new User(
                        usernameText,
                        passwordText,
                        Integer.parseInt(newUserID),
                        nameText,
                        addressText,
                        contactNumberText
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
                    
                    String nameText = nameField.getText().trim();
                    String addressText = addressField.getText().trim();
                    String contactNumberText = contactNumberField.getText().trim();
                    String usernameText = usernameField.getText().trim();
                    String passwordText = new String(passwordField.getPassword());
    
                    // Validate inputs
                    if ( nameText.isEmpty() || addressText.isEmpty()
                            || contactNumberText.isEmpty() || !contactNumberText.matches("\\d{11}") // 11-digit phone number validation
                            || usernameText.isEmpty() || passwordText.isEmpty()) {
                        JOptionPane.showMessageDialog(UserManagementGUI.this, 
                                "Please fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
    
                    // Create updated User object
                    User updatedUser = new User(
                            usernameText,
                            passwordText,
                            Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()), // Get User ID from selected row
                            nameText,
                            addressText,
                            contactNumberText
                    );
    
                    // Update in the database
                    userDAO.updateUser(updatedUser);
    
                    // Reload table data to reflect changes
                    loadUsersFromDatabase();
    
                    // Reselect the edited row
                    userTable.setRowSelectionInterval(selectedRow, selectedRow);
    
                    JOptionPane.showMessageDialog(UserManagementGUI.this, "User updated successfully!");
                    clearFields();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(UserManagementGUI.this, 
                            "Error updating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                int confirmation = JOptionPane.showConfirmDialog(UserManagementGUI.this, 
                        "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        int userID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
    
                        // Delete user from database
                        userDAO.deleteUser(userID);
    
                        // Remove user from table
                        tableModel.removeRow(selectedRow);
    
                        JOptionPane.showMessageDialog(UserManagementGUI.this, "User deleted successfully!");
                        clearFields();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(UserManagementGUI.this, 
                                "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(UserManagementGUI.this, "Please select a user to delete.");
            }
        }
    }
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase(); // Get search term and normalize
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(UserManagementGUI.this, 
                        "Please enter a name or ID to search.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String userID = tableModel.getValueAt(i, 0).toString().toLowerCase(); // Get User ID
                String name = tableModel.getValueAt(i, 1).toString().toLowerCase();   // Get Name
    
                // Check if search term matches either User ID or Name
                if (userID.contains(searchTerm) || name.contains(searchTerm)) {
                    userTable.setRowSelectionInterval(i, i); // Select the matching row
                    found = true;
                    break;
                }
            }
    
            if (!found) {
                JOptionPane.showMessageDialog(UserManagementGUI.this, 
                        "No user found with the given search term.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                userTable.clearSelection(); // Clear any previous selections if nothing is found
            }
        }
    }
    

    // Edit and Delete ButtonListeners remain unchanged...

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
