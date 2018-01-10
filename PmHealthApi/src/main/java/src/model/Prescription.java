package src.model;

import java.io.Serializable;

public class Prescription implements Serializable {
    String name;
    String dosage;
    String duration;

    public Prescription() {

    }

    public Prescription(String name, String dosage, String duration) {
        this.name = name;
        this.dosage = dosage;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
