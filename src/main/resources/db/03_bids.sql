CREATE TABLE IF NOT EXISTS bids (
    bid_id INT AUTO_INCREMENT PRIMARY KEY,       -- Unique identifier for each bid
    product_id INT NOT NULL,                     -- References the product being bid on
    user_id INT NOT NULL,                        -- References the user placing the bid
    bid_amount DECIMAL(10, 2) NOT NULL,          -- The amount of the bid
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the bid was placed

   FOREIGN KEY (product_id) REFERENCES products(product_id)
           ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (user_id) REFERENCES users(user_id)
           ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_product_id ON bids(product_id);
CREATE INDEX idx_user_id ON bids(user_id);
