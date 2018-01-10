package src.model;

import java.io.Serializable;

public class Vitals implements Serializable{
    String date;
    int height;
    int weight;
    double temperature;
    int pulse;
    String bloodPressure;

    public Vitals() {

    }

    public Vitals(String date, int height, int weight, double temperature, int pulse, String bloodPressure) {
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.temperature = temperature;
        this.pulse = pulse;
        this.bloodPressure = bloodPressure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
}
