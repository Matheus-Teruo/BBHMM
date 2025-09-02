-- V1__Create_Tables.sql

CREATE TABLE user (
    uuid CHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(100) NOT NULL
);

CREATE TABLE event (
    uuid CHAR(36) PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE event_user (
    uuid_user CHAR(36) NOT NULL,
    uuid_event CHAR(36) NOT NULL,
    PRIMARY KEY (uuid_user, uuid_event),
    CONSTRAINT fk_eventuser_user FOREIGN KEY (uuid_user) REFERENCES user(uuid),
    CONSTRAINT fk_eventuser_event FOREIGN KEY (uuid_event) REFERENCES event(uuid)
);

CREATE TABLE bills (
    uuid CHAR(36) PRIMARY KEY,
    value DECIMAL(10,2) NOT NULL,
    uuid_payer CHAR(36) NOT NULL,
    paid_value DECIMAL(10,2) DEFAULT 0,
    uuid_event CHAR(36) NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_bills_payer FOREIGN KEY (uuid_payer) REFERENCES user(uuid),
    CONSTRAINT fk_bills_event FOREIGN KEY (uuid_event) REFERENCES event(uuid)
);

CREATE TABLE participants (
    uuid CHAR(36) PRIMARY KEY,
    uuid_user CHAR(36) NOT NULL,
    uuid_bill CHAR(36) NOT NULL,
    paid_value DECIMAL(10,2) DEFAULT 0,
    paid BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_participants_user FOREIGN KEY (uuid_user) REFERENCES user(uuid),
    CONSTRAINT fk_participants_bill FOREIGN KEY (uuid_bill) REFERENCES bills(uuid)
);

CREATE TABLE pix (
    uuid CHAR(36) PRIMARY KEY,
    pix_key VARCHAR(100) NOT NULL,
    bank_account VARCHAR(100) NOT NULL,
    uuid_user CHAR(36) NOT NULL,
    CONSTRAINT fk_pix_user FOREIGN KEY (uuid_user) REFERENCES user(uuid)
);