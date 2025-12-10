-- V6__Add_Finalization_To_Event_And_Validation_User

ALTER TABLE events
ADD COLUMN finished BOOLEAN 
NOT NULL DEFAULT FALSE
AFTER event_date;

ALTER TABLE users
ADD COLUMN email_verified BOOLEAN 
NOT NULL DEFAULT TRUE
AFTER role;
