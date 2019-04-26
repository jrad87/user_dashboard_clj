CREATE TABLE messages (
  id BIGSERIAL PRIMARY KEY,
  text VARCHAR(255),
  message_to BIGINT REFERENCES users(id),
  message_from BIGINT REFERENCES users(id),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
