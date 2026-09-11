-- V9__Add_Image_To_User.sql

ALTER TABLE users
ADD COLUMN image_key VARCHAR(255)
AFTER email_verified;
