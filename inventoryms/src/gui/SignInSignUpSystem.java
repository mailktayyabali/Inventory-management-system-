package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInSignUpSystem {
    // Declare the frame and panels
    private JFrame frame;
    private JPanel signInPanel, signUpPanel;

    // Background image paths
    private final String backgroundImagePath = "E:\\oopProject\\inventoryms\\src\\gui\\background.jpg";

    public SignInSignUpSystem() {
        // Create the main frame
        frame = new JFrame("Sign In & Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());

        // Set frame size and center it on the screen
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null); // Center frame on the screen

        // Initialize panels
        signInPanel = createSignInPanel();
        signUpPanel = createSignUpPanel();

        // Add panels to the frame
        frame.add(signInPanel, "SignIn");
        frame.add(signUpPanel, "SignUp");

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
        content.add(signInButton);

        // Go to Sign-Up button
        JButton goToSignUpButton = new JButton("Don't have an account? Sign Up");
        goToSignUpButton.setBounds(100, 250, 220, 30);
        goToSignUpButton.setForeground(Color.BLUE);
        goToSignUpButton.setBorderPainted(false);
        goToSignUpButton.setContentAreaFilled(false);

        // ActionListener to switch to the Sign-Up panel
        goToSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel("SignUp");
            }
        });
        content.add(goToSignUpButton);

        return content;
    }

    // Method to create the Sign-Up panel with a background image
    private JPanel createSignUpPanel() {
        JPanel panel = new ImagePanel(backgroundImagePath);
        panel.setLayout(new GridBagLayout());
        JPanel contentPanel = createSignUpContent();
        panel.add(contentPanel);
        return panel;
    }

    // Content for Sign-Up
    private JPanel createSignUpContent() {
        JPanel content = new JPanel();
        content.setLayout(null);
        content.setPreferredSize(new Dimension(400, 400));
        content.setBackground(new Color(255, 255, 255, 180));

        // Sign-Up Label
        JLabel label = new JLabel("Sign Up");
        label.setFont(new Font("Lato", Font.BOLD, 30));
        label.setBounds(130, 30, 200, 40);
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

        // Confirm Password field
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(30, 200, 130, 25);
        content.add(confirmPassLabel);

        JPasswordField confirmPassField = new JPasswordField();
        confirmPassField.setBounds(150, 200, 200, 25);
        content.add(confirmPassField);

        // Sign-Up button
        JButton signUpButton = new JButton("Create Account");
        signUpButton.setBounds(150, 250, 150, 30);
        signUpButton.setBackground(new Color(70, 50, 160));
        signUpButton.setForeground(Color.WHITE);
        content.add(signUpButton);

        // Go to Sign-In button
        JButton goToSignInButton = new JButton("Already have an account? Sign In");
        goToSignInButton.setBounds(100, 300, 220, 30);
        goToSignInButton.setForeground(Color.BLUE);
        goToSignInButton.setBorderPainted(false);
        goToSignInButton.setContentAreaFilled(false);

        // ActionListener to switch to Sign-In panel
        goToSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel("SignIn");
            }
        });
        content.add(goToSignInButton);

        return content;
    }

    // Method to switch between panels
    private void switchPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), panelName);
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
                new SignInSignUpSystem();
            }
        });
    }
}
