package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Category;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.CategoryStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeFilmStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final CategoryStorage categoryStorage;
    private final MpaStorage mpaStorage;
    private final FriendshipStorage friendshipStorage;
    private final LikeFilmStorage likeFilmStorage;
    // USER

    @Test
    public void testFindUserAll() {

        List<User> users = userStorage.findAll();
        assertEquals(users.size(), 3);
    }
    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.findById(1);
        Optional<User> userOptional1 = userStorage.findById(5);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
        assertEquals(userOptional1, Optional.empty());
    }

    @Test
    public void testUserUpdate() {
        User user2 = new User(1, "sdddd@mail.ru", "sdd", "asr", LocalDate.of(1994, 12, 27));
        userStorage.update(user2);
        Optional<User> userOptional = userStorage.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "sdddd@mail.ru")
                );
    }

    @Test
    public void testFriend() {
        friendshipStorage.createFriend(2,1);
        assertEquals(friendshipStorage.findFriends(1).size(), 1);
        friendshipStorage.createFriend(3,1);
        friendshipStorage.createFriend(1,2);
        friendshipStorage.createFriend(3,2);
        assertEquals(friendshipStorage.findOtherFriends(1,2).size(), 1);
        friendshipStorage.deleteFriend(2,1);
        assertEquals(friendshipStorage.findFriends(1).size(), 1);
    }

    // FILM

    @Test
    public void testFindFilmAll() {

        List<Film> films = filmStorage.findAll();
        assertEquals(films.size(), 2);

    }

    @Test
    public void testFindFilmById() {

        Optional<Film> filmOptional = filmStorage.findById(1);
        Optional<Film> filmOptional1 = filmStorage.findById(5);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
        assertEquals(filmOptional1, Optional.empty());
    }

    @Test
    public void testFilmUpdate() {
        RatingMpa mpa = new RatingMpa(1, null);
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, null));
        categories.add(new Category(2, null));
        Film film2 = new Film(1, "spaceRSd", "journey into spaceW", LocalDate.of(1991, 12, 27), 210, mpa, categories);
        filmStorage.update(film2);
        Optional<Film> filmOptional = filmStorage.findById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "spaceRSd")
                );
    }

    @Test
    public void testCategory() {
        List<Category> categories = categoryStorage.findAll();
        assertEquals(categories.size(), 6);
    }

    @Test
    public void testCategoryId() {
        Optional<Category> category = categoryStorage.findById(1);
        Optional<Category> category1 = categoryStorage.findById(7);
        assertTrue(category.isPresent());
        assertFalse(category1.isPresent());
    }

    @Test
    public void testMpa() {
        List<RatingMpa> ratingMpas = mpaStorage.findAll();
        assertEquals(ratingMpas.size(), 5);
    }

    @Test
    public void testMpaId() {
        Optional<RatingMpa> ratingMpa = mpaStorage.findById(1);
        Optional<RatingMpa> ratingMpa1 = mpaStorage.findById(7);
        assertTrue(ratingMpa.isPresent());
        assertFalse(ratingMpa1.isPresent());
    }
    @Test
    public void testLike() {
        User user1 = new User(0, "sdd@mail.ru", "sdd", "asr", LocalDate.of(1994, 12, 27));
        User user2 = new User(0, "sdda@mail.ru", "ROg", "Ien", LocalDate.of(1993, 12, 27));
        User user3 = new User(0, "sdaaasd@mail.ru", "Andry", "Tom", LocalDate.of(1998, 2, 12));
        RatingMpa mpa = new RatingMpa(1, null);
        RatingMpa mpa1 = new RatingMpa(2, null);
        List<Category> categories = new ArrayList<>();
        List<Category> categories1 = new ArrayList<>();
        categories.add(new Category(1, null));
        categories.add(new Category(2, null));
        categories1.add(new Category(1, null));
        categories1.add(new Category(2, null));
        Film film1 = new Film(0, "spaceRS", "journey into spaceW", LocalDate.of(1991, 12, 27), 210, mpa, categories);
        Film film2 = new Film(0, "space", "journey into space", LocalDate.of(2002, 12, 23), 220, mpa1, categories1);
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        filmStorage.create(film1);
        filmStorage.create(film2);
        likeFilmStorage.createLike(2, 1);
        likeFilmStorage.createLike(2, 2);
        assertEquals(filmStorage.filmsPopular(1).get(0).getId(), 2);
        likeFilmStorage.createLike(1, 1);
        likeFilmStorage.createLike(1, 2);
        likeFilmStorage.deleteLike(2, 1);
        likeFilmStorage.deleteLike(2, 2);
        assertEquals(filmStorage.filmsPopular(1).get(0).getId(), 1);
    }
}
