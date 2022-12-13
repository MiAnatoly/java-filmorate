# java-filmorate
ER диаграмма

![](../Desktop/задание/QuickDBD-Free Diagram.png)
Программа для работы с информацией о фильмах и её пользователей

# Работа с БАЗОЙ ДАННЫХ

# Пользователь(User):

### показать пользователей

    SELECT * 
    FROM USERS_FILMS

### Добавить пользователя

    INSERT INTO USERS_FILMS (NAME, EMAIL, LOGIN, BIRTHDAY) VALUES (?, ?, ?, ?)

### обнавить пользователя

    UPDATE USERS_FILMS set NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? 
    WHERE USER_ID = ?

### показать пользователя

    SELECT *
    FROM USERS_FILMS
    WHERE user_id = {id}

## Друзья
### добавить друга

    INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (?, ?)

### удалить друга

    DELETE FROM FRIENDSHIP WHERE  USER_ID = ? AND FRIEND_ID = ?

### показать друзей

    SELECT * 
    FROM USERS_FILMS, FRIENDSHIP 
    where USERS_FILMS.USER_ID = FRIENDSHIP.FRIEND_ID
      AND FRIENDSHIP.USER_ID = {id}

### показать общих друзей

    select * 
    from USERS_FILMS u, FRIENDSHIP f, FRIENDSHIP o
    where u.USER_ID = f.FRIEND_ID " +
      AND u.USER_ID = o.FRIEND_ID " +
      AND f.USER_ID = {id} AND o.USER_ID = {otherId}

# Фильм(film):

### показать информацию о фильмах

    SELECT * FROM FILMS LEFT JOIN RATING_MPA RM on FILMS.RATING_ID = RM.RATING_ID

### добавить фильм

    INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) 
    values (?, ?, ?, ?, ?)

### обновить фильм

    UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?,  RATING_ID = ? WHERE FILM_ID = ?

### показать информацию о фильме

	SELECT * 
    FROM FILMS 
    LEFT JOIN RATING_MPA RM on FILMS.RATING_ID = RM.RATING_ID

### список из первых count фильмов по количеству лайков.
  если значение параметра count не задано, вернёт первые 10

    SELECT f.*, rm.RATING
    FROM FILMS AS f LEFT OUTER JOIN like_film AS lf ON f.FILM_ID = lf.FILM_ID
    LEFT JOIN RATING_MPA rm on f.RATING_ID = rm.RATING_ID
    GROUP BY f.NAME
    ORDER BY COUNT(lf.USER_ID) DESC
    LIMIT  + count

## Лайки
### добавить лайк

    INSERT INTO LIKE_FILM (FILM_ID, USER_ID) VALUES (?, ?)

### удалить лайк

    DELETE FROM LIKE_FILM where FILM_ID = ? AND USER_ID = ?

## Категории
### показать категории 

   	SELECT * 
    FROM CATEGORY

### показать категорию по id

    SELECT * 
    FROM CATEGORY WHERE CATEGORY_ID = ?

### вернуть категории фильма

    SELECT * FROM FILM_CATEGORY AS f
    LEFT OUTER JOIN CATEGORY AS C ON f.CATEGORY_ID = c.CATEGORY_ID 
    WHERE FILM_ID = {film.getId()}
    GROUP BY FILM_ID, f.CATEGORY_ID
    ORDER BY f.CATEGORY_ID

### вернуть категории фильмов

    SELECT * FROM FILM_CATEGORY AS f 
    LEFT OUTER JOIN CATEGORY AS C ON f.CATEGORY_ID = c.CATEGORY_ID 
    WHERE FILM_ID IN (%s) 
    GROUP BY FILM_ID
    ORDER BY FILM_ID, CATEGORY_ID

### добавить категории фильма в БД

    INSERT INTO FILM_CATEGORY (FILM_ID, CATEGORY_ID) VALUES (?,?)

### удалить категории фильма из БД

    DELETE FROM FILM_CATEGORY WHERE FILM_ID = ?

## Рейтинг
### показать список рейтингов

    SELECT * 
    FROM RATING_MPA

### показать рейтинг по id

    SELECT * 
    FROM RATING_MPA 
    WHERE RATING_ID = ?