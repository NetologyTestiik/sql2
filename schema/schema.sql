CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    login VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL
);

CREATE TABLE auth_codes (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE cards (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    balance_in_kopecks INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE card_transactions (
    id VARCHAR(255) PRIMARY KEY,
    card_from_id VARCHAR(255) NOT NULL,
    card_to_id VARCHAR(255) NOT NULL,
    amount_in_kopecks INT NOT NULL,
    FOREIGN KEY (card_from_id) REFERENCES cards(id),
    FOREIGN KEY (card_to_id) REFERENCES cards(id)
);

INSERT INTO users (id, login, password, status)
VALUES ('1', 'vasya', '$2a$10$bWDOJ6r4Q8JJH1Q8eKjumea2fBzOgPpe.9CiIh7DcJVoK3v1FJj0K', 'active');
