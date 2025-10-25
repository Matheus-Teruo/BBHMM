-- V2__Update_List_Request_User_Email_Bills_Debit_Amount.sql

ALTER TABLE user
ADD COLUMN email VARCHAR(255) UNIQUE;

ALTER TABLE bills
CHANGE COLUMN paid_value debit_amount DECIMAL(10,2) DEFAULT 0;

CREATE TABLE event_invitation (
    uuid CHAR(36) PRIMARY KEY,
    uuid_invited_user CHAR(36) NOT NULL,
    uuid_owner_user CHAR(36) NOT NULL,
    uuid_event CHAR(36) NOT NULL,
    accepted BOOLEAN,
    CONSTRAINT fk_event_invitation_invited_user FOREIGN KEY (uuid_invited_user) REFERENCES user(uuid),
    CONSTRAINT fk_event_invitation_owner_user FOREIGN KEY (uuid_owner_user) REFERENCES user(uuid),
    CONSTRAINT fk_event_invitation_event FOREIGN KEY (uuid_event) REFERENCES event(uuid)
);