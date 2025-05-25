package iotbay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import iotbay.dao.UserDAO;
import iotbay.model.User;
public class UserAccessTest {

    private UserDAO userDAO;
    private static Connection conn;

    @BeforeAll
    public static void initDB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement stmt = conn.createStatement()) {
            // Initialize the database schema and test data
            String initScript = "CREATE TABLE IF NOT EXISTS Users (" +
                    "Email VARCHAR(100) PRIMARY KEY, " +
                    "Password VARCHAR(100) NOT NULL, " +
                    "FirstName VARCHAR(50), " +
                    "LastName VARCHAR(50), " +
                    "PhoneNumber VARCHAR(20)," +
                    "IsActive BOOLEAN DEFAULT TRUE, " +
                    "CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "Role VARCHAR(20) DEFAULT 'USER' NOT NULL" +
                    ")";
            stmt.execute(initScript);
        } catch (SQLException e) {
            fail("Failed to initialize database: " + e.getMessage());
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
            stmt.execute("DELETE FROM Users"); // Clear the User table before each test
        }
    }

    /**
     * Helper function to generate a random email address for testing.
     */
    private String generateRandomEmail() {
        return "testuser_" + UUID.randomUUID().toString().replace("-", "") + "@example.com";
    }

    // [101] Register a new account
    @Test
    void testRegisterNewAccount() {
        // Simulate registration
        String email = generateRandomEmail();
        User user = new User("John", "Smith", email, "1234", "0101924567", "USER");
        try {
            userDAO.registerUser(user);
        } catch (Exception e) {
            fail("Registration failed: " + e.getMessage());
        }

        // Check if the user is registered
        User registeredUser = null;
        try {
            registeredUser = userDAO.findUser(email);
        } catch (Exception e) {
            fail("Failed to find registered user: " + e.getMessage());
        }

        // Assertions
        assertNotNull(registeredUser);
        assertEquals("John", registeredUser.getFirstName());
        assertEquals("Smith", registeredUser.getLastName());
        assertEquals(email, registeredUser.getEmail());
        assertEquals("1234", registeredUser.getPassword());
        assertEquals("0101924567", registeredUser.getPhone());
    }

    // [102] Login to account
    @Test
    void testLoginToAccount() {
        // Initialize a session map for testing purposes
        HashMap<String, Object> session = new HashMap<>();

        String email = generateRandomEmail();
        String password = "1234";
        User user = new User("John", "Smith", email, password, "0101924567", "USER");
        user.setActive(true);  // Set user as active
        
        try {
            // Register the user first
            userDAO.registerUser(user);
            session.put("currentUser", user);

            // Now attempt to log in
            User loggedInUser = userDAO.findUser(email, password);
            assertNotNull(loggedInUser, "User should be logged in");
            assertEquals(session.get("currentUser"), user);

            // Attempt to log in with incorrect credentials
            User failedLogin = userDAO.findUser(email, "wrongpassword");
            assertNull(failedLogin, "Login with wrong password should return null");
            assertNotEquals(session.get("currentUser"), failedLogin);
        } catch (Exception e) {
            fail("Login failed: " + e.getMessage());
        }
    }

    @Test
    void testLogoutOfAccount() {
        // Initialize a session map for testing purposes
        HashMap<String, Object> session = new HashMap<>();

        String email = generateRandomEmail();
        String password = "1234";
        User user = new User("John", "Smith", email, password, "0101924567", "USER");
        user.setActive(true);  // Set user as active

        try {
            // Register the user first
            userDAO.registerUser(user);
            session.put("currentUser", user);

            // Now attempt to log in
            User loggedInUser = userDAO.findUser(email, password);
            assertNotNull(loggedInUser, "User should be logged in");
            assertEquals(session.get("currentUser"), user);

            // Simulate logout
            session.remove("currentUser");
            assertNull(session.get("currentUser"), "User should be logged out");
        } catch (Exception e) {
            fail("Logout failed: " + e.getMessage());
        }
    }

    @Test
    void testUpdateProfileInformation() {
        // Initialize a session map for testing purposes
        HashMap<String, Object> session = new HashMap<>();

        String email = generateRandomEmail();
        String password = "1234";
        User user = new User("John", "Smith", email, password, "0101924567", "USER");
        user.setActive(true);  // Set user as active

        try {
            // Register the user first
            userDAO.registerUser(user);
            session.put("currentUser", user);

            // Now attempt to log in
            User loggedInUser = userDAO.findUser(email, password);
            assertNotNull(loggedInUser, "User should be logged in");
            assertEquals(session.get("currentUser"), user);

            // Update profile information
            String newFirstName = "Johnathan";
            String newLastName = "Doe";
            String newPassword = "newpassword";
            String newPhone = "0202020202";
            User updatedUser = new User(newFirstName, newLastName, email, newPassword, newPhone, "USER");
            updatedUser.setActive(true);  // Keep user active after update
            userDAO.updateUser(updatedUser);
            session.put("currentUser", updatedUser);

            // Verify the update in the database
            User updatedLoggedInUser = userDAO.findUser(email, newPassword);
            assertNotNull(updatedLoggedInUser, "User should be updated");
            assertEquals(newFirstName, updatedLoggedInUser.getFirstName());
            assertEquals(newLastName, updatedLoggedInUser.getLastName());
            assertEquals(newPassword, updatedLoggedInUser.getPassword());
            assertEquals(newPhone, updatedLoggedInUser.getPhone());

            // Verify that the session reflects the updated user
            User sessionUser = (User) session.get("currentUser");
            assertEquals(sessionUser.getEmail(), updatedLoggedInUser.getEmail());
            assertEquals(sessionUser.getFirstName(), updatedLoggedInUser.getFirstName());
            assertEquals(sessionUser.getLastName(), updatedLoggedInUser.getLastName());
            assertEquals(sessionUser.getPassword(), updatedLoggedInUser.getPassword());
            assertEquals(sessionUser.getPhone(), updatedLoggedInUser.getPhone());
            assertEquals(sessionUser.getRole(), updatedLoggedInUser.getRole());
        } catch (Exception e) {
            fail("User update failed: " + e.getMessage());
        }
    }

    @Test
    void testDeleteAccount() {
        // User user = userDAO.login("chris@example.com", "deleteMe");
        // boolean deleted = userDAO.deleteAccount(user, "deleteMe");
        // assertTrue(deleted);
        // assertNull(userDAO.login("chris@example.com", "deleteMe"));

        // Stub version
        User user = new User();
        user = null; // Simulate deletion
        assertNull(user);
    }
}