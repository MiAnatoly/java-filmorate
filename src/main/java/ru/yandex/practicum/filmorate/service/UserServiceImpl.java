package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll().orElseThrow(() -> new NotObjectException("нет объекта"));
    }
    // показать всех пользователей

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }
    // добавить позьзователя

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }
    // обнавит пользователя

    @Override
    public User findById(Integer id) {
        return findAll().stream()
                    .filter(x -> x.getId() == id)
                    .findAny()
                    .orElseThrow(() -> new NotObjectException("нет данного пользователя"));
    }
    // показать пользователя

    @Override
    public void createFriend(Integer friendId, Integer id) {
        User friend = findById(friendId);
        User my = findById(id);
        if (my.getFriendsId().contains(friendId)
                && friend.getFriendsId().contains(id))
            throw new RuntimeException("пользователь уже в друзьях");
        my.getFriendsId().add(friendId);
        friend.getFriendsId().add(id);
        update(friend);
        update(my);
    }
    // добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        User friend = findById(friendId);
        User my = findById(id);
        if (!(my.getFriendsId().contains(friendId)
                && friend.getFriendsId().contains(id)))
            throw new RuntimeException("пользователя нет в друзьях");
        my.getFriendsId().remove(friendId);
        friend.getFriendsId().remove(id);
        update(friend);
        update(my);

    }
    // удалить друга

    @Override
    public List<User> findFriends(Integer id) {
        List<Integer> friendsId = new ArrayList<>(findById(id).getFriendsId());
        return friendsUser(friendsId);
    }
    // показать друзей

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        findById(otherId);
        findById(id);
        List<Integer> myFriendsId = new ArrayList<>(findById(id).getFriendsId());
        List<Integer> friendsId = new ArrayList<>(findById(otherId).getFriendsId());
        List<Integer> OtherFriendsId = myFriendsId.stream().filter(friendsId::contains)
                .collect(Collectors.toList());
        return friendsUser(OtherFriendsId);
    }
    // показать общих друзей

    private List<User> friendsUser(List<Integer> friendsId) {
        return friendsId.stream().map(this::findById).collect(Collectors.toList());
    }
    // воспомогательный метод для преобразования списка из ID пользователей в список из пользователей
}