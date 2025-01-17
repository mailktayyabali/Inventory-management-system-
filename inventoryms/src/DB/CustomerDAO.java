package DB; 

import Backend.Customer;


import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {

    public void addCustomer(Customer customer) throws SQLException{
        String query = "INSERT INTO customers(customer_id,customer_name,address,contact) VALUES(?,?,?,?)";
        try(Connection conn= DBconnection.getConnection();
            PreparedStatement stmt= conn.prepareStatement(query)){
            stmt.setInt(1, customer.getCustomerID());
            stmt.setString(2, customer.getCustomerName());
            stmt.setString(3,customer.getAddress());
            stmt.setString(4,customer.getcontact());
            stmt.executeUpdate();
        }
    }
    public void updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE customers SET customer_name = ?, address = ?, contact = ? WHERE customer_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Setting the parameters for the query
            stmt.setString(1, customer.getCustomerName());  // customer_name
            stmt.setString(2, customer.getAddress());  // address
            stmt.setString(3, customer.getcontact());  // contact
            stmt.setInt(4, customer.getCustomerID());  // customer_id
    
            stmt.executeUpdate();  // Execute the update query
        }
    }
    
    public void deleteCustomer(int CustomerID) throws SQLException {
        String query = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1,CustomerID);
            stmt.executeUpdate();
        }
    }
    public List<Customer> getAllUsers() throws SQLException {
        String query = "SELECT * FROM customers";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("contact")
                ));
            }
        }
        return customers;
    }
    public Customer getCustomerByID(int customerID) throws SQLException {
            String query = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("address"),
                        rs.getString("contact")
                );
            }
        }
        return null; 
    }
    public List<Customer> getAllCustomers() throws SQLException {
        String query = "SELECT * FROM customers ";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getString("address"),
                    rs.getString("contact")
                ));
            }
        }
        return customers;
    }

}
