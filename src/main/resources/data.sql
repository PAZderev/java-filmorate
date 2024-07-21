INSERT INTO mpa (name)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17');


INSERT INTO genre (name)
VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');

INSERT INTO film (name, description, release_date, duration, mpa_id)
VALUES
    ('Inception', 'A mind-bending thriller', '2010-07-16', 148, 1),
    ('The Matrix', 'A hacker discovers a dystopian reality', '1999-03-31', 136, 2),
    ('Interstellar', 'A journey to save mankind', '2014-11-07', 169, 3);


INSERT INTO users (email, name, login, birthday)
VALUES
    ('john.doe@example.com', 'John Doe', 'johndoe', '1985-05-15'),
    ('jane.smith@example.com', 'Jane Smith', 'janesmith', '1990-08-22'),
    ('alice.wonder@example.com', 'Alice Wonder', 'alicewonder', '1995-12-30');


INSERT INTO users_likes (film_id, user_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 2),
    (3, 3);

INSERT INTO genre_film (film_id, genre_id)
VALUES
    (1, 1),
    (1, 3),
    (2, 1),
    (2, 3),
    (3, 2),
    (3, 4);

INSERT INTO user_friends (user1_id, user2_id)
VALUES
    (1, 2),  -- John Doe sent a friend request to Jane Smith
    (2, 1),  -- Jane Smith accepted the friend request from John Doe
    (2, 3),  -- Jane Smith sent a friend request to Alice Wonder
    (3, 2);  -- Alice Wonder accepted the friend request from Jane Smith
