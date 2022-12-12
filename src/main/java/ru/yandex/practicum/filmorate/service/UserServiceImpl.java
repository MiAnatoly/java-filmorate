package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements ru.yandex.practicum.filmorate.service.UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }
    // показать всех пользователей

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }
    // добавить позьзователя

    @Override
    public User update(User user) {
        return userStorage.update(user).orElseThrow(() -> new NotObjectException("нет пользователя"));
    }
    // обнавит пользователя

    @Override
    public User findById(Integer id) {
        return userStorage.findById(id).orElseThrow(() -> new NotObjectException("нет пользователя"));
    }
    // показать пользователя

    @Override
    public void createFriend(Integer friendId, Integer id) {
        friendshipStorage.createFriend(friendId, id);
    }
    // добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        friendshipStorage.deleteFriend(friendId, id);
    }
    // удалить друга

    @Override
    public List<User> findFriends(Integer id) {
        return friendshipStorage.findFriends(id);
    }
    // показать друзей

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        return friendshipStorage.findOtherFriends(otherId, id);
    }
    // показать общих друзей
}