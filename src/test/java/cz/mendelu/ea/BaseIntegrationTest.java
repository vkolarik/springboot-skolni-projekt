package cz.mendelu.ea;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected io.restassured.response.Response get(String path) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(path);
    }

    protected io.restassured.response.Response post(String path, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path);
    }

    protected io.restassured.response.Response put(String path, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(path);
    }

    protected io.restassured.response.Response delete(String path) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(path);
    }
} 