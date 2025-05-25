package iotbay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import iotbay.dao.UserDAO;
import iotbay.model.User;

public class UserManagementTest {
    private UserDAO userDAO;
    private static Connection conn;
    private User adminUser;

    @BeforeAll
    public static void initDB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement stmt = conn.createStatement()) {
            // Create Users table
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

            // Insert admin user
            String adminScript = "INSERT INTO Users (Email, Password, FirstName, LastName, PhoneNumber, Role, IsActive) " +
                    "VALUES ('admin@example.com', 'adminpass', 'Admin', 'User', '1234567890', 'ADMIN', true)";
            stmt.execute(adminScript);
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
            // Clear all users except admin
            stmt.execute("DELETE FROM Users WHERE Email != 'admin@example.com'");
        }
        
        // Get the existing admin user
        adminUser = userDAO.findUser("admin@example.com");
        assertNotNull(adminUser, "Admin user should exist in database");
        assertEquals("ADMIN", adminUser.getRole(), "Admin user should have ADMIN role");
        assertTrue(adminUser.isActive(), "Admin user should be active");
    }

    private String generateRandomEmail() {
        return "testuser_" + UUID.randomUUID().toString().replace("-", "") + "@example.com";
    }

    @Test
    public void testCreateNewUser() throws SQLException {
        // Create a new user
        String email = generateRandomEmail();
        User newUser = new User("John", "Doe", email, "password123", "1234567890", "USER");
        newUser.setActive(true);
        
        boolean created = userDAO.registerUser(newUser);
        assertTrue(created, "User should be created successfully");
        
        // Verify user exists
        User foundUser = userDAO.findUser(email);
        assertNotNull(foundUser, "Created user should exist in database");
        assertEquals("USER", foundUser.getRole(), "User should have USER role");
        assertTrue(foundUser.isActive(), "User should be active by default");
    }

    @Test
    public void testDeactivateUser() throws SQLException {
        // Create a test user
        String email = generateRandomEmail();
        User testUser = new User("Test", "User", email, "password123", "1234567890", "USER");
        testUser.setActive(true);
        userDAO.registerUser(testUser);
        
        // Deactivate the user
        boolean deactivated = userDAO.toggleUserStatus(email);
        assertTrue(deactivated, "User should be deactivated successfully");
        
        // Verify user is deactivated
        User foundUser = userDAO.findUser(email);
        assertNotNull(foundUser, "User should still exist in database");
        assertFalse(foundUser.isActive(), "User should be deactivated");
    }

    @Test
    public void testReactivateUser() throws SQLException {
        // Create and deactivate a test user
        String email = generateRandomEmail();
        User testUser = new User("Test", "User", email, "password123", "1234567890", "USER");
        testUser.setActive(true);
        userDAO.registerUser(testUser);
        userDAO.toggleUserStatus(email);
        
        // Reactivate the user
        boolean reactivated = userDAO.toggleUserStatus(email);
        assertTrue(reactivated, "User should be reactivated successfully");
        
        // Verify user is reactivated
        User foundUser = userDAO.findUser(email);
        assertNotNull(foundUser, "User should still exist in database");
        assertTrue(foundUser.isActive(), "User should be reactivated");
    }

    @Test
    public void testDeleteUser() throws SQLException {
        // Create a test user
        String email = generateRandomEmail();
        User testUser = new User("Test", "User", email, "password123", "1234567890", "USER");
        testUser.setActive(true);
        userDAO.registerUser(testUser);
        
        // Delete the user
        boolean deleted = userDAO.deleteUser(email);
        assertTrue(deleted, "User should be deleted successfully");
        
        // Verify user is deleted
        User foundUser = userDAO.findUser(email);
        assertNull(foundUser, "User should no longer exist in database");
    }

    @Test
    public void testListAllUsers() throws SQLException {
        // Create multiple test users
        for (int i = 0; i < 3; i++) {
            String email = generateRandomEmail();
            User testUser = new User("Test" + i, "User" + i, email, "password123", "1234567890", "USER");
            testUser.setActive(true);
            userDAO.registerUser(testUser);
        }
        
        // Get all users
        List<User> users = userDAO.getAllUsers();
        
        // Verify results
        assertNotNull(users, "User list should not be null");
        assertEquals(4, users.size(), "Should return all users including admin");
        
        // Verify admin user exists in list
        boolean adminFound = users.stream()
                .anyMatch(user -> "admin@example.com".equals(user.getEmail()));
        assertTrue(adminFound, "Admin user should be in the list");
    }

    @Test
    public void testUpdateUserDetails() throws SQLException {
        // Create a test user
        String email = generateRandomEmail();
        User testUser = new User("Test", "User", email, "password123", "1234567890", "USER");
        testUser.setActive(true);
        userDAO.registerUser(testUser);
        
        // Update user details
        testUser.setFirstName("Updated");
        testUser.setLastName("Name");
        testUser.setPhone("9876543210");
        boolean updated = userDAO.updateUser(testUser);
        assertTrue(updated, "User details should be updated successfully");
        
        // Verify updates
        User foundUser = userDAO.findUser(email);
        assertNotNull(foundUser, "User should still exist in database");
        assertEquals("Updated", foundUser.getFirstName(), "First name should be updated");
        assertEquals("Name", foundUser.getLastName(), "Last name should be updated");
        assertEquals("9876543210", foundUser.getPhone(), "Phone number should be updated");
    }
}
