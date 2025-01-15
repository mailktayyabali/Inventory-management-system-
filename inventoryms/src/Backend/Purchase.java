package Backend;

public class Purchase {
    int PurchaseId;
    String ProductName;
    int Quantity;
    double PurchasePrice;
    double SalePrice;
    public void Purchase(int PurchaseId,String ProductName, int Quantity, double PurchasePrice, double SalePrice) {
        this.PurchaseId=PurchaseId;
        this.ProductName = ProductName;
        this.Quantity = Quantity;
        this.PurchasePrice = PurchasePrice;
        this.SalePrice = SalePrice;
    }
    public int getPurchaseId(){
        return PurchaseId;
    }
    
    public String getProductName() {
        return ProductName;
    }
    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }
    public int getQuantity() {
        return Quantity;
    }
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
    public double getPurchasePrice() {
        return PurchasePrice;
    }
    public void setPurchasePrice(double PurchasePrice) {
        this.PurchasePrice = PurchasePrice;
    }
    public double getSalePrice() {
        return SalePrice;
    }
    public void setSalePrice(double SalePrice) {
        this.SalePrice = SalePrice;
    }
    
}
