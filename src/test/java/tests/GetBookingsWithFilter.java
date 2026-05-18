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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testFilterByFirstName() throws Exception {
        Response response = apiClient.getBookingsWithFilter("");
        //Создаем бронирования с разными именами
        createBooking("John", "Inx", "2024-01-01", "2024-01-05");
        createBooking("Jane", "Bush", "2024-02-10", "2024-02-15");
        createBooking("Stive", "Doe", "2024-03-01", "2024-03-05");
        createBooking("Stanley", "Johnson", "2024-04-01", "2024-04-05");

        // Проверка фильтров по firstName
        Response responseJohn = apiClient.getBookingsWithFilter("?firstname=John");
        assertThat(responseJohn.getStatusCode()).isEqualTo(200);
        assertThat(responseJohn.jsonPath().getList("bookingid")).hasSize(1);

        Response responseJane = apiClient.getBookingsWithFilter("?firstname=Jane");
        assertThat(responseJane.getStatusCode()).isEqualTo(200);
        assertThat(responseJane.jsonPath().getList("bookingid")).hasSize(1);

        Response responseStive = apiClient.getBookingsWithFilter("?firstname=Stive");
        assertThat(responseStive.getStatusCode()).isEqualTo(200);
        assertThat(responseStive.jsonPath().getList("bookingid")).hasSize(1);

        Response responseStanley = apiClient.getBookingsWithFilter("?firstname=Stanley");
        assertThat(responseStanley.getStatusCode()).isEqualTo(200);
        assertThat(responseStanley.jsonPath().getList("bookingid")).hasSize(1);

        // Проверка фильтров по lastName
        Response responseInx = apiClient.getBookingsWithFilter("?lastname=Inx");
        assertThat(responseInx.getStatusCode()).isEqualTo(200);
        assertThat(responseInx.jsonPath().getList("bookingid")).hasSize(1);

        Response responseBush = apiClient.getBookingsWithFilter("?lastname=Bush");
        assertThat(responseBush.getStatusCode()).isEqualTo(200);
        assertThat(responseBush.jsonPath().getList("bookingid")).hasSize(1);

        Response responseDoe = apiClient.getBookingsWithFilter("?lastname=Doe");
        assertThat(responseDoe.getStatusCode()).isEqualTo(200);
        assertThat(responseDoe.jsonPath().getList("bookingid")).hasSize(1);

        Response responseJohnson = apiClient.getBookingsWithFilter("?lastname=Johnson");
        assertThat(responseJohnson.getStatusCode()).isEqualTo(200);
        assertThat(responseJohnson.jsonPath().getList("bookingid")).hasSize(1);

        // Проверка фильтров по checkin
        Response responseCheckin1 = apiClient.getBookingsWithFilter("?checkin=2024-01-01");
        assertThat(responseCheckin1.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckin1.jsonPath().getList("bookingid")).hasSize(1);

        Response responseCheckin2 = apiClient.getBookingsWithFilter("?checkin=2024-02-10");
        assertThat(responseCheckin2.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckin2.jsonPath().getList("bookingid")).hasSize(1);

        Response responseCheckin3 = apiClient.getBookingsWithFilter("?checkin=2024-03-01");
        assertThat(responseCheckin3.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckin3.jsonPath().getList("bookingid")).hasSize(1);

        Response responseCheckin4 = apiClient.getBookingsWithFilter("?checkin=2024-04-01");
        assertThat(responseCheckin4.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckin4.jsonPath().getList("bookingid")).hasSize(1);


        // Проверка фильтров по checkout
        Response responseCheckout1 = apiClient.getBookingsWithFilter("?checkout=2024-01-05");
        assertThat(responseCheckout1.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckout1.jsonPath().getList("bookingid")).hasSize(1);

        Response responseCheckout2 = apiClient.getBookingsWithFilter("?checkout=2024-02-15");
        assertThat(responseCheckout2.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckout2.jsonPath().getList("bookingid")).hasSize(1);

        Response responseCheckout3 = apiClient.getBookingsWithFilter("?checkout=2024-03-05");
        assertThat(responseCheckout3.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckout3.jsonPath().getList("bookingid")).hasSize(1);

        Response responseCheckout4 = apiClient.getBookingsWithFilter("?checkout=2024-04-05");
        assertThat(responseCheckout4.getStatusCode()).isEqualTo(200);
        assertThat(responseCheckout4.jsonPath().getList("bookingid")).hasSize(1);

    }
}