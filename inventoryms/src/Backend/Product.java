package Backend;

public class Product {
    private String productID;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String categoryName;

    public Product(String productID, String name, String description, double price, int stockQuantity, String categoryName) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryName = categoryName;
    }

    // Getters and setters
    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
