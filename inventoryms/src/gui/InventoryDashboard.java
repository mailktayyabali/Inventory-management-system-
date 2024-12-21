package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryDashboard extends JFrame {
    private JPanel mainContentPanel; // Panel for switching content

    public InventoryDashboard() {
        setTitle("Warehouse Inventory Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout for the main frame
        setLayout(new BorderLayout());

        // Create and add the sidebar panel
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Create and add the main content area
        mainContentPanel = new JPanel(new CardLayout()); // Using CardLayout for switching
        JLabel mainContent = new JLabel("Welcome to Warehouse Inventory System", JLabel.CENTER);
        mainContent.setFont(new Font("Arial", Font.PLAIN, 20));
        mainContentPanel.add(mainContent, "Welcome");

        // Add user management GUI panel
        UserManagementGUI userGUI = new UserManagementGUI();
        mainContentPanel.add(userGUI, "Users");

        // Add product management GUI panel
        ProductManagementGUI productGUI = new ProductManagementGUI();
        mainContentPanel.add(productGUI, "Products");

        // Adding mainContentPanel to the frame
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        sidebar.setBackground(Color.LIGHT_GRAY);

        // Sidebar buttons
        String[] buttons = {"Home", "Products", "Current Stock", "Customers", "Suppliers", "Sales", "Purchase", "Users"};
        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.addActionListener(new SidebarButtonListener());
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
                default:
                    JOptionPane.showMessageDialog(InventoryDashboard.this, "Feature for " + buttonText + " coming soon!");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventoryDashboard dashboard = new InventoryDashboard();
            dashboard.setVisible(true);
        });
    }
}
