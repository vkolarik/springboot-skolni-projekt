package cz.mendelu.ea.domain.statistics;

import cz.mendelu.ea.domain.statistics.dto.TrendingStatsDTO;
import cz.mendelu.ea.utils.response.ObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
@Tag(name = "Statistics", description = "APIs for retrieving various statistics and analytics about users, tracks, and their interactions")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/user-engagement")
    @Operation(
        summary = "Get user engagement metrics",
        description = """
            Retrieves comprehensive metrics about user engagement with the platform.
            This endpoint provides insights into how users interact with tracks through favorites.
            
            The response includes:
            - Average number of favorites per user across the platform
            - Top 5 most active users based on their favorite count
            - Distribution of favorite genres among all users
            
            This data helps understand user behavior and platform usage patterns."""
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User engagement metrics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "content": {
                            "averageFavoritesPerUser": 15.5,
                            "mostActiveUsers": [
                              {
                                "id": 1,
                                "username": "user1",
                                "favoritesCount": 45
                              }
                            ],
                            "popularGenres": {
                              "Pop": 150,
                              "Rock": 120
                            }
                          },
                          "version": 1
                        }"""
                )
            )
        )
    })
    public ObjectResponse<Map<String, Object>> getUserEngagement() {
        // Combines user activity with their favorite tracks
        // Returns: Map of user engagement metrics including:
        // - Average favorites per user
        // - Most active users
        // - Most popular genres among users
        return ObjectResponse.of(statisticsService.getUserEngagement(), map -> map);
    }

    @GetMapping("/track-popularity")
    @Operation(
        summary = "Get track popularity metrics",
        description = """
            Analyzes track popularity based on user interactions and ratings.
            This endpoint provides insights into which tracks are most popular and how users rate different genres.
            
            The response includes:
            - List of most favorited tracks with their details and favorite counts
            - Average ratings given to tracks by genre
            - Correlation between track features (like danceability) and user ratings
            
            This data helps understand track performance and user preferences."""
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Track popularity metrics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "content": {
                            "mostFavoritedTracks": [
                              {
                                "id": "track1",
                                "name": "Popular Track",
                                "artist": "Artist Name",
                                "genre": "Pop",
                                "popularity": 85,
                                "count": 25
                              }
                            ],
                            "averageRatingByGenre": {
                              "Pop": 4.5,
                              "Rock": 4.2
                            },
                            "featureCorrelations": {
                              "Pop": 3.8,
                              "Rock": 3.5
                            }
                          },
                          "version": 1
                        }"""
                )
            )
        )
    })
    public ObjectResponse<Map<String, Object>> getTrackPopularity() {
        // Combines track data with user favorites
        // Returns: Map of track popularity metrics including:
        // - Most favorited tracks
        // - Average rating by genre
        // - Correlation between track features and user ratings
        return ObjectResponse.of(statisticsService.getTrackPopularity(), map -> map);
    }

    @GetMapping("/user-preferences")
    @Operation(
        summary = "Get user preference metrics",
        description = """
            Analyzes user music preferences across different demographics.
            This endpoint provides insights into how music preferences vary by country and user demographics.
            
            The response includes:
            - Genre preferences broken down by user country
            - Average track features preferred by users in each country
            - User clusters based on music taste
            
            This data helps understand regional music preferences and user behavior patterns."""
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User preference metrics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "content": {
                            "genrePreferencesByCountry": {
                              "USA": {
                                "Pop": 45,
                                "Rock": 30
                              },
                              "UK": {
                                "Pop": 35,
                                "Rock": 40
                              }
                            },
                            "averageFeaturesByCountry": {
                              "USA": 0.75,
                              "UK": 0.65
                            }
                          },
                          "version": 1
                        }"""
                )
            )
        )
    })
    public ObjectResponse<Map<String, Object>> getUserPreferences() {
        // Analyzes user preferences based on their favorites
        // Returns: Map of user preference metrics including:
        // - Genre preferences by country
        // - Average track features preferred by users
        // - User clusters based on music taste
        return ObjectResponse.of(statisticsService.getUserPreferences(), map -> map);
    }

    @GetMapping("/trending")
    @Operation(
        summary = "Get trending metrics",
        description = """
            Identifies current trends and patterns in user activity and track popularity.
            This endpoint provides insights into what's currently popular and emerging trends.
            
            The response includes:
            - Recently popular tracks from the last 7 days
            - Emerging genres that gained popularity in the last 30 days
            - User activity patterns by country in the last 7 days
            
            This data helps identify current trends and user engagement patterns."""
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Trending metrics retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "content": {
                            "recentlyPopularTracks": [
                              {
                                "id": "track1",
                                "name": "Trending Track",
                                "artist": "Artist Name",
                                "genre": "Pop",
                                "popularity": 90,
                                "count": 15
                              }
                            ],
                            "emergingGenres": {
                              "Pop": 45,
                              "Rock": 30
                            },
                            "userActivityByCountry": {
                              "USA": 25,
                              "UK": 15
                            }
                          },
                          "version": 1
                        }"""
                )
            )
        )
    })
    public ObjectResponse<TrendingStatsDTO> getTrending() {
        // Identifies trending content and patterns
        // Returns: Map of trending metrics including:
        // - Recently popular tracks
        // - Emerging genres
        // - User activity patterns
        return ObjectResponse.of(statisticsService.getTrending(), dto -> dto);
    }

    @GetMapping("/user-activity-timeline")
    @Operation(
        summary = "Get user activity timeline",
        description = """
            Analyzes user activity patterns over different time periods.
            This endpoint provides insights into when users are most active on the platform.
            
            The response includes:
            - Daily activity counts for the last 7 days
            - Peak activity hours throughout the day
            - Weekly patterns showing activity by day of the week
            
            This data helps understand user engagement patterns and optimal times for platform updates."""
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User activity timeline retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                        {
                          "content": {
                            "dailyActivity": {
                              "2024-03-14": 25,
                              "2024-03-15": 30,
                              "2024-03-16": 45
                            },
                            "peakActivityHours": {
                              "14": 35,
                              "15": 40,
                              "16": 45
                            },
                            "weeklyPatterns": {
                              "MONDAY": 120,
                              "TUESDAY": 150,
                              "WEDNESDAY": 130
                            }
                          },
                          "version": 1
                        }"""
                )
            )
        )
    })
    public ObjectResponse<Map<String, Object>> getUserActivityTimeline() {
        return ObjectResponse.of(statisticsService.getUserActivityTimeline(), map -> map);
    }
} 