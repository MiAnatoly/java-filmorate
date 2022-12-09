package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements ru.yandex.practicum.filmorate.service.UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }
    // показать всех пользователей

    @Override
    public User create(User user) {
        int id = userStorage.create(user).getId();
        return findById(id);
    }
    // добавить позьзователя

    @Override
    public User update(User user) {
        int id = userStorage.update(user).getId();
        return findById(id);
    }
    // обнавит пользователя

    @Override
    public User findById(Integer id) {
        return userStorage.findById(id).orElseThrow(() -> new NotObjectException("нет пользователя"));
    }
    // показать пользователя

    @Override
    public void createFriend(Integer friendId, Integer id) {
        userStorage.createFriend(friendId, id);
    }
    // добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        userStorage.deleteFriend(friendId, id);
    }
    // удалить друга

    @Override
    public List<User> findFriends(Integer id) {
        return userStorage.findFriends(id);
    }
    // показать друзей

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        return userStorage.findOtherFriends(otherId, id);
    }
    // показать общих друзей
}