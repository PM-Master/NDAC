package src.model;

import java.io.Serializable;
import java.util.List;

public class Visit implements Serializable{
    int          visitId;
    int          patientId;
    String       admissionDate;
    String       dischargeDate;
    String       reason;
    String       result;
    String       notes;
    String       diagnoses;
    String       treatments;
    Vitals       vitals;
    Prescription prescription;

    public Visit() {

    }

    public Visit(int visitId, int patientId, String admissionDate,
                 String dischargeDate, String reason, String result,
                 String notes, String diagnoses, String treatments,
                 Vitals vitals, Prescription prescription) {
        this.visitId = visitId;
        this.patientId = patientId;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.reason = reason;
        this.result = result;
        this.notes = notes;
        this.diagnoses = diagnoses;
        this.treatments = treatments;
        this.vitals = vitals;
        this.prescription = prescription;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
    }

    public String getTreatments() {
        return treatments;
    }

    public void setTreatments(String treatments) {
        this.treatments = treatments;
    }

    public Vitals getVitals() {
        return vitals;
    }

    public void setVitals(Vitals vitals) {
        this.vitals = vitals;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
