package Backend;

public class Supplier {
    private int supplierID; // Assuming suppliers have a unique ID
    private String supplierName;
    private String contact;
    private String address;
   

    public Supplier(int supplierID, String supplierName, String contact, String address) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.contact = contact;
        this.address = address;
        
    }

    // Getters and Setters
    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
}