package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField categoryIDField, categoryNameField;

    public CategoryManagementGUI() {
        setLayout(new BorderLayout());

        // Create UI components
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by ID/Name:");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Set up the table model and table
        tableModel = new DefaultTableModel(new String[]{"Category ID", "Category Name"}, 0);
        categoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        categoryIDField = new JTextField();
        categoryNameField = new JTextField();

        inputPanel.add(new JLabel("Category ID:"));
        inputPanel.add(categoryIDField);
        inputPanel.add(new JLabel("Category Name:"));
        inputPanel.add(categoryNameField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton addButton = new JButton("Add Category");
        JButton editButton = new JButton("Edit Category");
        JButton deleteButton = new JButton("Delete Category");
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
        add(tableScrollPane, BorderLayout.CENTER);
        add(rightScrollPane, BorderLayout.EAST);
    }

    private void clearFields() {
        categoryIDField.setText("");
        categoryNameField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String categoryID = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String categoryName = tableModel.getValueAt(i, 1).toString().toLowerCase();
                if (categoryID.contains(searchTerm) || categoryName.contains(searchTerm)) {
                    categoryTable.setRowSelectionInterval(i, i);
                    break;
                } else {
                    categoryTable.clearSelection();
                }
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get category details from input fields
            String categoryID = categoryIDField.getText();
            String categoryName = categoryNameField.getText();

            // Add category details to the table
            tableModel.addRow(new Object[]{categoryID, categoryName});
            JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Category added successfully!");
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow != -1) {
                // Update category details in the table
                tableModel.setValueAt(categoryIDField.getText(), selectedRow, 0);
                tableModel.setValueAt(categoryNameField.getText(), selectedRow, 1);

                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Category updated successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Please select a category to edit.");
            }
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove category from the table
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Category deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Please select a category to delete.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Category Management");
            frame.setContentPane(new CategoryManagementGUI());
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
