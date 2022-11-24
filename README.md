# java-filmorate
ER диаграмма

![QuickDBD-Free Diagram](https://user-images.githubusercontent.com/102370323/203843736-0fee2b5c-ac10-48ab-bcc1-e5936fd720b6.png)

Программа для работы с информацией о фильмах и её пользователей

# Работа с БАЗОЙ ДАННЫХ

# Пользователь(User):

### показать пользователя
    SELECT *
    FROM users
    WHERE user_id = {id}

### показать друзей

    SELECT *
    FROM users AS u
    WHERE u.user_id IN (SELECT friend_id
                  FROM friendship AS f
                  WHERE f.user_id = {id})

### показать общих друзей

    SELECT *
        FROM users AS u
        WHERE u.user_id IN (SELECT f.*
    FROM (SELECT friend_id 
        FROM friendship AS f
	    WHERE f.users_id = {id}
        AND f.status) AS f
	    INNER JOIN (SELECT friend_id 
                    FROM friendship AS f
	                WHERE f.users_id = {id}
                    AND f.status) AS fa ON f.friend_id = fa.friend_id)

# Фильм(film):

### показать информацию о фильме

	SELECT *
    	FROM films AS f
    	LEFT OUTER JOIN rating_MPA AS r ON f.raiting_id = r.raiting_id
    	WHERE f.films_id = {id}

### список из первых count фильмов по количеству лайков.
  если значение параметра count не задано, вернёт первые 10

    SELECT f.name,
    	   COUNT(lf.user_id) AS count_like
    FROM films AS f
    LEFT OUTER JOIN like_film AS lf ON f.film_id = lf.film_id
    GROUP BY f.name
    ORDER BY count_like DESC
    	LIMIT {size}

### показать категории фильма

   SELECT c.name
   FROM film_category AS fc
   left JOIN category AS c ON fc.category_id = c.category_id
	WHERE fc.film_id = {id}
