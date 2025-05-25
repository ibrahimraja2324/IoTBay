CREATE TABLE IF NOT EXISTS Orders (
    orderId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderDate TEXT DEFAULT (datetime('now')),
    status TEXT NOT NULL DEFAULT 'Pending',
    totalAmount REAL NOT NULL,
    userEmail TEXT NOT NULL,
    userId INTEGER,               -- Optional, depending on user system
    productId INTEGER,            -- Optional, useful for simple order summaries
    quantity TEXT,                -- Optional, better if normalized in order_items
    deliveryAddress TEXT,
    totalPrice REAL
);