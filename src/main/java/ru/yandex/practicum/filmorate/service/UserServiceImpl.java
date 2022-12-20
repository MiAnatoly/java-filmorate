package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.LikeFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final LikeFilmStorage likeFilmStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, FriendshipStorage friendshipStorage
            , LikeFilmStorage likeFilmStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
        this.likeFilmStorage = likeFilmStorage;
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

    public void deleteUser(int id) {
        likeFilmStorage.removeLikesUser(id);
        friendshipStorage.deleteAllFriendsUser(id);
        userStorage.deleteUser(id);
    }

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
        List<User> friendsUser = friendshipStorage.findFriends(id);
        if(friendsUser.isEmpty())
            findById(id);
        return friendsUser;

    }
    // показать друзей

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        return friendshipStorage.findOtherFriends(otherId, id);
    }
    // показать общих друзей
}