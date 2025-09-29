package cz.mendelu.ea.domain.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByTrack_TrackId(String trackId);
    
    List<Favorite> findByRating(Integer rating);
    
    List<Favorite> findByIsPublicTrue();
    
    @Query("SELECT f FROM Favorite f WHERE f.createdAt > ?1")
    List<Favorite> findRecentFavorites(LocalDateTime date);
    
    @Query("SELECT f FROM Favorite f WHERE f.lastPlayed > ?1")
    List<Favorite> findRecentlyPlayedFavorites(LocalDateTime date);
} 