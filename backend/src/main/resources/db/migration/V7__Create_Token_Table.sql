-- V7__Create_Token_Table.sql

CREATE TABLE tokens (
    uuid BINARY(16) PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    type ENUM('CONFIRM_EMAIL','RESET_PASSWORD') NOT NULL,
    time_stamp TIMESTAMP NOT NULL,
    uuid_user BINARY(16) NOT NULL,
    CONSTRAINT fk_tokens_user FOREIGN KEY (uuid_user) REFERENCES users(uuid)
);