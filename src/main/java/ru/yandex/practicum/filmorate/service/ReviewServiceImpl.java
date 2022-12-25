package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NotObjectException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.LikeReviewStorage;
import ru.yandex.practicum.filmorate.storage.film.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final LikeReviewStorage likeReviewStorage;
    private final EventStorage eventStorage;

    @Override
    public Review create(Review review) {
        Review review1 = reviewStorage.create(review);
        eventStorage.create(review1.getUserId(), EventType.REVIEW, Operation.ADD, review1.getReviewId());
        return review1;
    }

    @Override
    public Review update(Review review) {
        Review review1 = reviewStorage.update(review).orElseThrow(() -> new NotObjectException("нет отзыва"));
        eventStorage.create(review1.getUserId(), EventType.REVIEW, Operation.UPDATE, review1.getReviewId());
        return review1;
    } // Редактирование уже имеющегося отзыва.

    @Override
    public void delete(Integer id) {
        Review review1 = findById(id);
        reviewStorage.delete(id);
        eventStorage.create(review1.getUserId(), EventType.REVIEW, Operation.REMOVE, review1.getReviewId());
    } // Удаление уже имеющегося отзыва.

    @Override
    public Review findById(Integer id) {
        return reviewStorage.findById(id).orElseThrow(() -> new NotObjectException("нет отзыва"));
    } //Получение отзыва по идентификатору.

    @Override
    public List<Review> findAll(Integer filmId, Integer count) {
        return reviewStorage.findAll(filmId, count);
    } // Получение всех отзывов по идентификатору фильма, если фильм не указан, то все. Если кол-во не указано то 10

    @Override
    public void createLike(Integer id, Integer userId) {
        likeReviewStorage.createLike(id, userId);
    } // пользователь ставит лайк отзыву.

    @Override
    public void createDislike(Integer id, Integer userId) {
        likeReviewStorage.createDislike(id, userId);
    } // пользователь ставит дизлайк отзыву.

    @Override
    public void deleteLike(Integer id, Integer userId) {
        likeReviewStorage.deleteLike(id, userId);
    } //  пользователь удаляет лайк отзыву.

    @Override
    public void deleteDislike(Integer id, Integer userId) {
        likeReviewStorage.deleteDislike(id, userId);
    } //  пользователь удаляет лайк отзыву.
}
