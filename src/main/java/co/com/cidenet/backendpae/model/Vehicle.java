package co.com.cidenet.backendpae.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicles")
public class Vehicle {

    @Id
    String id;

    @Indexed(unique = true)
    String numberplate;

    String type;
    int totalseats;

    public Vehicle() { }

    public Vehicle(String numberplate, String type, int totalseats) {
        this.numberplate = numberplate;
        this.type = type;
        this.totalseats = totalseats;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getTotalseats() {
        return totalseats;
    }


}
