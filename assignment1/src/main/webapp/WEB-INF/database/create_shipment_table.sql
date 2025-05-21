DROP TABLE IF EXISTS Shipment;

CREATE TABLE IF NOT EXISTS Shipment (
    shipmentId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER NOT NULL,
    shipmentMethod TEXT NOT NULL,
    shipmentDate TEXT NOT NULL,
    address TEXT NOT NULL,
    status TEXT NOT NULL,
    userEmail TEXT NOT NULL,
    FOREIGN KEY (orderId) REFERENCES Orders(orderId),
    FOREIGN KEY (userEmail) REFERENCES User(Email)
);

-- Insert sample data
INSERT INTO Shipment (orderId, shipmentMethod, shipmentDate, address, status, userEmail) VALUES
(1, 'Standard Shipping', '2025-05-15', '123 Main St, Sydney NSW 2000', 'Pending', 'john.doe@example.com'),
(2, 'Express Shipping', '2025-05-16', '456 Park Ave, Melbourne VIC 3000', 'Shipped', 'jane.smith@example.com'),
(3, 'Standard Shipping', '2025-05-17', '789 King St, Brisbane QLD 4000', 'Delivered', 'bob.jones@example.com'),
(1, 'Express Shipping', '2025-05-18', '321 Queen St, Perth WA 6000', 'Processing', 'john.doe@example.com'),
(4, 'Standard Shipping', '2025-05-19', '654 George St, Adelaide SA 5000', 'Pending', 'alice.brown@example.com');