package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import io.restassured.response.Response;
import models.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest {

    private APIClient apiClient;      // ← Объявлена переменная
    private ObjectMapper objectMapper; // ← Объявлена переменная

    //Инициализация API клиента перед каждым тестом
    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBookingById() throws Exception {
        //Выполняем запрос к эндпойнту/booking через APIClient
        Response response = apiClient.getBooking();

        //Проверяем, что статус-код ответа равен 200
        assertThat(response.getStatusCode()).isEqualTo(200);

        //Десериализируем тело ответа в список объектов Booking
        String responseBody = response.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody, new TypeReference<List<Booking>>() {
        });

        //Проверяем, что тело ответа содержит oбъекты Booking
        assertThat(bookings).isNotEmpty(); //Проверяем, что список не пуст

        //Проверяем, что каждый объект Booking содержит валидное значение bookingid
        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);//bookingid должен быть больше 0
        }
    }

    //Тест на id=3
        @Test
        public void testGetBookingById() throws Exception {
            //Вызываем метод с ID = 111
            Response response = apiClient.getBookingById(111);
            //Проверяем, что статус-код ответа равен 200
            assertThat(response.getStatusCode()).isEqualTo(200);
            //Получаем тело ответа
            String responseBody = response.getBody().asString();
            Booking booking = objectMapper.readValue(responseBody, Booking.class);
            assertThat(booking.getFirstname()).isEqualTo("John");
            assertThat(booking.getLastname()).isEqualTo("Smith");
            assertThat(booking.getTotalprice()).isEqualTo(111);
            assertThat(booking.isDepositpaid()).isTrue();
            assertThat(booking.getAdditionalneeds()).isEqualTo("Breakfast");
            assertThat(booking.getBookingdates()).isNotNull();
            assertThat(booking.getBookingdates().getCheckin().toString()).isEqualTo("2018-01-01");
            assertThat(booking.getBookingdates().getCheckout().toString()).isEqualTo("2019-01-01");
        }
    }


















