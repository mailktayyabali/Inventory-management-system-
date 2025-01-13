package Backend;

public class Supplier {
    private int supplierID; // Assuming suppliers have a unique ID
    private String supplierName;
    private String contact;
    private String product;
    private String paymentStatus;

    public Supplier(int supplierID, String supplierName, String contact, String product, String paymentStatus) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.contact = contact;
        this.product = product;
        this.paymentStatus = paymentStatus;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
