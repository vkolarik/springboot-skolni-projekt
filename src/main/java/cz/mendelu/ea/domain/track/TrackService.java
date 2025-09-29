package cz.mendelu.ea.domain.track;

import cz.mendelu.ea.utils.exceptions.NotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrackService {

    private final TrackRepository repository;

    public TrackService(TrackRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "tracks")
    public List<Track> getAllTracks() {
        return repository.findAll();
    }

    @Cacheable(value = "track", key = "#id")
    public Track getTrackById(String id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<Track> getTracksByGenre(String genre) {
        return repository.findByTrackGenre(genre);
    }

    public List<Track> getTracksByArtist(String artist) {
        return repository.findByArtistsContaining(artist);
    }

    public List<Track> getPopularTracks(Integer minPopularity) {
        return repository.findByPopularityGreaterThanEqual(minPopularity);
    }

    public List<Track> getDanceableAndEnergeticTracks(Double minDanceability, Double minEnergy) {
        return repository.findDanceableAndEnergeticTracks(minDanceability, minEnergy);
    }

    public List<Track> getAcousticAndInstrumentalTracks(Double minAcousticness, Double minInstrumentalness) {
        return repository.findAcousticAndInstrumentalTracks(minAcousticness, minInstrumentalness);
    }

    // Complex calculations using streams
    public Map<String, Double> getAverageMetricsByGenre() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Track::getTrackGenre,
                        Collectors.averagingDouble(Track::getPopularity)
                ));
    }

    public Map<String, List<Track>> getTopTracksByGenre(int limit) {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Track::getTrackGenre,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                tracks -> tracks.stream()
                                        .sorted((t1, t2) -> t2.getPopularity().compareTo(t1.getPopularity()))
                                        .limit(limit)
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public Map<String, Double> getAverageEnergyByArtist() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Track::getArtists,
                        Collectors.averagingDouble(Track::getEnergy)
                ));
    }

    public Map<String, Double> getAverageDanceabilityByGenre() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Track::getTrackGenre,
                        Collectors.averagingDouble(Track::getDanceability)
                ));
    }

    public Map<String, Double> getAverageValenceByGenre() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Track::getTrackGenre,
                        Collectors.averagingDouble(Track::getValence)
                ));
    }
} 