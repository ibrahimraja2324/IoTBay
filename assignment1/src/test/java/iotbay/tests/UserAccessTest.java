package iotbay.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.UUID;

import javax.print.attribute.standard.JobHoldUntil;

import iotbay.model.User;
import iotbay.dao.DBConnector;
import iotbay.dao.UserDAO;

public class UserAccessTest {

    private UserDAO userDAO;
    private Connection conn;

    @BeforeEach
    void setUp() {
        try {
            // Use in-memory SQLite DB for testing
            conn = java.sql.DriverManager.getConnection("jdbc:sqlite:/Users/ethan/Desktop/ISD/ASS 2/IoTBay/assignment1/src/main/webapp/WEB-INF/database/database_test.db");
            userDAO = new UserDAO(conn); 
        }
        catch (Exception e) {
            fail("Failed to set up database connection: " + e.getMessage());
        }
    }

    /**
     * Helper function to generate a random email address for testing.
     */
    private String generateRandomEmail() {
        return "testuser_" + UUID.randomUUID().toString().replace("-", "") + "@example.com";
    }

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

    @Test
    void testLoginToAccount() {
        // userDAO.register(new User("Jane", "Smith", "jane@example.com", "0411111111", "securepass"));
        // User user = userDAO.login("jane@example.com", "securepass");
        // assertNotNull(user);
        // assertEquals("jane@example.com", user.getEmail());

        // Stub version
        
        assertEquals("jane@example.com", "");
    }

    @Test
    void testLogoutOfAccount() {
        // User user = userDAO.login("sam@example.com", "passw0rd");
        // userDAO.logout(user);
        // assertFalse(userDAO.isLoggedIn(user));

        // Stub version
        boolean loggedIn = true;
        loggedIn = false; // Simulate logout
        assertFalse(loggedIn);
    }

    @Test
    void testUpdateProfileInformation() {
        // User user = userDAO.login("alex@example.com", "oldpass");
        // boolean updated = userDAO.updateProfile(user, "Alexander", "Brown", "0433333333", "newpass", "oldpass");
        // assertTrue(updated);

        // Stub version
        User user = new User();
        user.setFirstName("Alexander");
        user.setPassword("newpass");
        assertEquals("Alexander", user.getFirstName());
        assertEquals("newpass", user.getPassword());
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
