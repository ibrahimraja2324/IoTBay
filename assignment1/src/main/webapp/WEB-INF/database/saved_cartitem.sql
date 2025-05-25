CREATE TABLE saved_cartitem (
    cartItemId INTEGER PRIMARY KEY AUTOINCREMENT,
    userEmail TEXT NOT NULL,
    productId INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (productId) REFERENCES Device(ID)
);

INSERT INTO saved_cartitem (userEmail, productId, quantity) VALUES
('a@a.com', 1, 1),
('a@a.com', 2, 1),
('a@a.com', 3, 1);