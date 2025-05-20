package iotbay.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator implements Serializable { 
    private String emailPattern = "([a-zA-Z0-9]+)(([._-])([a-zA-Z0-9]+))*(@)([a-z]+)(\\.)([a-z]{3})((([.])[a-z]{0,2})*)";      
    private String namePattern = "([A-Z][a-z]+(\\s))+[A-Z][a-z]*";       
    private String passwordPattern = "[a-z0-9]{4,}";     
    private String cardNumberPattern = "\\d{16}";
    private String cvvPattern = "\\d{3,4}";
    private String expiryDatePattern = "\\d{4}-\\d{2}-\\d{2}";

    public Validator() { }       

    // Generic validator using regex.
    public boolean validate(String pattern, String input) {       
        Pattern regEx = Pattern.compile(pattern);       
        Matcher match = regEx.matcher(input);       
        return match.matches(); 
    }       

    public boolean checkEmpty(String input) {       
        return input == null || input.trim().isEmpty();   
    }

    public boolean validateEmail(String email) {                       
        return email != null && validate(emailPattern, email);   
    }

    public boolean validateName(String name) {
        return name != null && validate(namePattern, name); 
    }       
   
    public boolean validatePassword(String password) {
        return password != null && validate(passwordPattern, password); 
    }
   
   
    public boolean validateCardNumber(String cardNumber) {
        return cardNumber != null && validate(cardNumberPattern, cardNumber);
    }

 
    public boolean validateCVV(String cvv) {
        return cvv != null && validate(cvvPattern, cvv);
    }

 
    public boolean validateExpiryDateFormat(String expiryDate) {
        return expiryDate != null && validate(expiryDatePattern, expiryDate);
    }

    public boolean validateExpiryDateFuture(String expiryDate) {
        if (!validateExpiryDateFormat(expiryDate)) {
            return false;
        }
        try {
            LocalDate expDate = LocalDate.parse(expiryDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate currentDate = LocalDate.now();
            return expDate.isAfter(currentDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    public boolean validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null) return false;
        return paymentMethod.equals("Visa") || paymentMethod.equals("MasterCard") ||
               paymentMethod.equals("American Express") || paymentMethod.equals("Discover");
    }

    public boolean validatePaymentFields(String paymentMethod, String cardHolderName, 
                                         String cardNumber, String cvv, String expiryDate) {
        boolean validMethod = validatePaymentMethod(paymentMethod);
        boolean validHolder = !checkEmpty(cardHolderName);
        boolean validNumber = validateCardNumber(cardNumber);
        boolean validCVV = validateCVV(cvv);
        boolean validExpiry = validateExpiryDateFuture(expiryDate);
        return validMethod && validHolder && validNumber && validCVV && validExpiry;
    }

    public boolean validateCardHolderName(String name) {
    return name != null && !name.trim().isEmpty();
}

}
