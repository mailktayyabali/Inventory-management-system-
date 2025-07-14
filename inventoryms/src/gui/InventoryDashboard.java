package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryDashboard extends JFrame {
    private JPanel mainContentPanel; // Panel for switching content
    private String role; // Store user role (admin or user)

    // Constructor accepts the role of the logged-in user
    public InventoryDashboard(String role) {
        this.role = role;
        setTitle("Warehouse Inventory Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Create and add the sidebar panel
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Create and add the main content area
        mainContentPanel = new JPanel(new CardLayout());
        JLabel mainContent = new JLabel("Welcome to Warehouse Inventory System", JLabel.CENTER);
        mainContent.setFont(new Font("Arial", Font.PLAIN, 20));
        mainContentPanel.add(mainContent, "Welcome");

        // Add other GUI panels
        UserManagementGUI userGUI = new UserManagementGUI();
        mainContentPanel.add(userGUI, "Users");
        ProductManagementGUI productGUI = new ProductManagementGUI();
        mainContentPanel.add(productGUI, "Products");
        CategoryManagementGUI categoryGUI = new CategoryManagementGUI();
        mainContentPanel.add(categoryGUI, "Category");
        SupplierManagementGUI supplierGUI = new SupplierManagementGUI();
        mainContentPanel.add(supplierGUI, "Suppliers");
        SaleManagementGUI salesGUI = new SaleManagementGUI();
        mainContentPanel.add(salesGUI, "Sales");
        CustomerGUI customersGUI = new CustomerGUI();
        mainContentPanel.add(customersGUI, "Customers");
        PurchaseManagementGUI purchaseManagementGUI = new PurchaseManagementGUI();
        mainContentPanel.add(purchaseManagementGUI, "Purchase");
        PointOfSaleGUI posGUI = new PointOfSaleGUI();  
        mainContentPanel.add(posGUI, "POS");

        // Add main content panel
        add(mainContentPanel, BorderLayout.CENTER);
    }

    // Create the sidebar with different buttons
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        sidebar.setBackground(Color.LIGHT_GRAY);

        // Sidebar buttons
        String[] buttons = {"Category", "Products", "Customers", "Suppliers", "Purchase", "Users", "POS", "Log Out"};

        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.addActionListener(new SidebarButtonListener());

            // Hide user-specific buttons if the role is "user"
            if ("user".equals(role) && (button.equals("Users") )) {
                btn.setVisible(false);
            }

            sidebar.add(btn);
        }

        return sidebar;
    }

    private class SidebarButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();
            CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();

            switch (buttonText) {
                case "Products":
                    cardLayout.show(mainContentPanel, "Products");
                    break;
                case "Users":
                    cardLayout.show(mainContentPanel, "Users");
                    break;
                case "Category":
                    cardLayout.show(mainContentPanel, "Category");
                    break;
                case "Suppliers":
                    cardLayout.show(mainContentPanel, "Suppliers");
                    break;
                case "Sales":
                    cardLayout.show(mainContentPanel, "Sales");
                    break;
                case "Customers":
                    cardLayout.show(mainContentPanel, "Customers");
                    break;
                case "Purchase":
                    cardLayout.show(mainContentPanel, "Purchase");
                    break;
                case "POS":
                    cardLayout.show(mainContentPanel, "POS");
                    break;
                case "Log Out":
                    int choice = JOptionPane.showConfirmDialog(InventoryDashboard.this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        // Close the dashboard and show the login window
                        dispose();
                        new SignInSignUpSystem().setVisible(true);
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(InventoryDashboard.this, "Feature for " + buttonText + " coming soon!");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Example: Pass "admin" or "user" based on login
            String role = "admin";  // this should be dynamic based on login
            InventoryDashboard dashboard = new InventoryDashboard(role);
            dashboard.setVisible(true);
        });
    }
}
