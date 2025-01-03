package Backend;

public class User extends human{
    int userID;
    String name;
    String address;
    String contactNumber;
    public User(String username, String password, int userID, String name, String address, String contactNumber){
        super(username, password);
        this.userID = userID;
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    } 
    public String getusername(){
        return username;
    }
    public String getpassword(){
        return password;
    }   
    public int getuserid(){
        return userID;
    }
    public String getname(){
        return name;
    }
    public String getaddress(){
        return address;
    }
    public String getcontactNumber(){
        return contactNumber;
    }

}
