# Проект JavaFilmorate
## Схема базы данных

[Схема базы данных](ER-Diagram.png)

## Пример данных и запросов

<details>
  <summary>Примеры запросов для работы с базой данных</summary>

  ### Запрос для получения заявок в друзья, отправленных пользователем (не принятых)
  ```sql
SELECT u.Name
FROM User u
JOIN UserFriends f ON u.UserID = f.User2ID
WHERE f.User1ID = 1
AND NOT EXISTS (
    SELECT 1
    FROM UserFriends f2
    WHERE f2.User1ID = f.User2ID AND f2.User2ID = f.User1ID
);
```
### Запрос для получения друзей пользователя
  ```sql
SELECT u.Name
FROM User u
JOIN UserFriends f ON u.UserID = f.User2ID
WHERE f.User1ID = 1
AND EXISTS (
    SELECT 1
    FROM UserFriends f2
    WHERE f2.User1ID = f.User2ID AND f2.User2ID = f.User1ID
);
```
### Запрос для получения всех запросов в друзья, которые пользователь должен подтвердить
  ```sql
SELECT u.Name
FROM User u
JOIN UserFriends f ON u.UserID = f.User1ID
WHERE f.User2ID = 1
AND NOT EXISTS (
    SELECT 1
    FROM UserFriends f2
    WHERE f2.User1ID = f.User2ID AND f2.User2ID = f.User1ID
);
```
### Запрос для получения всех фильмов, понравившихся пользователю
  ```sql
SELECT f.Name
FROM Film f
JOIN UsersLikes u ON u.FilmID = f.FilmID
WHERE u.UserID = 1
```
### Запрос для получения всех пользователей, лайкнувших фильм
  ```sql
SELECT u.Name
FROM User u
JOIN UsersLikes ul ON ul.UserID = u.UserID
WHERE ul.FilmID = 1
```
### Запрос для получения жанра фильма
  ```sql
SELECT g.Name
FROM Genre g
JOIN GenreFilm gl ON gl.GenreID = g.GenreID
WHERE gl.FilmID = 1
```
</details>