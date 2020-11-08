CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(80) NOT NULL,
    password_digest VARCHAR(80) NOT NULL
);

CREATE TABLE products(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    description VARCHAR(80) NOT NULL,
    seller_id BIGINT references users(id) NOT NULL
);

CREATE TYPE role AS ENUM('BUYER', 'SELLER', 'ADMIN');

CREATE TABLE roles(
    id SERIAL PRIMARY KEY,
    name role NOT NULL
);

CREATE TABLE user_roles(
    role_id INTEGER references roles(id) NOT NULL,
    user_id BIGINT references users(id) NOT NULL,
    PRIMARY KEY(role_id, user_id)
);