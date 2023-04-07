CREATE TABLE IF NOT EXISTS bookings (
    id         BIGINT NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    items_id   BIGINT NOT NULL,
    users_id   BIGINT NOT NULL,
    status     VARCHAR(16),
    CONSTRAINT bookings_pk PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS comments (
    id       BIGINT NOT NULL,
    text     VARCHAR(512),
    items_id BIGINT NOT NULL,
    users_id BIGINT NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT comments_pk PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS items (
    id           BIGINT NOT NULL,
    name         VARCHAR(256),
    description  VARCHAR(512),
    is_available BOOLEAN,
    users_id     BIGINT NOT NULL,
    requests_id  BIGINT,
    CONSTRAINT items_pk PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS requests (
    id          BIGINT NOT NULL,
    description VARCHAR(512),
    users_id    BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT requests_pk PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS users (
    id    BIGINT NOT NULL,
    name  VARCHAR(255),
    email VARCHAR(512),
    CONSTRAINT users_pk PRIMARY KEY ( id )
);

ALTER TABLE bookings DROP CONSTRAINT IF EXISTS bookings_items_fk;
ALTER TABLE bookings DROP CONSTRAINT IF EXISTS bookings_users_fk;
ALTER TABLE comments DROP CONSTRAINT IF EXISTS comments_items_fk;
ALTER TABLE comments DROP CONSTRAINT IF EXISTS comments_users_fk;
ALTER TABLE requests DROP CONSTRAINT IF EXISTS requests_users_fk;
ALTER TABLE items DROP CONSTRAINT IF EXISTS items_requests_fk;
ALTER TABLE items DROP CONSTRAINT IF EXISTS items_users_fk;
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pk;
ALTER TABLE users DROP CONSTRAINT IF EXISTS users__un;


ALTER TABLE users ADD CONSTRAINT users_pk PRIMARY KEY ( id );

ALTER TABLE users ADD CONSTRAINT users__un UNIQUE ( email );

ALTER TABLE items
    ADD CONSTRAINT items_requests_fk FOREIGN KEY ( requests_id )
        REFERENCES requests ( id );

ALTER TABLE items
    ADD CONSTRAINT items_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id );

ALTER TABLE bookings
    ADD CONSTRAINT bookings_items_fk FOREIGN KEY ( items_id )
        REFERENCES items ( id );

ALTER TABLE bookings
    ADD CONSTRAINT bookings_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id );

ALTER TABLE comments
    ADD CONSTRAINT comments_items_fk FOREIGN KEY ( items_id )
        REFERENCES items ( id );

ALTER TABLE comments
    ADD CONSTRAINT comments_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id );


ALTER TABLE requests
    ADD CONSTRAINT requests_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id );