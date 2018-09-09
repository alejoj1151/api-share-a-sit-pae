package co.com.cidenet.backendpae.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicles")
public class Vehicle {

    @Id
    String id;

    String numberplate;
    String type;
    int total_places;

    public Vehicle() { }

    public Vehicle(String numberplate, String type, int total_places) {
        this.numberplate = numberplate;
        this.type = type;
        this.total_places = total_places;
    }

    public String getId() {
        return id;
    }

    public String getNumberplate() {
        return numberplate;
    }

    public String getType() {
        return type;
    }

    public int getTotal_places() {
        return total_places;
    }


}
