CREATE TABLE IF NOT EXISTS item(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_category_id INTEGER,
    name TEXT NOT NULL,
    price FLOAT NOT NULL,
    image TEXT,
    description TEXT,
    FOREIGN KEY(item_category_id) REFERENCES item_category(id)
);