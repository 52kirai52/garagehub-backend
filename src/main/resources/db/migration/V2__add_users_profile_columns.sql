ALTER TABLE users
    ADD COLUMN birth_date DATE NOT NULL,
    ADD COLUMN gender VARCHAR(10) NOT NULL,
    ADD COLUMN email VARCHAR(255) NULL;

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);