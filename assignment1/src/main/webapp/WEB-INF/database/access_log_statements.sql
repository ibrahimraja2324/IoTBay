DROP TABLE IF EXISTS "AccessLog";

CREATE TABLE AccessLog (
    LogID INTEGER PRIMARY KEY AUTOINCREMENT,
    Email TEXT NOT NULL,
    Action TEXT NOT NULL,
    Role TEXT NOT NULL,
    Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data into AccessLog
INSERT INTO AccessLog (Email, Action, Role) VALUES
('alice@example.com', 'Login', 'Customer'),
('bob@example.com', 'Logout', 'Admin'),
('carol@example.com', 'ViewProduct', 'Customer'),
('dave@example.com', 'UpdateInventory', 'Admin'),
('eve@example.com', 'Login', 'Admin')