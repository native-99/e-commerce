-- Add user_id column to product table
-- This column stores the owner/seller/user who created or manages the product.
ALTER TABLE product
    ADD COLUMN user_id BIGINT;

-- Add foreign key constraint
-- This makes sure product.user_id always references an existing users.user_id.
ALTER TABLE product
    ADD CONSTRAINT fk_product_user
        FOREIGN KEY (user_id)
            REFERENCES users(user_id);

-- Add index on user_id for better performance
-- This helps queries like: SELECT * FROM product WHERE user_id = ?;
CREATE INDEX idx_product_user_id ON product(user_id);
