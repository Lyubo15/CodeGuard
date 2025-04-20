-- liquibase formatted sql

-- changeset codeguard:1

CREATE TABLE customer (
    id VARCHAR PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE application (
    id VARCHAR PRIMARY KEY,
    repository_url VARCHAR(255),
    ai_result_file_path VARCHAR(255),
    customer_id VARCHAR,

    CONSTRAINT fk_application_customer
    FOREIGN KEY (customer_id) REFERENCES customer(id)
    ON DELETE CASCADE
);

-- changeset codeguard:2

CREATE TABLE user_role (
    id VARCHAR PRIMARY KEY,
    authority VARCHAR NOT NULL
);

CREATE TABLE users (
    id VARCHAR PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE users_user_roles
(
    user_id VARCHAR NOT NULL,
    user_role_id VARCHAR NOT NULL,

    PRIMARY KEY (user_id, user_role_id),

    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (user_role_id) REFERENCES user_role (id) ON DELETE CASCADE
);

-- changeset codeguard:3
ALTER TABLE application
ADD COLUMN deleted BOOLEAN DEFAULT FALSE;
