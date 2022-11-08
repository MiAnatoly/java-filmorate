package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public User findUser(Integer id) {
        if (!storage.getUsers().containsKey(id))
            throw new NotObjectException("нет данного пользователя");
        return storage.getUsers().get(id);
    }
    // показать пользователя

    public void createFriend(Integer friendId, Integer id) {
        if (!storage.getUsers().containsKey(friendId))
            throw new NotObjectException("нет пользователя");
        if (!storage.getUsers().containsKey(id))
            throw new NotObjectException("вы не зарегистрированы");
        if (storage.getUsers().get(id).getFriendsId().contains(friendId)
                && storage.getUsers().get(friendId).getFriendsId().contains(id))
            throw new RuntimeException("пользователь уже в друзьях");
        storage.getUsers().get(id).getFriendsId().add(friendId);
        storage.getUsers().get(friendId).getFriendsId().add(id);
    }
    // добавить друга

    public void deleteFriend(Integer friendId, Integer id) {
        if (!storage.getUsers().containsKey(friendId))
            throw new RuntimeException("нет пользователя");
        if (!storage.getUsers().containsKey(id))
            throw new RuntimeException("вы не зарегистрированы");
        storage.getUsers().get(id).getFriendsId().remove(friendId);
        storage.getUsers().get(friendId).getFriendsId().remove(id);
    }
    // удалить друга

    public List<User> findFriends(Integer id) {
        if (!storage.getUsers().containsKey(id))
            throw new NotObjectException("вы не зарегистрированы");
        List<Integer> friendsId = new ArrayList<>(storage.getUsers().get(id).getFriendsId());
        return friendsUser(friendsId);
    }
    // показать друзей

    public List<User> findOtherFriends(Integer otherId, Integer id) {
        if (!storage.getUsers().containsKey(otherId))
            throw new NotObjectException("нет пользователя");
        if (!storage.getUsers().containsKey(id))
            throw new NotObjectException("вы не зарегистрированы");
        List<Integer> OtherFriendsId = new ArrayList<>();
        List<Integer> myFriendsId = new ArrayList<>(storage.getUsers().get(id).getFriendsId());
        List<Integer> friendsId = new ArrayList<>(storage.getUsers().get(otherId).getFriendsId());
        for (Integer friendId : myFriendsId) {
            if (friendsId.contains(friendId))
                OtherFriendsId.add(friendId);
        }
        return friendsUser(OtherFriendsId);
    }
    // показать общих друзей

    private List<User> friendsUser(List<Integer> friendsId) {
        List<User> friendsUser = new ArrayList<>();
        for (Integer friend : friendsId) {
            friendsUser.add(storage.getUsers().get(friend));
        }
        return friendsUser;
    }
    // воспомогательный метод для преобразования списка из ID пользователей в список из пользователей
}