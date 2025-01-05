package Backend;

public class Category {
    String CategoryID;
    String CategoryName;
    public Category(String CategoryID, String CategoryName){
        this.CategoryID = CategoryID;
        this.CategoryName = CategoryName;
    }
    public String getCategoryID(){
        return CategoryID;
    }
    public String getCategoryName(){
        return CategoryName;
    }
}
