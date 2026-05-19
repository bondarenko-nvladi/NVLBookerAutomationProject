package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import io.restassured.internal.RestAssuredResponseOptionsGroovyImpl;
import io.restassured.response.Response;
import models.BookingDates;
import models.CreatedBooking;
import models.NewBooking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialUpdateBooking {
    private APIClient apiClient;      // ← Объявлена переменная
    private ObjectMapper objectMapper;// ← Объявлена переменная
    private CreatedBooking createdBooking; //храним созданное бронирование
    private NewBooking newBooking; //новый объект для создания бронирования

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");
    }

    @Test
    public void testUpdateBooking() throws Exception {
        //создаем объект букинг с необходимыми данными
        newBooking = new NewBooking();
        newBooking.setFirstname("John");
        newBooking.setLastname("Doe");
        newBooking.setTotalprice(150);
        newBooking.setDepositpaid(true);
        newBooking.setBookingdates(new BookingDates("2024-01-01", "2024-01-05"));
        newBooking.setAdditionalneeds("Breakfast");

        // Выполняем запрос к эндпоинту booking через ApiClient
        String createBody = objectMapper.writeValueAsString(newBooking);
        Response createResponse = apiClient.createBooking(createBody);

        //проверяем, что статус код ответа равен 200
        assertThat(createResponse.getStatusCode()).isEqualTo(200);
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        //Отправляем PATCH-запрос
        String patchBody = "{ \"firstname\": \"Stanly\" }";
        Response patchResponse = apiClient.partialUpdateBooking(bookingId, patchBody);
        assertThat(patchResponse.getStatusCode()).isEqualTo(200);

        //Десериализуем тело ответа в объект Booking
        NewBooking result = objectMapper.readValue(patchResponse.asString(), NewBooking.class);

        //Проверка обновленных полей
        assertThat(result.getFirstname()).isEqualTo("Stanly");

        //Удаляем созданное бронирование
        Response deleteResponse = apiClient.deleteBooking(bookingId);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(201);

        //Проверяем, что бронирование удалено
        Response verifyResponse = apiClient.getBookingById(bookingId);
        assertThat(verifyResponse.getStatusCode()).isEqualTo(404);
    }
}





