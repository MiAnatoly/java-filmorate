package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User create(User user);

    User update(User user);

    void deleteUser(int id);

    User findById(Integer id);

    void createFriend(Integer friendId, Integer id);

    void deleteFriend(Integer friendId, Integer id);

    List<User> findFriends(Integer id);

    List<User> findOtherFriends(Integer otherId, Integer id);

    List<Film> findRecommendations(int userId);
}
