package iotbay.dao; 
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        DB db = new DB() {};  // Instantiate the abstract class with an anonymous class
        try {
            db.getConnection();
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}