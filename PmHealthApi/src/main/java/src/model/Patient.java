package src.model;

public class Patient {
    int userId;
    int patientId;

    public Patient() {

    }

    public Patient(int userId, int patientId) {
        this.userId = userId;
        this.patientId = patientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
