package iotbay;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.*;

import iotbay.dao.ShipmentDAO;
import iotbay.model.Shipment;

public class ShipmentManagementTest {

    private static Connection conn;
    private ShipmentDAO shipmentDAO;
    private static final String TEST_USER_EMAIL = "test@example.com";

    @BeforeAll
    public static void initDB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement stmt = conn.createStatement()) {
            String createTableSQL = "CREATE TABLE Shipment (" +
                    "shipmentId INT AUTO_INCREMENT PRIMARY KEY, " +
                    "orderId INT NOT NULL, " +
                    "shipmentMethod VARCHAR(50) NOT NULL, " +
                    "shipmentDate VARCHAR(20) NOT NULL, " +
                    "address VARCHAR(255) NOT NULL, " +
                    "status VARCHAR(20) NOT NULL, " +
                    "userEmail VARCHAR(100) NOT NULL" +
                    ")";
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
        shipmentDAO = new ShipmentDAO(conn);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Shipment");
        }
    }

    /**
     * US-402: As a Customer, I want to be able to input my shipping address, 
     * so that the company knows where to deliver the order
     */
    @Test
    public void testCreateShipmentWithAddress() throws SQLException {
        // Create a test shipment with address
        Shipment shipment = new Shipment();
        shipment.setOrderId(12345);
        shipment.setShipmentMethod("Standard Shipping");
        shipment.setShipmentDate(LocalDate.now().plusDays(3).toString());
        shipment.setAddress("123 Test Street, Test City, Test Country");
        shipment.setStatus("Pending");
        shipment.setUserEmail(TEST_USER_EMAIL);
        
        // Save to database
        boolean created = shipmentDAO.createShipment(shipment);
        
        // Verify shipment was created successfully
        assertTrue(created, "Shipment should be created successfully");
        assertNotEquals(0, shipment.getShipmentId(), "Shipment ID should be assigned");
        
        // Retrieve the shipment and verify address was saved
        Shipment savedShipment = shipmentDAO.findShipment(shipment.getShipmentId());
        assertNotNull(savedShipment, "Saved shipment should exist");
        assertEquals("123 Test Street, Test City, Test Country", savedShipment.getAddress(), 
                "Address should be saved correctly");
    }
    
    /**
     * US-404 & US-504: As a Customer, I want to be able to save my shipping information, 
     * so that I can reuse it later for convenience
     */
    @Test
    public void testSaveAndReuseShippingInformation() throws SQLException {
        // Create multiple shipments for the same user with different addresses
        Shipment shipment1 = createTestShipment(TEST_USER_EMAIL, "123 Home Street, Home City", "Standard Shipping");
        Shipment shipment2 = createTestShipment(TEST_USER_EMAIL, "456 Work Avenue, Work City", "Express Shipping");
        
        // Save both shipments
        shipmentDAO.createShipment(shipment1);
        shipmentDAO.createShipment(shipment2);
        
        // Retrieve all shipments for the user
        List<Shipment> userShipments = shipmentDAO.findShipmentsByUser(TEST_USER_EMAIL);
        
        // Verify both addresses were saved
        assertEquals(2, userShipments.size(), "User should have two shipments");
        
        // Verify we can retrieve all saved addresses for this user
        boolean homeAddressFound = false;
        boolean workAddressFound = false;
        
        for (Shipment s : userShipments) {
            if ("123 Home Street, Home City".equals(s.getAddress())) {
                homeAddressFound = true;
            }
            if ("456 Work Avenue, Work City".equals(s.getAddress())) {
                workAddressFound = true;
            }
        }
        
        assertTrue(homeAddressFound, "Home address should be saved");
        assertTrue(workAddressFound, "Work address should be saved");
    }
    
    /**
     * US-413: As an Anonymous User, I want to track my order using a provided tracking number, 
     * so that I can follow delivery progress without logging in
     */
    @Test
    public void testTrackOrderWithTrackingNumber() throws SQLException {
        // Create a test shipment
        Shipment shipment = createTestShipment(TEST_USER_EMAIL, "123 Test Street", "Standard Shipping");
        // Note: shipment will have "Pending" status by default from createTestShipment or will be set to "Pending" by DAO
        shipmentDAO.createShipment(shipment);
        
        int trackingNumber = shipment.getShipmentId();
        
        // Retrieve shipment using only the tracking number (shipmentId)
        Shipment trackedShipment = shipmentDAO.findShipment(trackingNumber);
        
        // Verify shipment can be tracked without user authentication
        assertNotNull(trackedShipment, "Shipment should be trackable by ID alone");
        assertEquals("Pending", trackedShipment.getStatus(), "Status should be retrievable");
    }
    
    /**
     * US-406: As a Customer, I want to be able to add multiple shipping addresses, 
     * so that I can choose where my orders are delivered
     */
    @Test
    public void testAddMultipleShippingAddresses() throws SQLException {
        // Create three shipments with different addresses
        Shipment shipment1 = createTestShipment(TEST_USER_EMAIL, "123 Home Street", "Standard Shipping");
        Shipment shipment2 = createTestShipment(TEST_USER_EMAIL, "456 Work Avenue", "Express Shipping");
        Shipment shipment3 = createTestShipment(TEST_USER_EMAIL, "789 Parent's House", "Priority Shipping");
        
        // Save all shipments
        shipmentDAO.createShipment(shipment1);
        shipmentDAO.createShipment(shipment2);
        shipmentDAO.createShipment(shipment3);
        
        // Retrieve all shipments for the user
        List<Shipment> userShipments = shipmentDAO.findShipmentsByUser(TEST_USER_EMAIL);
        
        // Verify all addresses were saved
        assertEquals(3, userShipments.size(), "User should have three shipments");
        
        // Verify each unique address is present
        boolean address1Found = false;
        boolean address2Found = false;
        boolean address3Found = false;
        
        for (Shipment s : userShipments) {
            if ("123 Home Street".equals(s.getAddress())) address1Found = true;
            if ("456 Work Avenue".equals(s.getAddress())) address2Found = true;
            if ("789 Parent's House".equals(s.getAddress())) address3Found = true;
        }
        
        assertTrue(address1Found && address2Found && address3Found, 
                "All three different addresses should be saved and retrievable");
    }
    
    /**
     * US-403: As a Customer, I want to be able to choose pickup or delivery, 
     * so that I can have the option to pick up from the store to save on delivery costs, 
     * or get it delivered at an extra cost
     */
    @Test
    public void testShipmentMethodSelection() throws SQLException {
        // Create shipments with different methods
        Shipment standardDelivery = createTestShipment(TEST_USER_EMAIL, "123 Home Address", "Standard Shipping");
        Shipment expressDelivery = createTestShipment(TEST_USER_EMAIL, "123 Home Address", "Express Shipping");
        Shipment storePickup = createTestShipment(TEST_USER_EMAIL, "Store #123 - Sydney CBD", "In-Store Pickup");
        
        // Save all shipments
        shipmentDAO.createShipment(standardDelivery);
        shipmentDAO.createShipment(expressDelivery);
        shipmentDAO.createShipment(storePickup);
        
        // Retrieve and verify each shipment
        Shipment savedStandard = shipmentDAO.findShipment(standardDelivery.getShipmentId());
        Shipment savedExpress = shipmentDAO.findShipment(expressDelivery.getShipmentId());
        Shipment savedPickup = shipmentDAO.findShipment(storePickup.getShipmentId());
        
        // Verify the shipment methods were saved correctly
        assertEquals("Standard Shipping", savedStandard.getShipmentMethod(), 
                "Standard shipping method should be saved correctly");
        assertEquals("Express Shipping", savedExpress.getShipmentMethod(), 
                "Express shipping method should be saved correctly");
        assertEquals("In-Store Pickup", savedPickup.getShipmentMethod(), 
                "Pickup method should be saved correctly");
    }
    
    /**
     * US-405: As a Customer, I want to be able to cancel shipping orders, 
     * so that I can refund the products
     */
    @Test
    public void testCancelPendingShipment() throws SQLException {
        // Create a pending shipment
        Shipment pendingShipment = createTestShipment(TEST_USER_EMAIL, "123 Test Street", "Standard Shipping");
        pendingShipment.setStatus("Pending");
        shipmentDAO.createShipment(pendingShipment);
        
        // Verify shipment exists
        Shipment savedShipment = shipmentDAO.findShipment(pendingShipment.getShipmentId());
        assertNotNull(savedShipment, "Shipment should exist before deletion");
        
        // Delete/cancel the shipment
        boolean cancelled = shipmentDAO.deleteShipment(pendingShipment.getShipmentId());
        
        // Verify cancellation
        assertTrue(cancelled, "Pending shipment should be cancellable");
        
        // Verify shipment no longer exists
        Shipment deletedShipment = shipmentDAO.findShipment(pendingShipment.getShipmentId());
        assertNull(deletedShipment, "Shipment should be deleted after cancellation");
    }
    
    /**
     * US-405: Additional test to verify that completed shipments cannot be cancelled
     */
    @Test
    public void testCannotCancelCompletedShipment() throws SQLException {
        // This test would simulate the business logic in ShipmentServlet where it checks
        // if a shipment can be deleted based on its status
        
        // Create a completed shipment
        Shipment completedShipment = createTestShipment(TEST_USER_EMAIL, "123 Test Street", "Standard Shipping");
        completedShipment.setStatus("Complete");
        shipmentDAO.createShipment(completedShipment);
        
        // In a real application, this logic would be in the service layer
        // Here we're just simulating it
        Shipment shipment = shipmentDAO.findShipment(completedShipment.getShipmentId());
        boolean canCancel = "Pending".equals(shipment.getStatus());
        
        // Verify that a completed shipment cannot be cancelled
        assertFalse(canCancel, "Completed shipments should not be cancellable");
    }
    
    /**
     * Helper method to create a test shipment
     */
    private Shipment createTestShipment(String userEmail, String address, String method) {
        Shipment shipment = new Shipment();
        shipment.setOrderId(12345);
        shipment.setShipmentMethod(method);
        shipment.setShipmentDate(LocalDate.now().plusDays(3).toString());
        shipment.setAddress(address);
        shipment.setStatus("Pending");
        shipment.setUserEmail(userEmail);
        return shipment;
    }
}