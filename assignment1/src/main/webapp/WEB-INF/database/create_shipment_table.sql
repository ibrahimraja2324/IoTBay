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
(1, 'Standard Shipping', '2024-05-01', '123 Main St, Sydney', 'Pending', 'a@a.com'), 
(2, 'Express Shipping', '2024-05-02', '456 King St, Melbourne', 'Complete', 'a@a.com'), 
(3, 'Priority Shipping', '2024-05-03', '789 Queen St, Brisbane', 'Complete', 'a@a.com'), 
(4, 'Next Day Delivery', '2024-05-04', '101 George St, Perth', 'Complete', 'dave@example.com'), 
(5, 'Standard Shipping', '2024-05-05', '202 Pitt St, Adelaide', 'Pending', 'eve@example.com'), 
(6, 'Express Shipping', '2024-05-06', '303 Collins St, Hobart', 'Pending', 'frank@example.com'), 
(7, 'Priority Shipping', '2024-05-07', '404 Bourke St, Darwin', 'Complete', 'grace@example.com'), 
(8, 'Next Day Delivery', '2024-05-08', '505 Swanston St, Canberra', 'Complete', 'heidi@example.com'), 
(9, 'Standard Shipping', '2024-05-09', '606 Elizabeth St, Sydney', 'Pending', 'ivan@example.com'), 
(10, 'Express Shipping', '2024-05-10', '707 Lygon St, Melbourne', 'Pending', 'judy@example.com'), 
(11, 'Priority Shipping', '2024-05-11', '808 Chapel St, Brisbane', 'Complete', 'mallory@example.com'), 
(12, 'Next Day Delivery', '2024-05-12', '909 Oxford St, Perth', 'Complete', 'oscar@example.com'), 
(13, 'Standard Shipping', '2024-05-13', '111 King William St, Adelaide', 'Pending', 'peggy@example.com'), 
(14, 'Express Shipping', '2024-05-14', '222 North Terrace, Hobart', 'Pending', 'trent@example.com'), 
(15, 'Priority Shipping', '2024-05-15', '333 Rundle Mall, Darwin', 'Complete', 'victor@example.com'), 
(16, 'Next Day Delivery', '2024-05-16', '444 Hindley St, Canberra', 'Complete', 'wendy@example.com'), 
(17, 'Standard Shipping', '2024-05-17', '555 Flinders St, Sydney', 'Pending', 'xavier@example.com'), 
(18, 'Express Shipping', '2024-05-18', '666 Collins St, Melbourne', 'Pending', 'yvonne@example.com'), 
(19, 'Priority Shipping', '2024-05-19', '777 Queen St, Brisbane', 'Complete', 'zara@example.com'), 
(20, 'Next Day Delivery', '2024-05-20', '888 George St, Perth', 'Complete', 'alice@example.com');