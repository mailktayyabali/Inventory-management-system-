package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryDashboard {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel homePanel;
    private JPanel productPanel;

    public InventoryDashboard() {
        frame = new JFrame("Warehouse Inventory Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the window

        // Left Navigation Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(8, 1));
        leftPanel.setBackground(new Color(230, 220, 250)); // Light purple background

        JButton homeButton = new JButton("Home");
        homeButton.setBackground(new Color(200, 200, 255));
        leftPanel.add(homeButton);

        JButton productButton = new JButton("Products");
        productButton.setBackground(new Color(200, 200, 255));
        leftPanel.add(productButton);

        String[] options = {"Current Stock", "Customers", "Suppliers", "Sales", "Purchase", "Users"};
        for (String option : options) {
            JButton optionButton = new JButton(option);
            optionButton.setBackground(new Color(220, 210, 240));
            leftPanel.add(optionButton);
        }

        frame.add(leftPanel, BorderLayout.WEST);

        // Main Panel (Center)
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout()); // Use CardLayout for easy switching
        frame.add(mainPanel, BorderLayout.CENTER);

        // Create Panels
        createHomePanel();
        createProductPanel();

        // Add Panels to Main Panel
        mainPanel.add(homePanel, "Home");
        mainPanel.add(productPanel, "Products");

        // Button Actions
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToPanel("Home");
            }
        });

        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToPanel("Products");
            }
        });

        // Show Frame
        frame.setVisible(true);
    }

    // Method to Create Home Panel
    private void createHomePanel() {
        homePanel = new JPanel();
        homePanel.setLayout(null); // Use null layout for custom positioning
        homePanel.setBackground(new Color(230, 220, 250)); // Light purple background

        JLabel welcomeLabel = new JLabel("Welcome to Warehouse Inventory System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(100, 50, 150)); // Dark purple text
        welcomeLabel.setBounds(100, 200, 600, 50);
        homePanel.add(welcomeLabel);
    }

    // Method to Create Product Panel
    private void createProductPanel() {
        productPanel = new JPanel();
        productPanel.setLayout(null);
        productPanel.setBackground(new Color(230, 220, 250)); // Light purple background

        JLabel titleLabel = new JLabel("PRODUCT DETAILS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(20, 20, 300, 30);
        productPanel.add(titleLabel);

        // Labels and TextFields
        String[] labels = {"Product Code:", "Product Name:", "Date:", "Quantity:", "Cost Price:", "Selling Price:", "Brand:"};
        int y = 70;
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(20, y, 100, 20);
            productPanel.add(lbl);

            JTextField textField = new JTextField();
            textField.setBounds(130, y, 130, 20);
            productPanel.add(textField);

            y += 40;
        }

        // Buttons
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        addButton.setBounds(20, y, 80, 25);
        editButton.setBounds(110, y, 80, 25);
        deleteButton.setBounds(200, y, 80, 25);
        clearButton.setBounds(290, y, 80, 25);

        productPanel.add(addButton);
        productPanel.add(editButton);
        productPanel.add(deleteButton);
        productPanel.add(clearButton);
    }

    // Method to Switch Panels
    private void switchToPanel(String panelName) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryDashboard());
    }
}
