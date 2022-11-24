# java-filmorate
ER диаграмма

![QuickDBD-Free Diagram](https://user-images.githubusercontent.com/102370323/203766852-64fde6cb-acf7-43d9-b4c4-0e9a0225b0b4.png)

Программа для работы с информацией о фильмах и её пользователей

# Работа с БАЗОЙ ДАННЫХ

# Пользователь(User):

### показать пользователя
    SELECT *
    FROM users
    WHERE user_id = 1

### показать друзей

    SELECT *
    FROM users AS u
    WHERE u.user_id IN (SELECT friend_id
                  FROM friendship AS f
                  WHERE f.user_id = 1)

### показать общих друзей

    SELECT *
        FROM users AS u
        WHERE u.user_id IN (SELECT f.*
    FROM (SELECT friend_id 
        FROM friendship AS f1
	    WHERE f1.users_id = 1
        AND f1.status) AS f
	    INNER JOIN (SELECT friend_id 
                    FROM friendship AS f2
	                WHERE f2.users_id = 2
                    AND f2.status) AS fa ON f.friend_id = fa.friend_id)

# Фильм(film):

### показать информацию о фильме

![показать фильм](https://user-images.githubusercontent.com/102370323/203766886-5f9215b2-a5d0-4956-b452-e31693d16d7f.jpg)

### список из первых count фильмов по количеству лайков.
  если значение параметра count не задано, вернёт первые 10

![показать фильмы по количеству лайков](https://user-images.githubusercontent.com/102370323/203758057-dfe988c8-bedf-49a0-b6ed-ea7082974d1e.jpg)

### показать категории фильма

![показать категории фильма](https://user-images.githubusercontent.com/102370323/203748907-288ae8f8-0131-40bf-910b-4cd3b622021c.jpg)
