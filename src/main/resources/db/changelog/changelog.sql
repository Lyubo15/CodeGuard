-- liquibase formatted sql

-- changeset codeguard:1

CREATE TABLE customer (
    id VARCHAR PRIMARY KEY,
    github_name VARCHAR(255) UNIQUE NOT NULL
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