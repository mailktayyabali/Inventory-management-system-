package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInSystem {
    // Declare the frame and panel
    private JFrame frame;
    private JPanel signInPanel;

    // Background image path
    private final String backgroundImagePath = "E:\\oopProject\\inventoryms\\src\\gui\\background.jpg";

    public SignInSystem() {
        // Create the main frame
        frame = new JFrame("Sign In");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());

        // Set frame size and center it on the screen
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null); // Center frame on the screen

        // Initialize panel
        signInPanel = createSignInPanel();

        // Add panel to the frame
        frame.add(signInPanel, "SignIn");

        // Set the frame to be resizable and visible
        frame.setResizable(true);
        frame.setVisible(true);
    }

    // Method to create the Sign-In panel with a background image
    private JPanel createSignInPanel() {
        JPanel panel = new ImagePanel(backgroundImagePath);
        panel.setLayout(new GridBagLayout());
        JPanel contentPanel = createSignInContent();
        panel.add(contentPanel);
        return panel;
    }

    // Content for Sign-In
    private JPanel createSignInContent() {
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setPreferredSize(new Dimension(400, 400));
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

                // Validation
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!username.equals("correctUsername") || !password.equals("correctPassword")) {
                    JOptionPane.showMessageDialog(frame, "Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Welcome " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        content.add(signInButton);

        return content;
    }

    // Custom JPanel for background image
    class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SignInSystem();
            }
        });
    }
}
