package cz.mendelu.ea.domain.track;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, String> {
    List<Track> findByTrackGenre(String genre);
    
    List<Track> findByArtistsContaining(String artist);
    
    List<Track> findByPopularityGreaterThanEqual(Integer minPopularity);
    
    @Query("SELECT t FROM Track t WHERE t.danceability > ?1 AND t.energy > ?2")
    List<Track> findDanceableAndEnergeticTracks(Double minDanceability, Double minEnergy);
    
    @Query("SELECT t FROM Track t WHERE t.acousticness > ?1 AND t.instrumentalness > ?2")
    List<Track> findAcousticAndInstrumentalTracks(Double minAcousticness, Double minInstrumentalness);
} 