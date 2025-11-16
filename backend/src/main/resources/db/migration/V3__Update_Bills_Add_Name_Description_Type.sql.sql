-- V3__Update_Bills_Add_Name_Description_Type.sql

ALTER TABLE bills
ADD COLUMN name VARCHAR(100) AFTER uuid,
ADD COLUMN description TEXT AFTER name;
ADD COLUMN type ENUM('BILL', 'PAYMENT') NOT NULL DEFAULT 'BILL' AFTER paid;