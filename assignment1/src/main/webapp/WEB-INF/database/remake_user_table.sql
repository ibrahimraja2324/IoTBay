DROP TABLE IF EXISTS "User";

CREATE TYPE UserRole AS ENUM ('ADMIN', 'USER');

CREATE TABLE "User" (
    Email VARCHAR(100) PRIMARY KEY,
    Password VARCHAR(100) NOT NULL,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    PhoneNumber VARCHAR(20),
    IsActive BOOLEAN DEFAULT TRUE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Role UserRole DEFAULT 'USER' NOT NULL
);

-- Insert sample data into User
INSERT INTO "User" (Email, Password, FirstName, LastName, PhoneNumber, IsActive, Role)
VALUES
    ('a@a.com', 'aaaa', 'Carol', 'Smith', '3456789012', TRUE, 'USER');