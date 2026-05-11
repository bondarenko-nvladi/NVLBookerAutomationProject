package core.clients;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class APIClient {

    private final String baseUrl;
    private String token;

    public APIClient() {
        this.baseUrl = determineBaseUrl();
    }

    //Геттер для baseUrl
    public String getBaseUrl() {
        return baseUrl;
    }

    public String getToken() {
        return token;
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
                .header("Accept", "application/json")
                .filter(addAuthTokenFilter());
    }

    //Метод получения токена
    public void createToken(String username, String password) {
        //Тело запроса для получения токена
        String requestBody = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);

        Response response = getRequestSpec()
                .body(requestBody)
                .when()
                .post(ApiEndpoints.AUTH.getPath()) //Используем Enum для эндпойнта /auth
                .then()
                .statusCode(200) // Ожидаемый статус код 200 ОК
                .extract()
                .response();

        //Извлекаем токен из ответа
        token = response.jsonPath().getString("token");
    }

    //Фильтр для добавление токена в заголовок Autorization
    private Filter addAuthTokenFilter() {
        return (FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) -> {
            if (token !=null) {
                requestSpec.header("Cookie", "token=" + token);
            }
            return ctx.next(requestSpec, responseSpec);
        };
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
                .extract()
                .response();
    }

    //Delete запрос на эндпоинт/booking
    public Response deleteBooking (int bookingId) {
        return getRequestSpec()
                .pathParam("id", bookingId) // указываем path parameter для id
                .when()
                .delete(ApiEndpoints.BOOKING.getPath() + "/{id}") //Используем параметр пути в запросе
                .then()
                .log().all()
                .statusCode(201) //Ожидаемый статус код 201
                .extract()
                .response();
    }
}














