package cz.mendelu.ea.domain.user;

import cz.mendelu.ea.BaseIntegrationTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserControllerIntegrationTest extends BaseIntegrationTest {

    private static final String BASE_PATH = "/users";

    @Test
    void createUser_ValidData_ReturnsCreatedUser() {
        // Given
        Map<String, Object> userData = Map.of(
            "username", "testuser1",
            "firstName", "Test",
            "lastName", "User",
            "email", "test1@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "USA"
        );

        // When
        Response response = post(BASE_PATH, userData);

        // Then
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("content.username", equalTo("testuser1"))
                .body("content.firstName", equalTo("Test"))
                .body("content.lastName", equalTo("User"))
                .body("content.email", equalTo("test1@example.com"))
                .body("content.dateOfBirth", equalTo("1990-01-01"))
                .body("content.country", equalTo("USA"));
    }

    @Test
    void createUser_InvalidData_ReturnsBadRequest() {
        // Given
        Map<String, Object> invalidUserData = Map.of(
            "username", "",  // Invalid empty username
            "firstName", "",  // Invalid empty first name
            "lastName", "",  // Invalid empty last name
            "email", "invalid-email",  // Invalid email format
            "dateOfBirth", "",  // Invalid null date of birth
            "country", ""  // Invalid empty country
        );

        // When
        Response response = post(BASE_PATH, invalidUserData);

        // Then
        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // When
        Response response = get(BASE_PATH);

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("items", notNullValue())
                .body("count", notNullValue())
                .body("version", equalTo(1));
    }

    @Test
    void getUserById_ExistingUser_ReturnsUser() {
        // Given
        // First create a user
        Map<String, Object> userData = Map.of(
            "username", "testuser2",
            "firstName", "Test",
            "lastName", "User2",
            "email", "test2@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "UK"
        );
        Response createResponse = post(BASE_PATH, userData);
        long userId = createResponse.jsonPath().getLong("content.id");

        // When
        Response response = get(BASE_PATH + "/" + userId);

        // Then
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("content.id", equalTo((int) userId))
                .body("content.username", equalTo("testuser2"))
                .body("content.firstName", equalTo("Test"))
                .body("content.lastName", equalTo("User2"))
                .body("content.email", equalTo("test2@example.com"))
                .body("content.dateOfBirth", equalTo("1990-01-01"))
                .body("content.country", equalTo("UK"));
    }

    @Test
    void getUserById_NonExistingUser_ReturnsNotFound() {
        // When
        Response response = get(BASE_PATH + "/999999");

        // Then
        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateUser_ValidData_ReturnsUpdatedUser() {
        // Given
        // First create a user
        Map<String, Object> userData = Map.of(
            "username", "testuser3",
            "firstName", "Test",
            "lastName", "User3",
            "email", "test3@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "Canada"
        );
        Response createResponse = post(BASE_PATH, userData);
        long userId = createResponse.jsonPath().getLong("content.id");

        // Update data
        Map<String, Object> updateData = Map.of(
            "username", "updateduser",
            "firstName", "Updated",
            "lastName", "User",
            "email", "updated@example.com",
            "country", "Australia"
        );

        // When
        Response response = put(BASE_PATH + "/" + userId, updateData);

        // Then
        response.then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body("content.id", equalTo((int) userId))
                .body("content.username", equalTo("updateduser"))
                .body("content.firstName", equalTo("Updated"))
                .body("content.lastName", equalTo("User"))
                .body("content.email", equalTo("updated@example.com"))
                .body("content.country", equalTo("Australia"));
    }

    @Test
    void deleteUser_ExistingUser_ReturnsNoContent() {
        // Given
        // First create a user
        Map<String, Object> userData = Map.of(
            "username", "testuser4",
            "firstName", "Test",
            "lastName", "User4",
            "email", "test4@example.com",
            "dateOfBirth", "1990-01-01",
            "country", "Germany"
        );
        Response createResponse = post(BASE_PATH, userData);
        long userId = createResponse.jsonPath().getLong("content.id");

        // When
        Response response = delete(BASE_PATH + "/" + userId);

        // Then
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // Verify user is deleted
        Response getResponse = get(BASE_PATH + "/" + userId);
        getResponse.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void deleteUser_NonExistingUser_ReturnsNotFound() {
        // When
        Response response = delete(BASE_PATH + "/999999");

        // Then
        response.then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
} 