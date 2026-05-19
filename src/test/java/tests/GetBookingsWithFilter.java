package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import io.restassured.internal.RestAssuredResponseOptionsGroovyImpl;
import io.restassured.response.Response;
import models.Booking;
import models.BookingDates;
import models.CreatedBooking;
import models.NewBooking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetBookingsWithFilter {
    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private List<Integer> createdBookingIds;
    private NewBooking newBooking;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");
        createdBookingIds = new ArrayList<>();
    }

    @AfterEach
    public void cleanup() {
        // Удаляем созданные бронирования
        for (Integer bookingId : createdBookingIds) {
            apiClient.deleteBooking(bookingId);
        }
        createdBookingIds.clear();
    }

    // Метод для создания бронирования
    private void createBooking(String firstname, String lastname, String checkin, String checkout) throws Exception {
        newBooking = new NewBooking();
        newBooking.setFirstname(firstname);
        newBooking.setLastname(lastname);
        newBooking.setTotalprice(100);
        newBooking.setDepositpaid(true);
        newBooking.setBookingdates(new BookingDates(checkin, checkout));
        newBooking.setAdditionalneeds("Breakfast");

        String requestBody = objectMapper.writeValueAsString(newBooking);

        // Объявляем переменную response
        Response response = apiClient.createBooking(requestBody);
        assertThat(response.getStatusCode()).isEqualTo(200);

        int bookingId = response.jsonPath().getInt("bookingid");
        createdBookingIds.add(bookingId);
    }

    @ParameterizedTest
    @CsvSource({
            "Oleg, Prime, 2026-01-01, 2026-01-05",
            "Olga, Straus, 2026-01-02, 2026-01-04"
    })
    public void testFilterByFirstName(String firstname, String lastname, String checkin, String checkout) throws Exception {
        // Создаем бронирование
        createBooking(firstname, lastname, checkin, checkout);

        // Проверка фильтров по firstName
        Response responseByFirstName = apiClient.getBookingsWithFilter("?firstname=" + firstname);
        assertThat(responseByFirstName.getStatusCode()).isEqualTo(200);
        List<Integer> bookingIdsByFirstName = responseByFirstName.jsonPath().getList("bookingid");
        assertThat(bookingIdsByFirstName).hasSize(1);

        // Дополнительная проверка: получаем бронирование и сверяем имя
        int bookingId = bookingIdsByFirstName.get(0);
        Response getBookingResponse = apiClient.getBookingById(bookingId);
        Booking booking = objectMapper.readValue(getBookingResponse.getBody().asString(), Booking.class);
        assertThat(booking.getFirstname()).isEqualTo(firstname);


        // Проверка фильтра по lastName
        Response responseByLastName = apiClient.getBookingsWithFilter("?lastname=" + lastname);
        assertThat(responseByLastName.getStatusCode()).isEqualTo(200);
        List<Integer> bookingIdsByLastName = responseByLastName.jsonPath().getList("bookingid");
        assertThat(bookingIdsByLastName).hasSize(1);

        // Дополнительная проверка: получаем бронирование и сверяем фамилию
        int bookingIdByLastName = bookingIdsByLastName.get(0);
        Response getBookingResponseByLastName = apiClient.getBookingById(bookingIdByLastName);
        Booking bookingByLastName = objectMapper.readValue(getBookingResponseByLastName.getBody().asString(), Booking.class);
        assertThat(bookingByLastName.getLastname()).isEqualTo(lastname);
    }
}