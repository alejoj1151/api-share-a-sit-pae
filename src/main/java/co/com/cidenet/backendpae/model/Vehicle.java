package co.com.cidenet.backendpae.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicles")
public class Vehicle {

    @Id
    String id;

    String number_plate;
    String type;
    int total_places;

    public Vehicle() { }

    public Vehicle(String number_plate, String type, int total_places) {
        this.number_plate = number_plate;
        this.type = type;
        this.total_places = total_places;
    }

    public String getId() {
        return id;
    }

    public String getNumberplate() {
        return number_plate;
    }

    public String getType() {
        return type;
    }

    public int getTotal_places() {
        return total_places;
    }


}
