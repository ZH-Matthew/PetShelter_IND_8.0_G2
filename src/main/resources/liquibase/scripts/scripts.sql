--liquibase formatted sql
--changeset evgeniy:1
CREATE TABLE cat_owners (
chat_id bigserial primary key,
date_time TIMESTAMP,
first_name TEXT,
last_name TEXT,
phone_number TEXT,
user_name TEXT,
days_overdue_report INT,
probation INT,
status varchar
);

--changeset evgeniy:2
CREATE TABLE cat_report (
id bigserial primary key,
diet TEXT,
changes_behavior TEXT,
well_being_and_adaptation TEXT,
file_as_array_of_bytes BYTEA,
date DATE,
chat_id bigserial unique references cat_owners(chat_id)
);

--changeset evgeniy:3
CREATE TABLE dog_owners (
chat_id bigserial primary key,
date_time TIMESTAMP,
first_name TEXT,
last_name TEXT,
phone_number TEXT,
user_name TEXT,
days_overdue_report INT,
probation INT,
status varchar
);

--changeset evgeniy:4
CREATE TABLE dog_report (
id bigserial primary key,
diet TEXT,
changes_behavior TEXT,
well_being_and_adaptation TEXT,
file_as_array_of_bytes BYTEA,
date DATE,
chat_id bigserial unique references dog_owners(chat_id)
);

--changeset evgeniy:5
CREATE TABLE selection (
chat_id bigserial primary key,
counter INT,
selection boolean
)
