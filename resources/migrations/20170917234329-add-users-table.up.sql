CREATE TABLE users
(id BIGSERIAL PRIMARY KEY,
 first_name VARCHAR(32),
 last_name VARCHAR(32),
 username VARCHAR(32), 
 is_admin BOOLEAN DEFAULT FALSE,
 password VARCHAR(300),
 description VARCHAR(512),
 created_at TIMESTAMP,
 updated_at TIMESTAMP);
