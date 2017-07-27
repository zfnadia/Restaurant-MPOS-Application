CREATE TABLE IF NOT EXISTS bill(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    gross_amount FLOAT NOT NULL,
    net_amount FLOAT NOT NULL,
    vat_pt FLOAT NOT NULL,
    discount_pt FLOAT,
    paid_amount FLOAT NOT NULL,
    change_amount FLOAT NOT NULL,
    creation_time FLOAT NOT NULL,
    table_number INTEGER NOT NULL,
    payment_method TEXT NOT NULL
);