CREATE TABLE IF NOT EXISTS Orders (
    orderId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderDate TEXT NOT NULL DEFAULT (datetime('now')),
    status TEXT NOT NULL DEFAULT 'Pending',
    totalAmount REAL NOT NULL,
    userEmail TEXT NOT NULL,
    deliveryAddress TEXT,
    paymentMethod TEXT,
    userId INTEGER,               -- Optional
    productId INTEGER,            -- Optional (if not using OrderItems)
    quantity INTEGER,             -- Optional
    totalPrice REAL               -- Optional (redundant with totalAmount)
);