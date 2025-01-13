package Backend;

public class Customer {
    int customerID;
    String customerName;
    String address;
    String contact;
    public Customer(int customerID,String customerName,String address,String contact){
        this.customerID=customerID;
        this.customerName=customerName;
        this.address=address;
        this.contact=contact;
    }
    public int getConnectionID(){
        return customerID;
    }
    
}
