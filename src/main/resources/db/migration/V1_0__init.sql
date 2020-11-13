CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(80) NOT NULL,
    password_digest VARCHAR(80) NOT NULL,
    enabled boolean NOT NULL
);

CREATE TABLE products(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    description VARCHAR(80) NOT NULL,
    seller_id BIGINT references users(id) ON DELETE CASCADE
);

CREATE TABLE roles(
    id SERIAL PRIMARY KEY,
    role VARCHAR(80) NOT NULL
);

CREATE TABLE user_roles(
    role_id INTEGER references roles(id) NOT NULL,
    user_id BIGINT references users(id) ON DELETE CASCADE,
    PRIMARY KEY(role_id, user_id)
);

CREATE TABLE verification_tokens(
    id BIGSERIAL PRIMARY KEY,
    token TEXT NOT NULL,
    expiration DATE NOT NULL,
    user_id BIGINT references users(id) ON DELETE CASCADE
);