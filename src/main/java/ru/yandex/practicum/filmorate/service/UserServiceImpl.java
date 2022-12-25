package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.recommendation.UserBasedRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final LikeFilmStorage likeFilmStorage;
    private final EventStorage eventStorage;
    private final UserBasedRating userBasedRating;
    private final FilmStorage filmStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, FriendshipStorage friendshipStorage
            , LikeFilmStorage likeFilmStorage, UserBasedRating userBasedRating,
                           FilmStorage filmStorage, EventStorage eventStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
        this.likeFilmStorage = likeFilmStorage;
        this.userBasedRating = userBasedRating;
        this.filmStorage = filmStorage;
        this.eventStorage = eventStorage;
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
        eventStorage.create(id, EventType.FRIEND, Operation.ADD, friendId);
    }
    // добавить друга

    @Override
    public void deleteFriend(Integer friendId, Integer id) {
        friendshipStorage.deleteFriend(friendId, id);
        eventStorage.create(id, EventType.FRIEND, Operation.REMOVE, friendId);
    }
    // удалить друга

    @Override
    public List<User> findFriends(Integer id) {
        List<User> friendsUser = friendshipStorage.findFriends(id);
        if (friendsUser.isEmpty())
            findById(id);
        return friendsUser;

    }
    // показать друзей

    @Override
    public List<User> findOtherFriends(Integer otherId, Integer id) {
        return friendshipStorage.findOtherFriends(otherId, id);
    }
    // показать общих друзей

    @Override
    public List<Event> findAllEvent(Integer id) {
      List<Event> events = eventStorage.findAllEvent(id);
      if(events.isEmpty())
          findById(id);
      return events;
    }

    @Override
    public List<Film> findRecommendations(int userId) {
        Map<Integer, Map<Integer, Boolean>> allUserFilmLike = userStorage.findAllUserFilmLike();
        Map<Integer, Double> filmPredRating = userBasedRating.getCleanSimilarRatings(userId, allUserFilmLike);
        List<Integer> filmsIds = new ArrayList<>(filmPredRating.keySet());
        return filmStorage.findByIds(filmsIds);
    }
}