-- :name create-user! :<! :1
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, username, password, created_at, updated_at)
VALUES (:first_name, :last_name, :username, :password, now(), now())
RETURNING id, created_at, updated_at;

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, username = :username
WHERE id = :id;

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id;

-- :name get-user-by-username :? :1
-- :doc retrieve a user given the username
SELECT * FROM users
WHERE username = :username;

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id;

-- :name get-users :? :*
-- :doc Get all users from db
SELECT * FROM users;

-- :name send-message! :<! :1
-- :doc Send a message from one user to another
INSERT INTO messages
(message_to, message_from, text, created_at, updated_at)
VALUES (:to, :from, :text, now(), now())
RETURNING (id, text, created_at, updated_at);

-- :name get-messages-to :? :*
-- :doc Get messages to the user whose id is passed as a parameter
SELECT messages.id AS id, messages.text, messages.created_at, users2.id AS user_id, users2.username FROM messages
INNER JOIN users ON users.id = messages.message_to
INNER JOIN users AS users2 ON users2.id = messages.message_from
WHERE users.id = :id;

-- :name delete-message! :! :1
-- :doc Delete a messager from the database
DELETE FROM messages WHERE messages.id = :id;


-- :name get-messages-from :? :*
-- :doc Get messages sent by the user whose id is passed as a param
SELECT messages.text, messages.created_at FROM messages
INNER JOIN users on users.id = messages.message_from
WHERE users.id = :id;
