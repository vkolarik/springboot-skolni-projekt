package cz.mendelu.ea.domain.favorite;

import cz.mendelu.ea.domain.track.Track;
import cz.mendelu.ea.domain.track.TrackService;
import cz.mendelu.ea.domain.user.User;
import cz.mendelu.ea.domain.user.UserService;
import cz.mendelu.ea.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository repository;
    private final UserService userService;
    private final TrackService trackService;

    public FavoriteService(FavoriteRepository repository, UserService userService, TrackService trackService) {
        this.repository = repository;
        this.userService = userService;
        this.trackService = trackService;
    }

    public Favorite createFavorite(Long userId, String trackId, Integer rating, String comment, Boolean isPublic) {
        User user = userService.getUserById(userId);
        Track track = trackService.getTrackById(trackId);

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setTrack(track);
        favorite.setRating(rating);
        favorite.setComment(comment);
        favorite.setIsPublic(isPublic);
        favorite.setCreatedAt(LocalDateTime.now());
        favorite.setLastPlayed(LocalDateTime.now());

        return repository.save(favorite);
    }

    public List<Favorite> getAllFavorites() {
        return repository.findAll();
    }

    public Favorite getFavoriteById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Favorite> getFavoritesByTrackId(String trackId) {
        return repository.findByTrack_TrackId(trackId);
    }

    public List<Favorite> getFavoritesByRating(Integer rating) {
        return repository.findByRating(rating);
    }

    public List<Favorite> getPublicFavorites() {
        return repository.findByIsPublicTrue();
    }

    public List<Favorite> getRecentFavorites(LocalDateTime date) {
        return repository.findRecentFavorites(date);
    }

    public List<Favorite> getRecentlyPlayedFavorites(LocalDateTime date) {
        return repository.findRecentlyPlayedFavorites(date);
    }

    public void updateLastPlayed(Long id) {
        Favorite favorite = getFavoriteById(id);
        favorite.setLastPlayed(LocalDateTime.now());
        repository.save(favorite);
    }

    public void deleteFavorite(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException();
        }
        repository.deleteById(id);
    }

    // Complex calculations using streams
    public Map<Integer, Long> getFavoritesCountByRating() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Favorite::getRating,
                        Collectors.counting()
                ));
    }

    public Map<String, Double> getAverageRatingByTrack() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        favorite -> favorite.getTrack().getTrackName(),
                        Collectors.averagingDouble(Favorite::getRating)
                ));
    }

    public Map<String, List<Favorite>> getTopRatedFavoritesByTrack(int limit) {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        favorite -> favorite.getTrack().getTrackName(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                favorites -> favorites.stream()
                                        .sorted((f1, f2) -> f2.getRating().compareTo(f1.getRating()))
                                        .limit(limit)
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public Map<String, Double> getAverageRatingByGenre() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        favorite -> favorite.getTrack().getTrackGenre(),
                        Collectors.averagingDouble(Favorite::getRating)
                ));
    }

    public Map<String, Long> getFavoritesCountByGenre() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        favorite -> favorite.getTrack().getTrackGenre(),
                        Collectors.counting()
                ));
    }

    public Favorite updateFavorite(Favorite favorite) {
        return repository.save(favorite);
    }
} 