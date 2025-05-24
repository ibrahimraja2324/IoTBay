CREATE TABLE saved_cartitem (
    cartItemId INTEGER PRIMARY KEY AUTOINCREMENT,
    userEmail TEXT NOT NULL,
    productId INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    FOREIGN KEY (productId) REFERENCES Device(ID)
);