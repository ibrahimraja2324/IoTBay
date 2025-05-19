DROP TABLE IF EXISTS Orders;

CREATE TABLE IF NOT EXISTS Orders (
    orderId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderDate TEXT NOT NULL,
    status TEXT NOT NULL,
    totalAmount REAL NOT NULL,
    userEmail TEXT NOT NULL,
    FOREIGN KEY (userEmail) REFERENCES User(Email)
   
);
