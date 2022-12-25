package ru.yandex.practicum.filmorate.recommendation;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserBasedRating {

    private static final int MIN_NUM_COMMON = 1;
    private static final double OPPOSITE_SIMILARITY = -1.0;
    private static final int similarityNum = 50;
    private static final int minimalRater = 1;

    private double calUserSim(Map<Integer, Boolean> userLikes,
                              Map<Integer, Boolean> otherUserLikes) {
        double similarityScore = 0.0;
        double nomOther = 0.0;
        int minNumCommon = 0;
        List<Integer> userRatedFilms = getRatedFilms(userLikes);
        for (Integer userFilm : userRatedFilms) {
            boolean otherUserRating = otherUserLikes.getOrDefault(userFilm, false);
            if (otherUserRating) {
                minNumCommon++;
                similarityScore++;
                nomOther++;
            }
        }
        if (minNumCommon >= MIN_NUM_COMMON && similarityScore != 0.0 && nomOther != 0.0) {
            return similarityScore / (Math.sqrt(nomOther));
        }
        return OPPOSITE_SIMILARITY;
    }

    private List<Integer> getRatedFilms(Map<Integer, Boolean> films) {
        return films.keySet().stream().filter(films::get).collect(Collectors.toList());
    }

    private Map<Integer, Double> getUserSimilarity(int userId,
                                                   Map<Integer, Map<Integer, Boolean>> userFilmLikes) {
        Map<Integer, Double> similarity = userFilmLikes.keySet().stream()
                .filter(k -> k != userId)
                .collect(Collectors.toMap(Function.identity(),
                        k -> calUserSim(userFilmLikes.get(userId), userFilmLikes.get(k))));
        similarity = similarity.entrySet().stream()
                .filter(e -> !e.getValue().equals(OPPOSITE_SIMILARITY))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return similarity;
    }

    public Map<Integer, Double> getSimilarRatings(int userId, Map<Integer, Map<Integer, Boolean>> userFilmLikes) {
        Map<Integer, Double> similarityRating = new HashMap<>();
        Map<Integer, Double> similarityScores = getUserSimilarity(userId, userFilmLikes);
        int numSimilarUsers = Math.min(similarityScores.size(), similarityNum);
        List<Map.Entry<Integer, Double>> highestSimilarityScores = similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(numSimilarUsers)
                .collect(Collectors.toList());
        List<Integer> unratedFilms = getUnratedFilms(userFilmLikes.get(userId));
        for (int filmId : unratedFilms) {
            int counter = 0;
            double norm = 0.0;
            double total = 0.0;
            for (int i = 0; i < numSimilarUsers; i++) {
                counter++;
                Map.Entry<Integer, Double> userCosineScore = highestSimilarityScores.get(i);
                norm += Math.abs(userCosineScore.getValue());
                int otherUserRating = userFilmLikes.get(userCosineScore.getKey()).get(filmId) ? 1 : 0;
                total += userCosineScore.getValue() * otherUserRating;
            }
            if (counter >= minimalRater) {
                double predRating = total / norm;
                similarityRating.put(filmId, predRating);
            }
        }
        return similarityRating;
    }

    private List<Integer> getUnratedFilms(Map<Integer, Boolean> films) {
        return films.keySet().stream().filter(k -> !films.get(k)).collect(Collectors.toList());
    }

    public Map<Integer, Double> getCleanSimilarRatings(int userId, Map<Integer, Map<Integer, Boolean>> userFilmLikes) {
        return getSimilarRatings(userId, userFilmLikes).entrySet().stream()
                .filter(e -> e.getValue() != 0.0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
