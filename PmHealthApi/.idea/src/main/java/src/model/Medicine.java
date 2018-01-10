package src.model;

import java.io.Serializable;

public class Medicine implements Serializable {
    int    medId;
    String name;
    String dosage;

    public Medicine() {

    }

    public Medicine(int medId, String name, String dosage) {
        this.medId = medId;
        this.name = name;
        this.dosage = dosage;
    }

    public int getMedId() {
        return medId;
    }

    public void setMedId(int medId) {
        this.medId = medId;
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
}
