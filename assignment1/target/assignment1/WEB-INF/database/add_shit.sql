-- Drop all tables
DROP TABLE IF EXISTS User;

-- Create User table
CREATE TABLE User (
    UserID INTEGER PRIMARY KEY AUTOINCREMENT,
    FirstName TEXT NOT NULL,
    LastName TEXT NOT NULL,
    Email TEXT NOT NULL UNIQUE,
    Password TEXT NOT NULL,
    PhoneNumber TEXT
);

SELECT * FROM User WHERE email = 'the_email_you_tried_to_register';