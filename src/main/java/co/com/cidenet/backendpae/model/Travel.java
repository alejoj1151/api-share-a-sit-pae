package co.com.cidenet.backendpae.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.util.List;

@Document(collection = "travels")
public class Travel {

    @Id
    private String id;

    @Indexed(unique = true)
    private Vehicle vehicle;

    @DBRef
    List<User> passengers;

    @DBRef
    private User driver;

    int available_seats;

    private String origen;
    private String destination;

    private String hour;
    private String date;

    public Travel() { }

    public Travel(Vehicle vehicle, int available_seats) {
        this.vehicle = vehicle;
        this.available_seats = available_seats;
        this.origen = null;
        this.destination = null;
        this.hour = null;
        this.date = null;
    }

    public Travel(Vehicle vehicle, List<User> passengers, User driver, int available_seats, String origen, String destination, String hour, String date) {
        this.vehicle = vehicle;
        this.passengers = passengers;
        this.driver = driver;
        this.available_seats = available_seats;
        this.origen = origen;
        this.destination = destination;
        this.hour = hour;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<User> passengers) {
        this.passengers = passengers;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public int getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(int available_seats) {
        this.available_seats = available_seats;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
