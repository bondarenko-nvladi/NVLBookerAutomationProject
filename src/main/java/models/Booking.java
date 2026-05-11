package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Booking {
    private int bookingid;
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private String additionalneeds;
    private BookingDates bookingdates;



        //Конструктор
        @JsonCreator
        public Booking(
                @JsonProperty("bookingid") int bookingid,
                @JsonProperty("firstname") String firstname,
                @JsonProperty("lastname") String lastname,
                @JsonProperty("totalprice") int totalprice,
                @JsonProperty("depositpaid") boolean depositpaid,
                @JsonProperty("additionalneeds") String additionalneeds,
                @JsonProperty("bookingdates") BookingDates bookingdates) {
            this.bookingid = bookingid;
            this.firstname = firstname;
            this.lastname = lastname;
            this.totalprice = totalprice;
            this.depositpaid = depositpaid;
            this.additionalneeds = additionalneeds;
            this.bookingdates = bookingdates;
        }

        // Геттеры
        public int getBookingid() {
            return bookingid;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public int getTotalprice() {
            return totalprice;
        }

        public boolean isDepositpaid() {
            return depositpaid;
        }

        public String getAdditionalneeds() {
            return additionalneeds;
        }

        public BookingDates getBookingdates() {
            return bookingdates;
        }


        // Сеттеры (нужны для создания/обновления объектов)
        public void setBookingid(int bookingid) {
            this.bookingid = bookingid;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public void setTotalprice(int totalprice) {
            this.totalprice = totalprice;
        }

        public void setDepositpaid(boolean depositpaid) {
            this.depositpaid = depositpaid;
        }

        public void setAdditionalneeds(String additionalneeds) {
            this.additionalneeds = additionalneeds;
        }

        public void setBookingdates(BookingDates bookingdates) {
            this.bookingdates = bookingdates;
        }
    }

