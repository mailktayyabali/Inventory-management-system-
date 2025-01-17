package Backend;

public class Purchase {
    private String purchaseID;          // Unique ID for each purchase
    private String productName;      // Name of the purchased product
    private int quantity;            // Quantity of the product purchased
    private double purchasePrice;    // Price at which the product was purchased
    private double salePrice;        // Price at which the product will be sold
    private String supplierName;          // ID of the supplier

    // Constructor
    public Purchase(String purchaseID, String productName, int quantity, double purchasePrice, double salePrice, String supplierName) {
        this.purchaseID = purchaseID;
        this.productName = productName;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.supplierName = supplierName;
    }

    // Getters and Setters
    public String getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(String purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
