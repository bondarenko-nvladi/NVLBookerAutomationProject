package core.clients;

import io.restassured.response.Response;
import settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class APIClient {

    private final String baseUrl;

    public APIClient() {
        this.baseUrl = determineBaseUrl();
    }

    //Геттер для baseUrl
    public String getBaseUrl() {
        return baseUrl;
    }

    //Определение базового URL на основе файла конфигурации
    private String determineBaseUrl() {
        String environment = System.getProperty("env", "test");
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found: " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load configuration file: " + configFileName, e);
        }
        return properties.getProperty("baseUrl");
    }

    //Настройка базовых параметров HTTP-запросов
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }


    //GET запрос на эндпойнт /ping
    public Response ping() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.PING.getPath()) //Используем ENUM для эндпойнта /ping
                .then()
                .statusCode(201) //Ожидаемый статус код 201 Created
                .extract()
                .response();
    }

    //GET запрос на эндпойнт /booking
    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath()) //Используем ENUM для эндпойнта /booking
                .then()
                .statusCode(200) //Ожидаемый статус код 200
                .extract()
                .response();
    }

    //GET запрос на эндпойнт /booking/111
    public Response getBookingById (int bookingId) {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING_BY_ID.getPath() + bookingId) //Используем ENUM для эндпойнта /bookingId=111
                .then()
                .statusCode(200) //Ожидаемый статус код 200
                .extract()
                .response();
    }
}














