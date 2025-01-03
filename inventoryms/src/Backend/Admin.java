package Backend;

public class Admin extends human{
    public Admin(String username, String password){
        super(username, password);
        this.password = password;
    }
    public String getusername(){
        return username;
    }
    public String getpassword(){
        return password;
    }
}