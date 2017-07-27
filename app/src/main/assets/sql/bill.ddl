CREATE TABLE IF NOT EXISTS bill(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    gross_amount FLOAT NOT NULL,
    net_amount FLOAT NOT NULL,
    vat_pt FLOAT NOT NULL,
--    vat FLOAT NOT NULL,
--    service_charge FLOAT NOT NULL,
--    service_charge_pt FLOAT,
    discount_pt FLOAT,
--    discount FLOAT NOT NULL,
    paid_amount FLOAT NOT NULL,
    change_amount FLOAT NOT NULL,
    creation_time FLOAT NOT NULL,
    table_number INTEGER NOT NULL,
    payment_method TEXT NOT NULL
);