package iotbay.model;

public class User {
    private int userID;
    private String name;
    private String gender;
    private String emailAddress;
    private String favouriteColour;
    private String email;
    private String phone;
    private String fullName;
    private String password;
    private String address1;
    private String address2;
    private String suburb;
    private Integer postcode;
    private String state; 
    private Integer cardNumber;
    private Integer cardExpiry;
    private Integer cardCVV;
    
  
    public User() {
    }
    
 
    public User(String fullName, String email, String phone, String password) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
    
    

    public User(String userEmail, String userName, String userPassword, String userGender, String userFavcol) {
        this.email = userEmail;
        this.name = userName;
        this.password = userPassword;
        this.gender = userGender;
        this.favouriteColour = userFavcol;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
