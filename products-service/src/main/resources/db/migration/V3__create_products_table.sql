CREATE TABLE IF NOT EXISTS products (
    id              BIGSERIAL PRIMARY KEY,
    account_number  VARCHAR(64)  NOT NULL,
    balance         NUMERIC(19, 4) NOT NULL DEFAULT 0,
    product_type    VARCHAR(32)  NOT NULL,
    user_id         BIGINT       NOT NULL,
    CONSTRAINT fk_products_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_products_user_id ON products (user_id);
