CREATE TABLE IF NOT EXISTS film
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(300) NOT NULL,
    description  VARCHAR(300),
    release_date DATE         NOT NULL,
    duration     INT          NOT NULL,
    mpa_id       INT          NOT NULL
);


CREATE TABLE IF NOT EXISTS genre
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT uc_genre_name UNIQUE (name)
);


CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email    VARCHAR(300) NOT NULL,
    name     VARCHAR(300) NOT NULL,
    login    VARCHAR(100) NOT NULL,
    birthday DATE         NOT NULL,
    CONSTRAINT uc_user_email UNIQUE (email),
    CONSTRAINT uc_user_login UNIQUE (login)
);


CREATE TABLE IF NOT EXISTS users_likes
(
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_users_likes PRIMARY KEY (film_id, user_id)
);


CREATE TABLE IF NOT EXISTS genre_film
(
    film_id  BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    CONSTRAINT pk_genre_film PRIMARY KEY (film_id, genre_id)
);


CREATE TABLE IF NOT EXISTS user_friends
(
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    CONSTRAINT pk_user_friends PRIMARY KEY (user1_id, user2_id)
);

CREATE TABLE IF NOT EXISTS mpa
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(10)
);


COMMIT;

-- Добавление внешних ключей
ALTER TABLE users_likes
    ADD CONSTRAINT IF NOT EXISTS fk_users_likes_film_id FOREIGN KEY (film_id)
        REFERENCES film (id) ON DELETE CASCADE;

ALTER TABLE users_likes
    ADD CONSTRAINT IF NOT EXISTS fk_users_likes_user_id FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE genre_film
    ADD CONSTRAINT IF NOT EXISTS fk_genre_film_film_id FOREIGN KEY (film_id)
        REFERENCES film (id) ON DELETE CASCADE;

ALTER TABLE genre_film
    ADD CONSTRAINT IF NOT EXISTS fk_genre_film_genre_id FOREIGN KEY (genre_id)
        REFERENCES genre (id) ON DELETE CASCADE;

ALTER TABLE user_friends
    ADD CONSTRAINT IF NOT EXISTS fk_user_friends_user1_id FOREIGN KEY (user1_id)
        REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_friends
    ADD CONSTRAINT IF NOT EXISTS fk_user_friends_user2_id FOREIGN KEY (user2_id)
        REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE film
    ADD CONSTRAINT IF NOT EXISTS fk_film_mpa_id FOREIGN KEY (mpa_id)
        REFERENCES mpa (id) ON DELETE CASCADE;

COMMIT
