
CREATE TABLE IF NOT EXISTS Payment (
    paymentId INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER NOT NULL,
    paymentMethod TEXT NOT NULL,
    cardDetails TEXT NOT NULL,
    amount REAL NOT NULL,
    date TEXT NOT NULL,
    userId INTEGER NOT NULL
);
