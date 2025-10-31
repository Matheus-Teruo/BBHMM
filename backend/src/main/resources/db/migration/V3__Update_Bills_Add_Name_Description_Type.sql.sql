-- V3__Update_Bills_Add_Name_Description_Type.sql

ALTER TABLE bills
ADD COLUMN name VARCHAR(100) AFTER uuid,
ADD COLUMN description TEXT AFTER name;
ADD COLUMN type ENUM('bill', 'payment') NOT NULL DEFAULT 'bill' AFTER paid;