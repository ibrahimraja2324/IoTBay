-- Script to add a test user into the User table

-- If you already have data in the table and UserID is meant to be auto-generated,
-- you might not include the UserID here. If it's not auto-generated, then include it.
INSERT INTO User (UserID, FirstName, LastName, Email, Password, PhoneNumber)
VALUES (1, 'John', 'Doe', 'john.doe@example.com', 'password123', '0492876890');

-- Verify the inserted record by selecting from the table:
SELECT * FROM User;
