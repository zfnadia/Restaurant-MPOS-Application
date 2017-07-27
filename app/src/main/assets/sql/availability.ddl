CREATE TABLE IF NOT EXISTS availability(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    item_id INTEGER NOT NULL,
    slot INTEGER NOT NULL,
    FOREIGN KEY(item_id) REFERENCES item(id)
);