package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStorage {

    List<User> findAll();

    User create(User user);

    User update(User user);

    Optional<User> findById(Integer id);

    void createFriend(Integer friendId, Integer id);

    void deleteFriend(Integer friendId, Integer id);

    List<User> findFriends(Integer id);

    List<User> findOtherFriends(Integer otherId, Integer id);

}
