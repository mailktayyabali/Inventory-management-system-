package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderManagementGUI extends JPanel {
    private JTable customerTable, productTable, cartTable;
    private JTable customerDetailsTable, productDetailsTable;
    private DefaultTableModel customerTableModel, productTableModel, cartTableModel;
    private DefaultTableModel customerDetailsModel, productDetailsModel;

    public OrderManagementGUI() {
        setLayout(new BorderLayout());

        // Create top panel for search
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Order ID:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Create main panel with grid layout
        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        // Customer list panel
        JPanel customerListPanel = new JPanel(new BorderLayout());
        JLabel customerListLabel = new JLabel("Customer List");
        customerTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Mobile", "Email"}, 0);
        customerTable = new JTable(customerTableModel);
        customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerListPanel.add(customerListLabel, BorderLayout.NORTH);
        customerListPanel.add(customerScrollPane, BorderLayout.CENTER);

        // Customer details table
        JPanel customerDetailsPanel = new JPanel(new BorderLayout());
        JLabel customerDetailsLabel = new JLabel("Customer Details");
        customerDetailsModel = new DefaultTableModel(new String[]{"Field", "Value"}, 0);
        customerDetailsTable = new JTable(customerDetailsModel);
        customerDetailsTable.setEnabled(true); // Editable
        customerDetailsPanel.add(customerDetailsLabel, BorderLayout.NORTH);
        customerDetailsPanel.add(new JScrollPane(customerDetailsTable), BorderLayout.CENTER);

        // Product list panel
        JPanel productListPanel = new JPanel(new BorderLayout());
        JLabel productListLabel = new JLabel("Product List");
        productTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Qty", "Desc", "Category"}, 0);
        productTable = new JTable(productTableModel);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productListPanel.add(productListLabel, BorderLayout.NORTH);
        productListPanel.add(productScrollPane, BorderLayout.CENTER);

        // Product details table
        JPanel productDetailsPanel = new JPanel(new BorderLayout());
        JLabel productDetailsLabel = new JLabel("Product Details");
        productDetailsModel = new DefaultTableModel(new String[]{"Field", "Value"}, 0);
        productDetailsTable = new JTable(productDetailsModel);
        productDetailsTable.setEnabled(true); // Editable
        productDetailsPanel.add(productDetailsLabel, BorderLayout.NORTH);
        productDetailsPanel.add(new JScrollPane(productDetailsTable), BorderLayout.CENTER);

        // Cart panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        JLabel cartLabel = new JLabel("Cart");
        cartTableModel = new DefaultTableModel(new String[]{"Prod ID", "Name", "Qty", "Price", "Desc", "Sub Total"}, 0);
        cartTable = new JTable(cartTableModel);
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartPanel.add(cartLabel, BorderLayout.NORTH);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        // Add panels to main panel
        mainPanel.add(customerListPanel);
        mainPanel.add(productListPanel);
        mainPanel.add(cartPanel);
        mainPanel.add(customerDetailsPanel);
        mainPanel.add(productDetailsPanel);
        add(mainPanel, BorderLayout.CENTER);

        // Create button panel for actions
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addToCartButton = new JButton("Add to Cart");
        JButton saveOrderButton = new JButton("Save Order Details");
        JButton resetButton = new JButton("Reset");
        JButton closeButton = new JButton("Close");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(saveOrderButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        productTable.getSelectionModel().addListSelectionListener(new ProductSelectionListener());
        customerTable.getSelectionModel().addListSelectionListener(new CustomerSelectionListener());
        addToCartButton.addActionListener(new AddToCartButtonListener());
    }

    private class CustomerSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                customerDetailsModel.setRowCount(0); // Clear current data
                customerDetailsModel.addRow(new Object[]{"Name", customerTableModel.getValueAt(selectedRow, 1)});
                customerDetailsModel.addRow(new Object[]{"Mobile", customerTableModel.getValueAt(selectedRow, 2)});
                customerDetailsModel.addRow(new Object[]{"Email", customerTableModel.getValueAt(selectedRow, 3)});
            }
        }
    }

    private class ProductSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                productDetailsModel.setRowCount(0); // Clear current data
                productDetailsModel.addRow(new Object[]{"Name", productTableModel.getValueAt(selectedRow, 1)});
                productDetailsModel.addRow(new Object[]{"Price", productTableModel.getValueAt(selectedRow, 2)});
                productDetailsModel.addRow(new Object[]{"Desc", productTableModel.getValueAt(selectedRow, 4)});
                productDetailsModel.addRow(new Object[]{"Quantity", ""}); // User to fill
            }
        }
    }

    private class AddToCartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                String productId = productTableModel.getValueAt(selectedRow, 0).toString();
                String productName = productDetailsModel.getValueAt(0, 1).toString();
                int quantity = Integer.parseInt(productDetailsModel.getValueAt(3, 1).toString());
                double price = Double.parseDouble(productDetailsModel.getValueAt(1, 1).toString());
                double subTotal = quantity * price;
                String description = productDetailsModel.getValueAt(2, 1).toString();

                cartTableModel.addRow(new Object[]{productId, productName, quantity, price, description, subTotal});
                JOptionPane.showMessageDialog(OrderManagementGUI.this, "Product added to cart!");
            } else {
                JOptionPane.showMessageDialog(OrderManagementGUI.this, "Please select a product to add to the cart.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Order Management");
            frame.setContentPane(new OrderManagementGUI());
            frame.setSize(1200, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
