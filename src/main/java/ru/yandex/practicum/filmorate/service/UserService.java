package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStorage.findAll().values());
    }
    // показать всех пользователей

    @Override
    public User create(User user) {
        return userStorage.create(user).orElseThrow(() -> new NotObjectException("объект отсутствует"));
    }
    // добавить позьзователя

    @Override
    public User update(User user) {
        return userStorage.update(user).orElseThrow(() -> new NotObjectException("объект отсутствует"));
    }
    // обнавит пользователя

    @Override
    public User findUser(Integer id) {
        return findById(id, new NotObjectException("нет данного пользователя"));
    }
    // показать пользователя

    @Override
    public void createFriend(Integer friendId, Integer id) {
        findById(friendId, new NotObjectException("нет пользователя"));
        findById(id, new NotObjectException("вы не зарегистрированы"));
        if (userStorage.findAll().get(id).getFriendsId().contains(friendId)
                && userStorage.findAll().get(friendId).getFriendsId().contains(id))
            throw new RuntimeException("пользователь уже в друзьях");
        userStorage.findAll().get(id).getFriendsId().add(friendId);
        userStorage.findAll().get(friendId).getFriendsId().add(id);
    }
    // добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        findById(friendId, new NotObjectException("нет пользователя"));
        findById(id, new NotObjectException("вы не зарегистрированы"));
        userStorage.findAll().get(id).getFriendsId().remove(friendId);
        userStorage.findAll().get(friendId).getFriendsId().remove(id);
    }
    // удалить друга

    @Override
    public List<User> findFriends(Integer id) {
        findById(id, new NotObjectException("вы не зарегистрированы"));
        List<Integer> friendsId = new ArrayList<>(userStorage.findAll().get(id).getFriendsId());
        return friendsUser(friendsId);
    }
    // показать друзей

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        findById(otherId, new NotObjectException("нет пользователя"));
        findById(id, new NotObjectException("вы не зарегистрированы"));
        List<Integer> myFriendsId = new ArrayList<>(userStorage.findAll().get(id).getFriendsId());
        List<Integer> friendsId = new ArrayList<>(userStorage.findAll().get(otherId).getFriendsId());
        List<Integer> OtherFriendsId = myFriendsId.stream().filter(friendsId::contains)
                .collect(Collectors.toList());
        return friendsUser(OtherFriendsId);
    }
    // показать общих друзей

    private List<User> friendsUser(List<Integer> friendsId) {
        return friendsId.stream().map(userStorage.findAll()::get).collect(Collectors.toList());
    }
    // воспомогательный метод для преобразования списка из ID пользователей в список из пользователей

    private User findById(Integer id, RuntimeException exception) {
        return Optional.ofNullable(userStorage.findAll().get(id)).orElseThrow(() -> exception);
    }
    //поиск пользователя по id
}