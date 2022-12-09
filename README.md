# java-filmorate
ER диаграмма

![QuickDBD-Free Diagram](https://user-images.githubusercontent.com/102370323/203858378-e024ac8f-89cf-496e-91a2-617248bc0a06.png)

Программа для работы с информацией о фильмах и её пользователей

# Работа с БАЗОЙ ДАННЫХ

# Пользователь(User):

### показать пользователя

    SELECT *
    FROM USERS_FILMS
    WHERE user_id = {id}

### показать друзей

    SELECT *
    FROM USERS_FILMS AS u
    WHERE u.user_id IN (SELECT friend_id
                  FROM friendship AS f
                  WHERE f.user_id = {id}
		  AND f.status)

### показать общих друзей

    SELECT *
        FROM USERS_FILMS AS u
        WHERE u.user_id IN (SELECT f.*
    FROM (SELECT friend_id 
        FROM friendship AS f
	    WHERE f.users_id = {id}) AS f
	    INNER JOIN (SELECT friend_id 
                    FROM friendship AS f
	                WHERE f.users_id = {id}) AS fa 
                    ON f.friend_id = fa.friend_id)

# Фильм(film):

### показать информацию о фильме

	SELECT *
    	FROM films AS f
    	WHERE f.films_id = {id}

### список из первых count фильмов по количеству лайков.
  если значение параметра count не задано, вернёт первые 10

    SELECT f.film_id,
    	   COUNT(lf.user_id) AS count_like
    FROM films AS f
    LEFT OUTER JOIN like_film AS lf ON f.film_id = lf.film_id
    GROUP BY f.name
    ORDER BY count_like DESC
    	LIMIT {size}

### показать категории фильма

   	SELECT Fc.CATEGORY_ID
   	FROM film_category AS fc
	WHERE fc.film_id = {id}
    GROUP BY FILM_ID, CATEGORY_ID
