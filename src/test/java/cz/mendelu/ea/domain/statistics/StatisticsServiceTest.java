package cz.mendelu.ea.domain.statistics;

import cz.mendelu.ea.domain.favorite.FavoriteService;
import cz.mendelu.ea.domain.statistics.dto.TrendingStatsDTO;
import cz.mendelu.ea.domain.track.TrackService;
import cz.mendelu.ea.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/test-data/base-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    @Transactional
    void getUserEngagement_ReturnsCorrectMetrics() {
        // When
        Map<String, Object> result = statisticsService.getUserEngagement();

        // Then
        assertNotNull(result);
        
        // Check average favorites per user
        Double avgFavorites = (Double) result.get("averageFavoritesPerUser");
        assertNotNull(avgFavorites);
        assertEquals(3.0, avgFavorites, 0.001); // Each user has 3 favorites

        // Check most active users
        List<?> mostActiveUsers = (List<?>) result.get("mostActiveUsers");
        assertNotNull(mostActiveUsers);
        assertTrue(mostActiveUsers.size() <= 5); // Should return at most 5 users

        // Check popular genres
        @SuppressWarnings("unchecked")
        Map<String, Long> popularGenres = (Map<String, Long>) result.get("popularGenres");
        assertNotNull(popularGenres);
        assertTrue(popularGenres.containsKey("Pop"));
        assertTrue(popularGenres.containsKey("Rock"));
        assertTrue(popularGenres.containsKey("Jazz"));
        assertTrue(popularGenres.containsKey("Classical"));
        assertTrue(popularGenres.containsKey("Electronic"));
    }

    @Test
    void getTrackPopularity_ReturnsCorrectMetrics() {
        // When
        Map<String, Object> result = statisticsService.getTrackPopularity();

        // Then
        assertNotNull(result);

        // Check most favorited tracks
        @SuppressWarnings("unchecked")
        List<TrendingStatsDTO.TrackWithCount> mostFavoritedTracks = 
            (List<TrendingStatsDTO.TrackWithCount>) result.get("mostFavoritedTracks");
        assertNotNull(mostFavoritedTracks);
        assertFalse(mostFavoritedTracks.isEmpty());

        // Check average rating by genre
        @SuppressWarnings("unchecked")
        Map<String, Double> avgRatingByGenre = (Map<String, Double>) result.get("averageRatingByGenre");
        assertNotNull(avgRatingByGenre);
        assertTrue(avgRatingByGenre.containsKey("Pop"));
        assertTrue(avgRatingByGenre.containsKey("Rock"));
        assertTrue(avgRatingByGenre.containsKey("Jazz"));
        assertTrue(avgRatingByGenre.containsKey("Classical"));
        assertTrue(avgRatingByGenre.containsKey("Electronic"));

        // Check feature correlations
        @SuppressWarnings("unchecked")
        Map<String, Double> featureCorrelations = (Map<String, Double>) result.get("featureCorrelations");
        assertNotNull(featureCorrelations);
        assertTrue(featureCorrelations.containsKey("Pop"));
        assertTrue(featureCorrelations.containsKey("Rock"));
        assertTrue(featureCorrelations.containsKey("Jazz"));
        assertTrue(featureCorrelations.containsKey("Classical"));
        assertTrue(featureCorrelations.containsKey("Electronic"));
    }

    @Test
    @Transactional
    void getUserPreferences_ReturnsCorrectMetrics() {
        // When
        Map<String, Object> result = statisticsService.getUserPreferences();

        // Then
        assertNotNull(result);

        // Check genre preferences by country
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Long>> genreByCountry = 
            (Map<String, Map<String, Long>>) result.get("genrePreferencesByCountry");
        assertNotNull(genreByCountry);
        assertTrue(genreByCountry.containsKey("USA"));
        assertTrue(genreByCountry.containsKey("UK"));
        assertTrue(genreByCountry.containsKey("Canada"));

        // Check average features by country
        @SuppressWarnings("unchecked")
        Map<String, Double> avgFeatures = (Map<String, Double>) result.get("averageFeaturesByCountry");
        assertNotNull(avgFeatures);
        assertTrue(avgFeatures.containsKey("USA"));
        assertTrue(avgFeatures.containsKey("UK"));
        assertTrue(avgFeatures.containsKey("Canada"));
    }

    @Test
    @Transactional
    void getTrending_ReturnsCorrectMetrics() {
        // Given
        LocalDateTime fixedTime = LocalDateTime.of(2024, 1, 4, 0, 0); // After all test data timestamps
        
        // When
        TrendingStatsDTO result = statisticsService.getTrending(fixedTime);

        // Then
        assertNotNull(result);
        assertNotNull(result.getRecentlyPopularTracks());
        assertNotNull(result.getEmergingGenres());
        assertNotNull(result.getUserActivityByCountry());

        // Check recently popular tracks
        assertFalse(result.getRecentlyPopularTracks().isEmpty());
        result.getRecentlyPopularTracks().forEach(track -> {
            assertNotNull(track.getId());
            assertNotNull(track.getName());
            assertNotNull(track.getArtist());
            assertNotNull(track.getGenre());
            assertNotNull(track.getCount());
        });

        // Check emerging genres
        assertFalse(result.getEmergingGenres().isEmpty());
        assertTrue(result.getEmergingGenres().containsKey("Pop"));
        assertTrue(result.getEmergingGenres().containsKey("Rock"));
        assertTrue(result.getEmergingGenres().containsKey("Jazz"));
        assertTrue(result.getEmergingGenres().containsKey("Classical"));
        assertTrue(result.getEmergingGenres().containsKey("Electronic"));

        // Check user activity by country
        assertFalse(result.getUserActivityByCountry().isEmpty());
        assertTrue(result.getUserActivityByCountry().containsKey("USA"));
        assertTrue(result.getUserActivityByCountry().containsKey("UK"));
        assertTrue(result.getUserActivityByCountry().containsKey("Canada"));
    }

    @Test
    @Transactional
    void getUserActivityTimeline_ReturnsCorrectMetrics() {
        // Given
        LocalDateTime fixedTime = LocalDateTime.of(2024, 1, 4, 0, 0); // After all test data timestamps
        
        // When
        Map<String, Object> result = statisticsService.getUserActivityTimeline(fixedTime);

        // Then
        assertNotNull(result);

        // Check daily activity
        @SuppressWarnings("unchecked")
        Map<?, Long> dailyActivity = (Map<?, Long>) result.get("dailyActivity");
        assertNotNull(dailyActivity);
        assertFalse(dailyActivity.isEmpty());

        // Check peak activity hours
        @SuppressWarnings("unchecked")
        Map<Integer, Long> peakActivityHours = (Map<Integer, Long>) result.get("peakActivityHours");
        assertNotNull(peakActivityHours);
        assertFalse(peakActivityHours.isEmpty());
        peakActivityHours.keySet().forEach(hour -> {
            assertTrue(hour >= 0 && hour <= 23);
        });

        // Check weekly patterns
        @SuppressWarnings("unchecked")
        Map<?, Long> weeklyPatterns = (Map<?, Long>) result.get("weeklyPatterns");
        assertNotNull(weeklyPatterns);
        assertFalse(weeklyPatterns.isEmpty());
    }
} 