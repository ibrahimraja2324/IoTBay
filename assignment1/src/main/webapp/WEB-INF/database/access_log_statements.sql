DROP TABLE IF EXISTS access_log;

CREATE TABLE AccessLog (
    LogID INTEGER PRIMARY KEY AUTOINCREMENT,
    Email TEXT NOT NULL,
    Action TEXT NOT NULL,
    Role TEXT NOT NULL,
    Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Email) REFERENCES User(email)
);

-- Insert sample data into AccessLog
INSERT INTO AccessLog (Email, Action, Role) VALUES
('alice@example.com', 'Login', 'Customer'),
('bob@example.com', 'Logout', 'Staff'),
('carol@example.com', 'ViewProduct', 'Customer'),
('dave@example.com', 'UpdateInventory', 'Staff'),
('eve@example.com', 'Login', 'Admin')