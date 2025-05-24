package iotbay;


import static org.junit.jupiter.api.Assertions.*;

import iotbay.model.Payment;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import iotbay.dao.PaymentDAO;

public class PaymentManagementTest {

    private static Connection conn;
    private PaymentDAO paymentDAO;

    @BeforeAll
    public static void initDB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        try (Statement stmt = conn.createStatement()) {
            String createTableSQL = "CREATE TABLE Payment ("
                    + "paymentId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "orderId INT, "
                    + "paymentMethod VARCHAR(50), "
                    + "cardHolderName VARCHAR(100), "
                    + "cardNumber VARCHAR(50), "
                    + "cvv VARCHAR(10), "
                    + "expiryDate VARCHAR(20), "
                    + "amount DOUBLE, "
                    + "userEmail VARCHAR(100)"
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
        paymentDAO = new PaymentDAO(conn);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Payment");
        }
    }

    /**
     * [408] Save a payment method (Customer)
     */
    @Test
    public void testSavePaymentMethod() throws SQLException {
        Payment payment = new Payment(
            0, 0, "Visa", "Ibrahim Raja", 
            "1234567812345678", "123", "09/25", 0.0, 
            "ibrahim@example.com"
        );
        paymentDAO.addPayment(payment);

        List<Payment> payments = paymentDAO.findPaymentMethods("ibrahim@example.com");
        assertEquals(1, payments.size(), "There should be one saved payment method.");

        Payment savedPayment = payments.get(0);
        assertEquals("Ibrahim Raja", savedPayment.getCardHolderName(), "Card holder name should match.");
        assertEquals("1234567812345678", savedPayment.getCardNumber());
        
        String maskedCard = savedPayment.getCardNumber().replaceAll(".(?=.{4})", "*");
        assertEquals("************5678", maskedCard, "Card number should be masked correctly.");
    }

    /**
     * [415] View all my saved payment methods (Customer)
     */
    @Test
    public void testViewPaymentMethods() throws SQLException {
        Payment p1 = new Payment(0, 0, "Visa", "Ibrahim Raja", 
            "1111222233334444", "123", "09/25", 0.0, "ibrahim@example.com");
        Payment p2 = new Payment(0, 0, "MasterCard", "Ibrahim Raja", 
            "5555666677778888", "456", "10/25", 0.0, "ibrahim@example.com");

        paymentDAO.addPayment(p1);
        paymentDAO.addPayment(p2);

        List<Payment> payments = paymentDAO.findPaymentMethods("ibrahim@example.com");
        assertEquals(2, payments.size(), "There should be two saved payment methods.");
        
        for (Payment pm : payments) {
            assertNotNull(pm.getPaymentMethod());
            assertNotNull(pm.getCardHolderName());
            assertNotNull(pm.getCardNumber());
            String masked = pm.getCardNumber().replaceAll(".(?=.{4})", "*");
            String last4 = pm.getCardNumber().substring(pm.getCardNumber().length() - 4);
            assertTrue(masked.endsWith(last4));
            assertNotNull(pm.getExpiryDate());
        }
    }

    /**
     * [416] Update my saved payment method (Customer)
     */
    @Test
    public void testUpdatePaymentMethod() throws SQLException {
        Payment p = new Payment(0, 0, "Visa", "Ibrahim Raja", 
            "1111222233334444", "123", "09/25", 0.0, "ibrahim@example.com");
        paymentDAO.addPayment(p);
        
        List<Payment> payments = paymentDAO.findPaymentMethods("ibrahim@example.com");
        Payment stored = payments.get(0);
        
        stored.setCardHolderName("Ibrahim Updated");
        stored.setExpiryDate("10/25");
        paymentDAO.updatePayment(stored);
        
        Payment updated = paymentDAO.findPayment(stored.getPaymentId());
        assertEquals("Ibrahim Updated", updated.getCardHolderName(), "Card holder name should be updated.");
        assertEquals("10/25", updated.getExpiryDate(), "Expiry date should be updated.");
    }

    /**
     * [417] Delete a saved payment method (Customer)
     */
    @Test
    public void testDeletePaymentMethod() throws SQLException {
        Payment p = new Payment(0, 0, "Visa", "Ibrahim Raja", 
            "1111222233334444", "123", "09/25", 0.0, "ibrahim@example.com");
        paymentDAO.addPayment(p);
        
        List<Payment> paymentsBefore = paymentDAO.findPaymentMethods("ibrahim@example.com");
        assertEquals(1, paymentsBefore.size(), "One payment method should exist before deletion.");
        
        Payment toDelete = paymentsBefore.get(0);
        paymentDAO.deletePayment(toDelete.getPaymentId());
        
        List<Payment> paymentsAfter = paymentDAO.findPaymentMethods("ibrahim@example.com");
        assertEquals(0, paymentsAfter.size(), "No payment methods should exist after deletion.");
    }

    /**
     * [418] Search/Filter Saved Payment Methods (Customer)
     */
    @Test
    public void testSearchFilteredPaymentMethods() throws SQLException {
    
        String userEmail = "john@example.com";
    
        Payment payment1 = new Payment(0, 0, "Visa", "John Doe", 
                "1111222233334444", "123", "09/25", 0.0, userEmail);
        Payment payment2 = new Payment(0, 0, "MasterCard", "Johnny Bravo", 
                "5555666677778888", "456", "10/25", 0.0, userEmail);
        Payment payment3 = new Payment(0, 0, "Amex", "Jenny Doe", 
                "9999888877776666", "789", "11/25", 0.0, userEmail);

        paymentDAO.addPayment(payment1);
        paymentDAO.addPayment(payment2);
        paymentDAO.addPayment(payment3);
        
    
        List<Payment> allPayments = paymentDAO.findPaymentMethods(userEmail);
        
    
        String searchName = "John Doe";
        final String searchNameLower = searchName.trim().toLowerCase();
        List<Payment> filteredByName = allPayments.stream().filter(pm -> {
            boolean nameMatches = pm.getCardHolderName().toLowerCase().contains(searchNameLower);
            boolean cardMatches = false;
            String cardNumber = pm.getCardNumber(); 
            if (cardNumber != null && cardNumber.length() >= 4) {
                String lastFour = cardNumber.substring(cardNumber.length() - 4);
                cardMatches = lastFour.contains(searchName);
            }
            return nameMatches || cardMatches;
        }).collect(Collectors.toList());
        assertEquals(1, filteredByName.size(), "Search for 'John Doe' should return exactly one matching payment.");
        assertEquals("John Doe", filteredByName.get(0).getCardHolderName(),
                "The returned payment's cardholder name should match 'John Doe'.");

        String searchDigits = "8888"; 
        List<Payment> filteredByCard = allPayments.stream().filter(pm -> {
            boolean nameMatches = pm.getCardHolderName().toLowerCase().contains(searchDigits.toLowerCase());
            boolean cardMatches = false;
            String cardNumber = pm.getCardNumber();
            if (cardNumber != null && cardNumber.length() >= 4) {
                String lastFour = cardNumber.substring(cardNumber.length() - 4);
                cardMatches = lastFour.contains(searchDigits);
            }
            return nameMatches || cardMatches;
        }).collect(Collectors.toList());
        assertEquals(1, filteredByCard.size(), "Search for '8888' should return exactly one matching payment.");
        assertEquals("5555666677778888", filteredByCard.get(0).getCardNumber(),
                "The returned payment's card number should match the search query.");

        String searchNone = "notfound";
        List<Payment> filteredNone = allPayments.stream().filter(pm -> {
            boolean nameMatches = pm.getCardHolderName().toLowerCase().contains(searchNone.toLowerCase());
            boolean cardMatches = false;
            String cardNumber = pm.getCardNumber();
            if (cardNumber != null && cardNumber.length() >= 4) {
                String lastFour = cardNumber.substring(cardNumber.length() - 4);
                cardMatches = lastFour.contains(searchNone);
            }
            return nameMatches || cardMatches;
        }).collect(Collectors.toList());
        assertTrue(filteredNone.isEmpty(), "Search for 'notfound' should return zero results.");


        Payment toDelete = filteredByCard.get(0);
        paymentDAO.deletePayment(toDelete.getPaymentId());
    
        List<Payment> updatedPayments = paymentDAO.findPaymentMethods(userEmail);
        List<Payment> filteredAfterDeletion = updatedPayments.stream().filter(pm -> {
            boolean nameMatches = pm.getCardHolderName().toLowerCase().contains(searchDigits.toLowerCase());
            boolean cardMatches = false;
            String cardNumber = pm.getCardNumber();
            if (cardNumber != null && cardNumber.length() >= 4) {
                String lastFour = cardNumber.substring(cardNumber.length() - 4);
                cardMatches = lastFour.contains(searchDigits);
            }
            return nameMatches || cardMatches;
        }).collect(Collectors.toList());
        assertTrue(filteredAfterDeletion.isEmpty(), "After deletion, search for '8888' should yield no results.");
    }


    /**
     * [503] Save multiple payment methods (Customer)
     */
    @Test
    public void testSaveMultiplePaymentMethods() throws SQLException {
        Payment p1 = new Payment(0, 0, "Visa", "Ibrahim Raja", 
            "1111222233334444", "123", "09/25", 0.0, "ibrahim@example.com");
        Payment p2 = new Payment(0, 0, "MasterCard", "Ibrahim Raja", 
            "5555666677778888", "456", "10/25", 0.0, "ibrahim@example.com");
        paymentDAO.addPayment(p1);
        paymentDAO.addPayment(p2);
        
        List<Payment> payments = paymentDAO.findPaymentMethods("ibrahim@example.com");
        assertEquals(2, payments.size(), "Both payment methods should be saved.");
        
    
        List<String> brands = payments.stream()
            .map(Payment::getPaymentMethod)
            .collect(Collectors.toList());
        assertTrue(brands.contains("Visa"));
        assertTrue(brands.contains("MasterCard"));
    }
}
