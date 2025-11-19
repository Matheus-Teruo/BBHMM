-- V4__add_event_date_to_event_table.sql

ALTER TABLE events
ADD COLUMN event_date DATE AFTER description;
