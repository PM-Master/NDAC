package src.model;

import java.io.Serializable;

public class Record implements Serializable{
    int    patientId;
    String name;
    String dob;
    String gender;
    String ssn;
    String race;
    String maritalStatus;
    String cellPhone;
    String workPhone;
    String homePhone;
    String email;
    String address;

    public Record() {

    }

    public Record(int patientId, String name, String dob, String gender,
                  String ssn, String race, String maritalStatus, String cellPhone,
                  String workPhone, String homePhone, String email, String address) {
        this.patientId = patientId;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.ssn = ssn;
        this.race = race;
        this.maritalStatus = maritalStatus;
        this.cellPhone = cellPhone;
        this.workPhone = workPhone;
        this.homePhone = homePhone;
        this.email = email;
        this.address = address;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
