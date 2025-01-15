package DB; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Backend.Customer;

public class CustomerDAO {

    public void addCustomer(Customer customer) throws SQLException{
        String query = "INSERT INTO customers(customer_id,customer_name,address,contact) VALUES(?,?,?,?)";
        try(Connection conn= DBconnection.getConnection();
            PreparedStatement stmt= conn.prepareStatement(query)){
            stmt.setInt(1, customer.getCustomerID());
            stmt.setString(2, customer.getCustomerName());
            stmt.setString(3,customer.getAddress());
            stmt.setString(4,customer.getconnect());
            stmt.executeUpdate();
        }
    }
    public void updateCustomer(Customer customer) throws SQLException{
        String query="UPDATE customers SET customer_id=?,customer_name=?,address=?,contact=?";
        try(Connection conn=DBconnection.getConnection();
            PreparedStatement stmt =conn.prepareStatement(query)){
                stmt.setInt(1, customer.getCustomerID());
                stmt.setString(2,customer.getCustomerName());
                stmt.setString(3,customer.getAddress());
                stmt.setString(4,customer.getconnect());
                stmt.executeUpdate();
            }
    }
    

}
