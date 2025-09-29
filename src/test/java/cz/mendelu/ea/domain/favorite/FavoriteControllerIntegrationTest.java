package cz.mendelu.ea.domain.favorite;

import cz.mendelu.ea.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FavoriteControllerIntegrationTest extends BaseIntegrationTest {

    private static final String BASE_PATH = "/favorites";

    @Test
    void createFavorite_ValidData_ReturnsCreatedFavorite() {
        // Given
        // First create a user
        Map<String, Object> userData = Map.of(
            "username", "favoriteuser",
            "firstName", "Favorite",
            "lastName", "User",
            "email", "favorite@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "USA"
        );
        Response userResponse = post("/users", userData);
        Long userId = userResponse.jsonPath().getLong("content.id");

        // Get an existing track
        Response trackResponse = get("/tracks");
        String trackId = trackResponse.jsonPath().getString("items[0].trackId");

        // Create favorite data
        Map<String, Object> favoriteData = Map.of(
            "userId", userId,
            "trackId", trackId,
            "rating", 4
        );

        // When
        Response response = post(BASE_PATH, favoriteData);

        // Then
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("content.id", notNullValue())
                .body("content.createdAt", notNullValue())
                .body("content.rating", equalTo(4))
                .body("content.comment", nullValue())
                .body("content.isPublic", equalTo(true))
                .body("content.lastPlayed", notNullValue())
                .body("version", equalTo(1));
    }

    @Test
    void createFavorite_InvalidData_ReturnsBadRequest() {
        // Given
        Map<String, Object> invalidFavoriteData = Map.of(
            "userId", 999999,  // Non-existing user
            "trackId", "non-existing-track",
            "rating", 6.0  // Invalid rating > 5
        );

        // When
        Response response = post(BASE_PATH, invalidFavoriteData);

        // Then
        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAllFavorites_ReturnsListOfFavorites() {
        // When
        Response response = get(BASE_PATH);

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("items", notNullValue())
                .body("items", not(empty()))
                .body("items[0].id", notNullValue())
                .body("items[0].createdAt", notNullValue())
                .body("items[0].rating", notNullValue())
                .body("items[0].comment", nullValue())
                .body("items[0].isPublic", equalTo(true))
                .body("items[0].lastPlayed", notNullValue())
                .body("count", notNullValue())
                .body("version", equalTo(1));
    }

    @Test
    void getFavoriteById_ExistingFavorite_ReturnsFavorite() {
        // Given
        // First create a user
        Map<String, Object> userData = Map.of(
            "username", "favoriteuser2",
            "firstName", "Favorite",
            "lastName", "User2",
            "email", "favorite2@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "UK"
        );
        Response userResponse = post("/users", userData);
        Long userId = userResponse.jsonPath().getLong("content.id");

        Response trackResponse = get("/tracks");
        String trackId = trackResponse.jsonPath().getString("items[0].trackId");

        Map<String, Object> favoriteData = Map.of(
            "userId", userId,
            "trackId", trackId,
            "rating", 4
        );
        Response createResponse = post(BASE_PATH, favoriteData);
        long favoriteId = createResponse.jsonPath().getLong("content.id");

        // When
        Response response = get(BASE_PATH + "/" + favoriteId);

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.id", equalTo((int) favoriteId))
                .body("content.createdAt", notNullValue())
                .body("content.rating", equalTo(4))
                .body("content.comment", nullValue())
                .body("content.isPublic", equalTo(true))
                .body("content.lastPlayed", notNullValue())
                .body("version", equalTo(1));
    }

    @Test
    void getFavoriteById_NonExistingFavorite_ReturnsNotFound() {
        // When
        Response response = get(BASE_PATH + "/999999");

        // Then
        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateFavorite_ValidData_ReturnsUpdatedFavorite() {
        // Given
        // First create a user
        Map<String, Object> userData = Map.of(
            "username", "favoriteuser3",
            "firstName", "Favorite",
            "lastName", "User3",
            "email", "favorite3@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "Canada"
        );
        Response userResponse = post("/users", userData);
        Long userId = userResponse.jsonPath().getLong("content.id");

        Response trackResponse = get("/tracks");
        String trackId = trackResponse.jsonPath().getString("items[0].trackId");

        Map<String, Object> favoriteData = Map.of(
            "userId", userId,
            "trackId", trackId,
            "rating", 3
        );
        Response createResponse = post(BASE_PATH, favoriteData);
        long favoriteId = createResponse.jsonPath().getLong("content.id");

        // Update data
        Map<String, Object> updateData = Map.of(
            "rating", 5,
            "isPublic", true
        );

        // When
        Response response = put(BASE_PATH + "/" + favoriteId, updateData);

        // Then
        response.then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body("content.id", equalTo((int) favoriteId))
                .body("content.createdAt", notNullValue())
                .body("content.rating", equalTo(5))
                .body("content.comment", nullValue())
                .body("content.isPublic", equalTo(true))
                .body("content.lastPlayed", notNullValue())
                .body("version", equalTo(1));
    }

    @Test
    void deleteFavorite_ExistingFavorite_ReturnsNoContent() {
        // Given
        // First create a user
        Map<String, Object> userData = Map.of(
            "username", "favoriteuser4",
            "firstName", "Favorite",
            "lastName", "User4",
            "email", "favorite4@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "Germany"
        );
        Response userResponse = post("/users", userData);
        Long userId = userResponse.jsonPath().getLong("content.id");

        Response trackResponse = get("/tracks");
        String trackId = trackResponse.jsonPath().getString("items[0].trackId");

        Map<String, Object> favoriteData = Map.of(
            "userId", userId,
            "trackId", trackId,
            "rating", 4.5
        );
        Response createResponse = post(BASE_PATH, favoriteData);
        Long favoriteId = createResponse.jsonPath().getLong("content.id");

        // When
        Response response = delete(BASE_PATH + "/" + favoriteId);

        // Then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Verify favorite is deleted
        Response getResponse = get(BASE_PATH + "/" + favoriteId);
        getResponse.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deleteFavorite_NonExistingFavorite_ReturnsNotFound() {
        // When
        Response response = delete(BASE_PATH + "/999999");

        // Then
        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
} 