CREATE TABLE IF NOT EXISTS Orders (
    orderId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderDate TEXT NOT NULL,
    status TEXT NOT NULL,
    totalAmount REAL NOT NULL,
    userId INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES User(userId)
   
);
