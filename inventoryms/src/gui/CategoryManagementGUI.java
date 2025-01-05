package gui;

import Backend.Category;
import DB.CategoryDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CategoryManagementGUI extends JPanel {
    private JTextField searchField;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField categoryIDField, categoryNameField;
    private final CategoryDAO categoryDAO;

    public CategoryManagementGUI() {
        categoryDAO = new CategoryDAO();
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

        // Load initial data
        loadCategoryData();
    }

    private void loadCategoryData() {
        try {
            tableModel.setRowCount(0); // Clear existing data
            for (Category category : categoryDAO.getAllCategories()) {
                tableModel.addRow(new Object[]{category.getCategoryID(), category.getCategoryName()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage());
        }
    }

    private void clearFields() {
        categoryIDField.setText("");
        categoryNameField.setText("");
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String categoryID = tableModel.getValueAt(i, 0).toString().toLowerCase();
                String categoryName = tableModel.getValueAt(i, 1).toString().toLowerCase();
                if (categoryID.contains(searchTerm) || categoryName.contains(searchTerm)) {
                    categoryTable.setRowSelectionInterval(i, i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "No matching category found.");
                categoryTable.clearSelection();
            }
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String categoryID = categoryIDField.getText().trim();
            String categoryName = categoryNameField.getText().trim();

            if (categoryID.isEmpty() || categoryName.isEmpty()) {
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Please fill in all fields.");
                return;
            }

            try {
                Category category = new Category(categoryID, categoryName);
                categoryDAO.addCategory(category);
                tableModel.addRow(new Object[]{category.getCategoryID(), category.getCategoryName()});
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Category added successfully!");
                clearFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Error adding category: " + ex.getMessage());
            }
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow != -1) {
                String categoryID = categoryIDField.getText().trim();
                String categoryName = categoryNameField.getText().trim();

                if (categoryID.isEmpty() || categoryName.isEmpty()) {
                    JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Please fill in all fields.");
                    return;
                }

                try {
                    Category category = new Category(categoryID, categoryName);
                    categoryDAO.updateCategory(category);

                    // Update table model
                    tableModel.setValueAt(category.getCategoryID(), selectedRow, 0);
                    tableModel.setValueAt(category.getCategoryName(), selectedRow, 1);

                    JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Category updated successfully!");
                    clearFields();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Error updating category: " + ex.getMessage());
                }
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
                String categoryID = tableModel.getValueAt(selectedRow, 0).toString();
                try {
                    categoryDAO.deleteCategory(categoryID);

                    // Remove category from the table
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Category deleted successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Error deleting category: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(CategoryManagementGUI.this, "Please select a category to delete.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Category Management");
            frame.setContentPane(new CategoryManagementGUI());
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
