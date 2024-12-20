package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryDashboard {

    private JFrame frame;
    private JPanel rightPanel; // Panel where Product Panel will be displayed dynamically
    private JPanel productPanel; // Panel for Product Details

    public InventoryDashboard() {
        frame = new JFrame("Warehouse Inventory Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
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
        productButton.setBackground(new Color(220, 210, 240));
        leftPanel.add(productButton);

        String[] options = {"Current Stock", "Customers", "Suppliers", "Sales", "Purchase", "Users"};
        for (String option : options) {
            JButton optionButton = new JButton(option);
            optionButton.setBackground(new Color(220, 210, 240)); // Light purple shade
            leftPanel.add(optionButton);
        }

        frame.add(leftPanel, BorderLayout.WEST);

        // Right Panel (Dynamic Content Panel)
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(new Color(255, 255, 255)); // White background
        frame.add(rightPanel, BorderLayout.CENTER);

        // Create Product Panel
        createProductPanel();

        // Add ActionListener to the Products button
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProductPanel();
            }
        });

        // Set frame visible
        frame.setVisible(true);
    }

    private void createProductPanel() {
        productPanel = new JPanel();
        productPanel.setLayout(null); // Absolute layout
        productPanel.setBackground(new Color(240, 240, 255)); // Very light purple

        // Title Label
        JLabel titleLabel = new JLabel("PRODUCT DETAILS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(20, 20, 200, 30);
        productPanel.add(titleLabel);

        // Labels and Text Fields for Form
        String[] labels = {"Product Code:", "Product Name:", "Date:", "Quantity:", "Cost Price:", "Selling Price:", "Brand:"};
        int y = 70;

        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(20, y, 100, 20); // Label position
            productPanel.add(lbl);

            JTextField textField = new JTextField();
            textField.setBounds(130, y, 150, 20); // Text field position
            productPanel.add(textField);

            y += 40; // Increment y for next label and text field
        }

        // Buttons
        JButton addButton = new JButton("Add");
        addButton.setBounds(20, y + 10, 80, 30);
        productPanel.add(addButton);

        JButton editButton = new JButton("Edit");
        editButton.setBounds(110, y + 10, 80, 30);
        productPanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(200, y + 10, 80, 30);
        productPanel.add(deleteButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(290, y + 10, 80, 30);
        productPanel.add(clearButton);
    }

    private void showProductPanel() {
        rightPanel.removeAll(); // Clear the existing panel content
        rightPanel.add(productPanel, BorderLayout.CENTER); // Add the product panel
        rightPanel.revalidate(); // Refresh the panel
        rightPanel.repaint(); // Repaint to reflect the changes
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryDashboard();
            }
        });
    }
}

