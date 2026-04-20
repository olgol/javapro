INSERT INTO products (account_number, balance, product_type, user_id)
SELECT '40817810000000000001', 10000.0000, 'ACCOUNT', id
FROM users WHERE username = 'seed_alice' LIMIT 1;

INSERT INTO products (account_number, balance, product_type, user_id)
SELECT '40817810000000000002', 2500.5000, 'CARD', id
FROM users WHERE username = 'seed_alice' LIMIT 1;

INSERT INTO products (account_number, balance, product_type, user_id)
SELECT '40817810000000000003', 500.0000, 'ACCOUNT', id
FROM users WHERE username = 'seed_bob' LIMIT 1;
