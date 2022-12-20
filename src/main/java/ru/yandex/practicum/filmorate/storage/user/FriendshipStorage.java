package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void createFriend(Integer friendId, Integer id); // Добавить друга

    void deleteFriend(Integer friendId, Integer id); // удалить друга

    List<User> findFriends(Integer id); // показать друзей пользователя

    List<User> findOtherFriends(Integer otherId, Integer id); // общие друзья

    void deleteAllFriendsUser(int id); // удалить всех друзей
}
