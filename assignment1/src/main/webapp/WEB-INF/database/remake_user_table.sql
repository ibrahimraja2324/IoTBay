DROP TABLE IF EXISTS "User";

CREATE TYPE UserRole AS ENUM ('ADMIN', 'STAFF', 'USER');

PRAGMA journal_mode=DELETE;

CREATE TABLE "Users" (
    Email VARCHAR(100) PRIMARY KEY,
    Password VARCHAR(100) NOT NULL,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    PhoneNumber VARCHAR(20),
    IsActive BOOLEAN DEFAULT TRUE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Role VARCHAR(20) DEFAULT 'USER' NOT NULL
);

INSERT INTO "Users" (Email, Password, FirstName, LastName, PhoneNumber, IsActive, CreatedAt, UpdatedAt, Role) VALUES
('a@a.com', 'aaaa', 'Carol', 'Smith', '3456789012', TRUE, '2025-05-20 03:17:07', '2025-05-20 03:17:07', 'USER');

INSERT INTO "Users" (Email, Password, FirstName, LastName, PhoneNumber, IsActive, CreatedAt, UpdatedAt, Role) VALUES
('admin@example.com', 'admin123', 'Admin', 'User', '0000000000', TRUE, '2025-05-23 01:59:45', '2025-05-23 01:59:45', 'ADMIN');

INSERT INTO "Users" (Email, Password, FirstName, LastName, PhoneNumber, IsActive, CreatedAt, UpdatedAt, Role) VALUES
('staff@acc.com', 'staff123', 'Staff', 'Account', '1234567890', TRUE, '2025-05-25 03:02:03', '2025-05-25 03:02:03', 'STAFF');