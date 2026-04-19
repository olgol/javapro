INSERT INTO users (username) VALUES ('seed_alice'), ('seed_bob')
ON CONFLICT (username) DO NOTHING;
