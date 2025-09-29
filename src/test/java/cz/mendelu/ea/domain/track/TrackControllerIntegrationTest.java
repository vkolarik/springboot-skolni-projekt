package cz.mendelu.ea.domain.track;

import cz.mendelu.ea.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TrackControllerIntegrationTest extends BaseIntegrationTest {

    private static final String BASE_PATH = "/tracks";

    @Test
    void getAllTracks_ReturnsListOfTracks() {
        // When
        Response response = get(BASE_PATH);

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("items", notNullValue())
                .body("items", not(empty()))
                .body("items[0].trackId", notNullValue())
                .body("items[0].artists", notNullValue())
                .body("items[0].albumName", notNullValue())
                .body("items[0].trackName", notNullValue())
                .body("items[0].popularity", notNullValue())
                .body("items[0].durationMs", notNullValue())
                .body("items[0].explicit", notNullValue())
                .body("items[0].danceability", notNullValue())
                .body("items[0].energy", notNullValue())
                .body("items[0].key", notNullValue())
                .body("items[0].loudness", notNullValue())
                .body("items[0].mode", notNullValue())
                .body("items[0].speechiness", notNullValue())
                .body("items[0].acousticness", notNullValue())
                .body("items[0].instrumentalness", notNullValue())
                .body("items[0].liveness", notNullValue())
                .body("items[0].valence", notNullValue())
                .body("items[0].tempo", notNullValue())
                .body("items[0].timeSignature", notNullValue())
                .body("items[0].trackGenre", notNullValue())
                .body("items[0].favorites", notNullValue());
    }

    @Test
    void getTrackById_ExistingTrack_ReturnsTrack() {
        // Given
        // First get all tracks to find an existing ID
        Response getAllResponse = get(BASE_PATH);
        String trackId = getAllResponse.jsonPath().getString("items[0].trackId");

        // When
        Response response = get(BASE_PATH + "/" + trackId);

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.trackId", equalTo(trackId))
                .body("content.artists", notNullValue())
                .body("content.albumName", notNullValue())
                .body("content.trackName", notNullValue())
                .body("content.popularity", notNullValue())
                .body("content.durationMs", notNullValue())
                .body("content.explicit", notNullValue())
                .body("content.danceability", notNullValue())
                .body("content.energy", notNullValue())
                .body("content.key", notNullValue())
                .body("content.loudness", notNullValue())
                .body("content.mode", notNullValue())
                .body("content.speechiness", notNullValue())
                .body("content.acousticness", notNullValue())
                .body("content.instrumentalness", notNullValue())
                .body("content.liveness", notNullValue())
                .body("content.valence", notNullValue())
                .body("content.tempo", notNullValue())
                .body("content.timeSignature", notNullValue())
                .body("content.trackGenre", notNullValue())
                .body("content.favorites", notNullValue())
                .body("version", equalTo(1));
    }

    @Test
    void getTrackById_NonExistingTrack_ReturnsNotFound() {
        // When
        Response response = get(BASE_PATH + "/non-existing-track-id");

        // Then
        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
} 