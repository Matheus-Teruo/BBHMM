-- V10__Update_Bill.sql

CREATE TABLE discounts (
    uuid BINARY(16) PRIMARY KEY,
    uuid_event BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    type ENUM('FIXED_TOTAL', 'PERCENTAGE', 'FIXED_PER_BILL') NOT NULL,
    value DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_discounts_event FOREIGN KEY (uuid_event) REFERENCES events(uuid)
);

CREATE TABLE bill_discounts (
    uuid_bill BINARY(16) NOT NULL,
    uuid_discount BINARY(16) NOT NULL,
    applied_value DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (uuid_bill, uuid_discount),
    CONSTRAINT fk_bd_bill FOREIGN KEY (uuid_bill) REFERENCES bills(uuid) ON DELETE CASCADE,
    CONSTRAINT fk_bd_discount FOREIGN KEY (uuid_discount) REFERENCES discounts(uuid) ON DELETE CASCADE
);

ALTER TABLE event_user
ADD COLUMN total_to_receive DECIMAL(10,2) NOT NULL DEFAULT 0.00,
ADD COLUMN total_to_pay DECIMAL(10,2) NOT NULL DEFAULT 0.00,
ADD COLUMN total_discounts DECIMAL(10,2) NOT NULL DEFAULT 0.00;