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
    public int getCustomerID(){
        return customerID;
    }
    public String getCustomerName(){
        return customerName;
    }
    public String getAddress(){
        return address;
    }
    public String getcontact(){
        return contact;
    }
}
