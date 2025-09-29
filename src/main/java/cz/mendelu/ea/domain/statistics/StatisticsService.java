package cz.mendelu.ea.domain.statistics;

import cz.mendelu.ea.domain.favorite.FavoriteService;
import cz.mendelu.ea.domain.statistics.dto.TrendingStatsDTO;
import cz.mendelu.ea.domain.track.TrackService;
import cz.mendelu.ea.domain.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final UserService userService;
    private final TrackService trackService;
    private final FavoriteService favoriteService;

    public StatisticsService(UserService userService, TrackService trackService, FavoriteService favoriteService) {
        this.userService = userService;
        this.trackService = trackService;
        this.favoriteService = favoriteService;
    }

    /**
     * Calculates user engagement metrics to understand how users interact with the platform.
     * 
     * This method analyzes:
     * 1. Average favorites per user - Calculates the mean number of favorites across all users
     * 2. Most active users - Identifies the top 5 users with the most favorites
     * 3. Popular genres - Counts the total number of favorites for each genre
     * 
     * @return Map containing:
     *         - averageFavoritesPerUser: Double representing the mean favorites per user
     *         - mostActiveUsers: List of top 5 users sorted by favorite count
     *         - popularGenres: Map of genre names to their total favorite count
     */
    public Map<String, Object> getUserEngagement() {
        Map<String, Object> result = new HashMap<>();
        
        // Calculate average favorites per user across all users
        double avgFavorites = userService.getAllUsers().stream()
                .mapToDouble(user -> user.getFavorites().size())
                .average()
                .orElse(0.0);
        result.put("averageFavoritesPerUser", avgFavorites);

        // Find top 5 most active users based on their favorite count
        var mostActiveUsers = userService.getAllUsers().stream()
                .sorted((u1, u2) -> Integer.compare(u2.getFavorites().size(), u1.getFavorites().size()))
                .limit(5)
                .collect(Collectors.toList());
        result.put("mostActiveUsers", mostActiveUsers);

        // Count total favorites for each genre
        var popularGenres = favoriteService.getAllFavorites().stream()
                .collect(Collectors.groupingBy(
                        f -> f.getTrack().getTrackGenre(),
                        Collectors.counting()
                ));
        result.put("popularGenres", popularGenres);

        return result;
    }

    /**
     * Analyzes track popularity metrics based on user interactions and ratings.
     * 
     * This method calculates:
     * 1. Most favorited tracks - Tracks with their favorite counts and details
     * 2. Average ratings by genre - Mean rating for tracks in each genre
     * 3. Feature correlations - Relationship between track features and ratings
     * 
     * @return Map containing:
     *         - mostFavoritedTracks: List of tracks with their favorite counts
     *         - averageRatingByGenre: Map of genres to their average ratings
     *         - featureCorrelations: Map of genres to their feature-rating correlations
     */
    public Map<String, Object> getTrackPopularity() {
        Map<String, Object> result = new HashMap<>();

        // Calculate most favorited tracks with their details
        var mostFavoritedTracks = favoriteService.getAllFavorites().stream()
                .collect(Collectors.groupingBy(
                        f -> {
                            var track = f.getTrack();
                            var trackInfo = new TrendingStatsDTO.TrackWithCount();
                            trackInfo.setId(track.getTrackId());
                            trackInfo.setName(track.getTrackName());
                            trackInfo.setArtist(track.getArtists());
                            trackInfo.setGenre(track.getTrackGenre());
                            trackInfo.setPopularity(track.getPopularity());
                            return trackInfo;
                        },
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    entry.getKey().setCount(entry.getValue());
                    return entry.getKey();
                })
                .collect(Collectors.toList());
        result.put("mostFavoritedTracks", mostFavoritedTracks);

        // Calculate average rating for each genre
        var avgRatingByGenre = favoriteService.getAllFavorites().stream()
                .collect(Collectors.groupingBy(
                        f -> f.getTrack().getTrackGenre(),
                        Collectors.averagingDouble(f -> f.getRating())
                ));
        result.put("averageRatingByGenre", avgRatingByGenre);

        // Calculate correlation between track features and ratings
        var featureCorrelations = favoriteService.getAllFavorites().stream()
                .collect(Collectors.groupingBy(
                        f -> f.getTrack().getTrackGenre(),
                        Collectors.averagingDouble(f -> f.getRating() * f.getTrack().getDanceability())
                ));
        result.put("featureCorrelations", featureCorrelations);

        return result;
    }

    /**
     * Analyzes user preferences across different demographics.
     * 
     * This method examines:
     * 1. Genre preferences by country - Distribution of favorite genres for each country
     * 2. Average track features by country - Mean track features preferred by users in each country
     * 
     * @return Map containing:
     *         - genrePreferencesByCountry: Nested map of countries to their genre preferences
     *         - averageFeaturesByCountry: Map of countries to their preferred track features
     */
    public Map<String, Object> getUserPreferences() {
        Map<String, Object> result = new HashMap<>();

        // Analyze genre preferences by country
        var genreByCountry = userService.getAllUsers().stream()
                .collect(Collectors.groupingBy(
                        user -> user.getCountry(),
                        Collectors.flatMapping(
                                user -> user.getFavorites().stream(),
                                Collectors.groupingBy(
                                        f -> f.getTrack().getTrackGenre(),
                                        Collectors.counting()
                                )
                        )
                ));
        result.put("genrePreferencesByCountry", genreByCountry);

        // Calculate average track features preferred by users in each country
        var avgFeatures = favoriteService.getAllFavorites().stream()
                .collect(Collectors.groupingBy(
                        f -> f.getUser().getCountry(),
                        Collectors.averagingDouble(f -> f.getTrack().getDanceability())
                ));
        result.put("averageFeaturesByCountry", avgFeatures);

        return result;
    }

    /**
     * Identifies current trends and patterns in user activity and track popularity.
     * 
     * This method analyzes:
     * 1. Recently popular tracks - Tracks that gained favorites in the last 7 days
     * 2. Emerging genres - Genres that gained popularity in the last 30 days
     * 3. User activity patterns - Activity levels by country in the last 7 days
     * 
     * @param currentTime The reference time point for calculating trends
     * @return TrendingStatsDTO containing:
     *         - recentlyPopularTracks: List of tracks with their recent favorite counts
     *         - emergingGenres: Map of genres to their recent popularity
     *         - userActivityByCountry: Map of countries to their recent activity levels
     */
    public TrendingStatsDTO getTrending(LocalDateTime currentTime) {
        TrendingStatsDTO result = new TrendingStatsDTO();
        LocalDateTime weekAgo = currentTime.minusDays(7);
        LocalDateTime monthAgo = currentTime.minusDays(30);

        // Find recently popular tracks (last 7 days)
        var recentFavorites = favoriteService.getAllFavorites().stream()
                .filter(f -> f.getLastPlayed() != null && f.getLastPlayed().isAfter(weekAgo))
                .collect(Collectors.groupingBy(
                        f -> {
                            var track = f.getTrack();
                            var trackInfo = new TrendingStatsDTO.TrackWithCount();
                            trackInfo.setId(track.getTrackId());
                            trackInfo.setName(track.getTrackName());
                            trackInfo.setArtist(track.getArtists());
                            trackInfo.setGenre(track.getTrackGenre());
                            trackInfo.setPopularity(track.getPopularity());
                            return trackInfo;
                        },
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    entry.getKey().setCount(entry.getValue());
                    return entry.getKey();
                })
                .collect(Collectors.toList());
        result.setRecentlyPopularTracks(recentFavorites);

        // Identify emerging genres (last 30 days)
        var emergingGenres = favoriteService.getAllFavorites().stream()
                .filter(f -> f.getLastPlayed() != null && f.getLastPlayed().isAfter(monthAgo))
                .collect(Collectors.groupingBy(
                        f -> f.getTrack().getTrackGenre(),
                        Collectors.counting()
                ));
        result.setEmergingGenres(emergingGenres);

        // Analyze user activity patterns by country (last 7 days)
        var userActivity = favoriteService.getAllFavorites().stream()
                .filter(f -> f.getLastPlayed() != null && f.getLastPlayed().isAfter(weekAgo))
                .collect(Collectors.groupingBy(
                        f -> f.getUser().getCountry(),
                        Collectors.counting()
                ));
        result.setUserActivityByCountry(userActivity);

        return result;
    }

    /**
     * Identifies current trends and patterns in user activity and track popularity.
     * Uses the current system time as reference.
     * 
     * @return TrendingStatsDTO containing trending statistics
     */
    public TrendingStatsDTO getTrending() {
        return getTrending(LocalDateTime.now());
    }

    /**
     * Analyzes user activity patterns over different time periods.
     * 
     * This method examines:
     * 1. Daily activity - Number of favorites added per day in the last week
     * 2. Peak activity hours - Most active hours of the day
     * 3. Weekly patterns - Activity levels by day of the week
     * 
     * @param currentTime The reference time point for calculating activity patterns
     * @return Map containing:
     *         - dailyActivity: Map of dates to number of favorites added
     *         - peakActivityHours: Map of hours (0-23) to activity count
     *         - weeklyPatterns: Map of days of week to activity count
     */
    public Map<String, Object> getUserActivityTimeline(LocalDateTime currentTime) {
        Map<String, Object> result = new HashMap<>();
        LocalDateTime weekAgo = currentTime.minusDays(7);

        // Analyze daily activity in the last week
        var dailyActivity = favoriteService.getAllFavorites().stream()
                .filter(f -> f.getLastPlayed() != null && f.getLastPlayed().isAfter(weekAgo))
                .collect(Collectors.groupingBy(
                        f -> f.getLastPlayed().toLocalDate(),
                        Collectors.counting()
                ));
        result.put("dailyActivity", dailyActivity);

        // Find peak activity hours
        var peakActivityHours = favoriteService.getAllFavorites().stream()
                .filter(f -> f.getLastPlayed() != null)
                .collect(Collectors.groupingBy(
                        f -> f.getLastPlayed().getHour(),
                        Collectors.counting()
                ));
        result.put("peakActivityHours", peakActivityHours);

        // Analyze weekly patterns
        var weeklyPatterns = favoriteService.getAllFavorites().stream()
                .filter(f -> f.getLastPlayed() != null)
                .collect(Collectors.groupingBy(
                        f -> f.getLastPlayed().getDayOfWeek(),
                        Collectors.counting()
                ));
        result.put("weeklyPatterns", weeklyPatterns);

        return result;
    }

    /**
     * Analyzes user activity patterns over different time periods.
     * Uses the current system time as reference.
     * 
     * @return Map containing activity patterns
     */
    public Map<String, Object> getUserActivityTimeline() {
        return getUserActivityTimeline(LocalDateTime.now());
    }
} 