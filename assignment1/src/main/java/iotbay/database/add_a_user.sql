-- Script to add a couple of users to the database

INSERT INTO User (UserID, Username, Password, Email, FirstName, LastName, PhoneNumber, Address, CreatedAt, UpdatedAt) 
VALUES 
(1, 'john_doe', 'password123', 'john.doe@example.com', 'John', 'Dang', '0492876890', '123 park road', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Verify the inserted users
SELECT * FROM users;