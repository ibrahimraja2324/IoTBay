package iotbay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.UUID;

import iotbay.model.User;
import iotbay.dao.UserDAO;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserManagementTest {

    private static Connection conn;
    private UserDAO userDAO;

    @BeforeAll
    public static void initDB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement stmt = conn.createStatement()) {
            String createTableSQL = "CREATE TABLE \"User\" ("
                    + "Email VARCHAR(100) PRIMARY KEY, "
                    + "Password VARCHAR(100), "
                    + "FirstName VARCHAR(100), "
                    + "LastName VARCHAR(100), "
                    + "PhoneNumber VARCHAR(20), "
                    + "Role VARCHAR(20), "
                    + "IsActive BOOLEAN DEFAULT 1"
                    + ")";
            stmt.execute(createTableSQL);
        }
    }

    @AfterAll
    public static void closeDB() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        userDAO = new UserDAO(conn);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM \"User\"");
        }
    }

    private String generateRandomEmail() {
        return "testuser_" + UUID.randomUUID().toString().replace("-", "") + "@example.com";
    }

    @Test
    public void testRegisterNewAccount() throws SQLException {
        String email = generateRandomEmail();
        User user = new User("John", "Smith", email, "1234", "0101924567", "USER");
        userDAO.registerUser(user);

        User registeredUser = userDAO.findUser(email);
        assertNotNull(registeredUser);
        assertEquals("John", registeredUser.getFirstName());
        assertEquals("Smith", registeredUser.getLastName());
        assertEquals(email, registeredUser.getEmail());
        assertEquals("1234", registeredUser.getPassword());
        assertEquals("0101924567", registeredUser.getPhone());
    }

    @Test
    public void testLoginToAccount() throws SQLException {
        String email = generateRandomEmail();
        User user = new User("Jane", "Smith", email, "securepass", "0411111111", "USER");
        userDAO.registerUser(user);

        User loggedInUser = userDAO.login(email, "securepass");
        assertNotNull(loggedInUser);
        assertEquals(email, loggedInUser.getEmail());
    }

    @Test
    public void testUpdateProfileInformation() throws SQLException {
        String email = generateRandomEmail();
        User user = new User("Alex", "Brown", email, "oldpass", "0433333333", "USER");
        userDAO.registerUser(user);

        boolean updated = userDAO.updateProfile(user, "Alexander", "Brown", "0433333333", "newpass", "oldpass");
        assertTrue(updated);
    }

    @Test
    public void testDeleteAccount() throws SQLException {
        String email = generateRandomEmail();
        User user = new User("Chris", "Doe", email, "deleteMe", "0455555555", "USER");
        userDAO.registerUser(user);

        boolean deleted = userDAO.deleteAccount(user, "deleteMe");
        assertTrue(deleted);
        assertNull(userDAO.login(email, "deleteMe"));
    }

    @Test
    public void testToggleAccountStatus() throws SQLException {
        String email = generateRandomEmail();
        User user = new User("Sam", "Smith", email, "passw0rd", "0466666666", "USER");
        userDAO.registerUser(user);

        boolean toggled = userDAO.toggleAccountStatus(user);
        assertTrue(toggled);
    }
}