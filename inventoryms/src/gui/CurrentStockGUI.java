package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CurrentStockGUI extends JPanel {
    private JTextField searchField;
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JTextField productNameField, categoryField, quantityField;

    public CurrentStockGUI() {
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Product/Category:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table inside a scroll pane
        tableModel = new DefaultTableModel(new String[]{"Product Name", "Category"}, 0);
        stockTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(stockTable); // Adding table to scroll pane
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scroll bar

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        productNameField = new JTextField();
        categoryField = new JTextField();
        quantityField = new JTextField();

        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Stock");
        JButton editButton = new JButton("Edit Stock");
        JButton deleteButton = new JButton("Delete Stock");
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

        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER); // Add scroll pane instead of the table directly
        add(rightScrollPane, BorderLayout.EAST);
    }

    private void clearFields() {
        productNameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productName = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String category = tableModel.getValueAt(i, 1).toString().toLowerCase();
                if (productName.contains(searchTerm) || category.contains(searchTerm)) {
                    stockTable.setRowSelectionInterval(i, i);
                    break;
                } else {
                    stockTable.clearSelection();
                }
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get stock details from input fields
            String productName = productNameField.getText();
            String category = categoryField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            // Add stock details to the table
            tableModel.addRow(new Object[]{productName, category, quantity});
            JOptionPane.showMessageDialog(CurrentStockGUI.this, "Stock added successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = stockTable.getSelectedRow();
            if (selectedRow != -1) {
                // Update stock details in the table
                tableModel.setValueAt(productNameField.getText(), selectedRow, 0);
                tableModel.setValueAt(categoryField.getText(), selectedRow, 1);
                tableModel.setValueAt(Integer.parseInt(quantityField.getText()), selectedRow, 2);

                JOptionPane.showMessageDialog(CurrentStockGUI.this, "Stock updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(CurrentStockGUI.this, "Please select a stock to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = stockTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove stock from the table
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(CurrentStockGUI.this, "Stock deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(CurrentStockGUI.this, "Please select a stock to delete.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Current Stock Management");
            frame.setContentPane(new CurrentStockGUI());
            frame.setSize(800, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
