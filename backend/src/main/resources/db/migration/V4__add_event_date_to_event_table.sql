-- V4__add_event_date_to_event_table.sql

ALTER TABLE event
ADD COLUMN event_date DATE AFTER description;
