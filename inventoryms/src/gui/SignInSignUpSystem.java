package gui;

import DB.SignInDAO;
import DB.UserDAO;
import javax.swing.*;

import Backend.Admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SignInSignUpSystem extends JFrame {
    private SignInDAO signInDAO;
    private JPanel signInPanel, signUpPanel;
    private UserDAO userDAO ;
    private final String backgroundImagePath = "E:\\oopProject\\inventoryms\\src\\gui\\background.jpg";  // Path to background image

    public SignInSignUpSystem() {
        signInDAO = new SignInDAO();
        userDAO = new UserDAO();
        setTitle("Sign In/Sign Up System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setLayout(null);
        content.setPreferredSize(new Dimension(400, 600));
        content.setBackground(new Color(255, 255, 255, 180)); // Slight transparency

        // Sign-In Label
        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBounds(120, 30, 200, 40);
        label.setForeground(Color.BLACK);
        content.add(label);

        // Username field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 100, 100, 25);
        content.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 100, 200, 25);
        content.add(userField);

        // Password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 150, 100, 25);
        content.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 150, 200, 25);
        content.add(passField);

        // Sign-In button
        JButton signInButton = new JButton("Sign In");
        signInButton.setBounds(150, 200, 100, 30);
        signInButton.setBackground(new Color(70, 50, 160));
        signInButton.setForeground(Color.WHITE);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                try {
                    boolean isAdmin = signInDAO.validateAdmin(username, password);
                    boolean isUser = signInDAO.validateUser(username, password);

                    if (isAdmin || isUser) {
                        String role = isAdmin ? "admin" : "user";
                        InventoryDashboard dashboard = new InventoryDashboard(role);
                        dashboard.setVisible(true);
                        dispose();  // Close the login window after successful login
                    } else {
                        JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        content.add(signInButton);

        // Sign-Up Label
        JLabel signUpLabel = new JLabel("Sign Up");
        signUpLabel.setFont(new Font("Arial", Font.BOLD, 30));
        signUpLabel.setBounds(120, 250, 200, 40);
        signUpLabel.setForeground(Color.BLACK);
        content.add(signUpLabel);

        // Sign-Up Username field
        JLabel signUpUserLabel = new JLabel("Username:");
        signUpUserLabel.setBounds(50, 300, 100, 25);
        content.add(signUpUserLabel);

        JTextField signUpUserField = new JTextField();
        signUpUserField.setBounds(150, 300, 200, 25);
        content.add(signUpUserField);

        // Sign-Up Password field
        JLabel signUpPassLabel = new JLabel("Password:");
        signUpPassLabel.setBounds(50, 350, 100, 25);
        content.add(signUpPassLabel);

        JPasswordField signUpPassField = new JPasswordField();
        signUpPassField.setBounds(150, 350, 200, 25);
        content.add(signUpPassField);

        // Confirm Password field
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(50, 400, 150, 25);
        content.add(confirmPassLabel);

        JPasswordField confirmPassField = new JPasswordField();
        confirmPassField.setBounds(200, 400, 150, 25);
        content.add(confirmPassField);

        // Sign-Up button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(150, 450, 100, 30);
        signUpButton.setBackground(new Color(70, 50, 160));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = signUpUserField.getText();
                String password = new String(signUpPassField.getPassword());
                String confirmPassword = new String(confirmPassField.getPassword());

                // Validation
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        if (!signInDAO.doesAdminExist(username)) {
                            boolean signUpSuccess = signInDAO.addAdmin(username, password);  // Admin registration
                            if (signUpSuccess) {
                                JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Sign up successful! Please sign in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Sign up failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Admin account already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(SignInSignUpSystem.this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        content.add(signUpButton);

        add(content);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInSignUpSystem frame = new SignInSignUpSystem();
            frame.pack();
            frame.setVisible(true);
        });
    }
}
