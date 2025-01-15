// package DB;
// import Backend.Purchase;

// import java.sql.*;

// public class PurchaseDAO {
//     public void addPurchase(Purchase purchase) throws SQLException{
//         String query="INSERT INTO purchase (purchase_id, supplier_id, quantity, purchase_date, total_amount) VALUES (?,?,?,?,?)";
//         try(Connection conn=DBconnection.getConnection();
//             PreparedStatement stmt=conn.prepareStatement(query)){
//             stmt.setInt(1,purchase.getPurchaseId());
//             stmt.setInt(3,purchase.getSupplierId());
//             stmt.setInt(4,purchase.getQuantity());
//             stmt.setDate(5, Date.valueOf(purchase.getPurchaseDate()));
//             stmt.setDouble(6,purchase.getTotalAmount());
//             stmt.executeUpdate();
//         }
//     }
// }
