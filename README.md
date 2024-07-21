# Проект JavaFilmorate
## Схема базы данных

[Схема базы данных](ER-Diagram.png)

## Пример данных и запросов

<details>
  <summary>Примеры запросов для работы с базой данных</summary>

  ### Запрос для получения заявок в друзья, отправленных пользователем (не принятых)
  ```sql
SELECT u.name
FROM users u
JOIN user_friends f ON u.id = f.user2_id
WHERE f.user1_id = 1
AND NOT EXISTS (
    SELECT 1
    FROM user_friends f2
    WHERE f2.user1_id = f.user2_id AND f2.user2_id = f.user1_id
);
```
### Запрос для получения друзей пользователя
  ```sql
SELECT u.name
FROM users u
JOIN user_friends f ON u.id = f.user2_id
WHERE f.user1_id = 1
AND EXISTS (
    SELECT 1
    FROM user_friends f2
    WHERE f2.user1_id = f.user2_id AND f2.user2_id = f.user1_id
);
```
### Запрос для получения всех запросов в друзья, которые пользователь должен подтвердить
  ```sql
SELECT u.name
FROM users u
JOIN user_friends f ON u.id = f.user1_id
WHERE f.user2_id = 1
AND NOT EXISTS (
    SELECT 1
    FROM user_friends f2
    WHERE f2.user1_id = f.user2_id AND f2.user2_id = f.user1_id
);
```
### Запрос для получения всех фильмов, понравившихся пользователю
  ```sql
SELECT f.Name
FROM film f
JOIN users_likes u ON u.film_id = f.id
WHERE u.user_id = 1
```
### Запрос для получения всех пользователей, лайкнувших фильм
  ```sql
SELECT u.Name
FROM users u
JOIN users_likes ul ON ul.user_id = u.id
WHERE ul.film_id = 1
```
### Запрос для получения жанра фильма
  ```sql
SELECT g.Name
FROM Genre g
JOIN genre_film gl ON gl.genre_id = g.id
WHERE gl.film_id = 1
```
</details>