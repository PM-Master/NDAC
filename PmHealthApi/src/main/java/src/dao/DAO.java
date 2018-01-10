package src.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import src.model.*;
import src.model.Record;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    public static final String PM_HEALTH_DB = "pm_health";

    private static DAO dao;
    public static DAO getDao() throws PropertyVetoException, ClassNotFoundException, SQLException {
        if(dao == null) {
            dao = new DAO();
        }
        return dao;
    }

    public DAO() throws PropertyVetoException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        getConnection();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:5050/pm_health", "root", "root");
    }

    private ResultSet executeQuery(String sessionId, String sql) throws SQLException {
        String username = getSessionUsername(sessionId);

        Statement stmt = getConnection().createStatement();

        return stmt.executeQuery("/*user=" + username + "*/ " + sql);
    }

    public List<String> getLinks(String sessionId) throws SQLException {
        String username = getSessionUsername(sessionId);

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        ResultSet resultSet = stmt.executeQuery("/*user=" + username + "*/ " + "select links.name from links");

        List<String> links = new ArrayList<>();
        while(resultSet.next()) {
            links.add(resultSet.getString(1));
        }

        conn.close();

        return links;
    }

    public void getUsers() {}

    public User getUser(String username) throws SQLException {
        String sql = "select user_id, username, password from users where username='" + username + "'";

        Connection connection = getConnection();
        Statement stmt = getConnection().createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        User user = null;
        if(resultSet.next()) {
            user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
        }
        connection.close();

        return user;
    }

    public void createSession(int userId, String sessionId) throws SQLException {
        String sql = "insert into sessions (user_id, session_id) values (" + userId + ", '" + sessionId + "')";

        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute(sql);

        connection.close();
    }

    public void deleteSession(String sessionId) {
    }

    public String getSessionUsername(String sessionId) throws SQLException {
        String sql = "select username from users join sessions on users.user_id = sessions.user_id where sessions.session_id = '" + sessionId + "'";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        resultSet.next();
        String sessionUserName = resultSet.getString(1);

        conn.close();

        return sessionUserName;
    }

    public List<Record> getRecords(String sessionId) throws SQLException {
        String sql = "select patient_info.patient_id, patient_info.name, patient_info.dob, patient_info.gender, patient_info.ssn, " +
                "patient_info.race, patient_info.marital_status, patient_info.cell_phone, patient_info.work_phone, " +
                "patient_info.home_phone, patient_info.email, patient_info.address from patient_info";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        String username = getSessionUsername(sessionId);
        ResultSet resultSet = stmt.executeQuery("/*user=" + username + "*/ " + sql);

        List<Record> records = new ArrayList<>();
        while(resultSet.next()) {
            records.add(new Record(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
                    resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
                    resultSet.getString(10), resultSet.getString(11), resultSet.getString(12)));
        }

        conn.close();

        return records;
    }

    public Record getRecord(int patientId, String sessionId) throws SQLException {
        String sql = "select patient_info.patient_id, patient_info.name, patient_info.dob, patient_info.gender, patient_info.ssn, " +
                "patient_info.race, patient_info.marital_status, patient_info.cell_phone, patient_info.work_phone, " +
                "patient_info.home_phone, patient_info.email, patient_info.address from patient_info where patient_id = " + patientId;

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        String username = getSessionUsername(sessionId);
        ResultSet resultSet = stmt.executeQuery("/*user=" + username + "*/ " + sql);

        resultSet.next();

        Record record = new Record(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
                resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
                resultSet.getString(10), resultSet.getString(11), resultSet.getString(12));

        conn.close();

        return record;
    }

    public Vitals getLastVitals(int patientId, String sessionId) throws SQLException {
        String sql = "select max(visits.visit_id), visits.admission_date, height, weight, temperature, pulse, blood_pressure from vitals join visits on visits.visit_id = vitals.visit_id where visits.patient_id = " + patientId;

        ResultSet resultSet = executeQuery(sessionId, sql);
        resultSet.next();

        return new Vitals(resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4),
                resultSet.getDouble(5), resultSet.getInt(6), resultSet.getString(7));
    }

    public List<Visit> getVisits(int patientId, String sessionId) throws SQLException {
        String sql = "select visits.visit_id, visits.patient_id, visits.admission_date, visits.discharge_date, " +
                "visits.reason, visits.result, diagnoses.diagnosis, treatments.treatment, vitals.height, vitals.weight, " +
                "vitals.temperature, vitals.pulse, vitals.blood_pressure, visit_notes.note, prescriptions.medicine, prescriptions.dosage, prescriptions.duration " +
                "from visits " +
                "left outer join diagnoses on visits.visit_id = diagnoses.visit_id " +
                "left outer join treatments on visits.visit_id = treatments.visit_id " +
                "left outer join vitals on visits.visit_id = vitals.visit_id " +
                "left outer join visit_notes on visits.visit_id = visit_notes.visit_id " +
                "left outer join prescriptions on visits.visit_id = prescriptions.visit_id " +
                "where visits.patient_id = " + patientId;

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        String username = getSessionUsername(sessionId);
        ResultSet resultSet = stmt.executeQuery("/*user=" + username + "*/ " + sql);

        List<Visit> visits = new ArrayList<>();
        while(resultSet.next()) {
            visits.add(new Visit(resultSet.getInt(1), resultSet.getInt(2),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6),
                    resultSet.getString(14), resultSet.getString(7),
                    resultSet.getString(8),
                    new Vitals(
                            resultSet.getString(3),
                            resultSet.getInt(9),
                            resultSet.getInt(10),
                            resultSet.getDouble(11),
                            resultSet.getInt(12),
                            resultSet.getString(13)
                    ),
                    new Prescription(
                            resultSet.getString(15),
                            resultSet.getString(16),
                            resultSet.getString(17)
                    ))
            );
        }

        conn.close();

        return visits;
    }

    public void updateRecord(int patientId, Record data, String sessionId) throws SQLException {
        String sql = "update patient_info set ";
        String sets = "";
        if(data.getName() != null) {
            sets += "name='" + data.getName() + "'";
        }
        if(data.getDob() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "dob='" + data.getDob() + "'";
        }
        if(data.getSsn() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "ssn='" + data.getSsn() + "'";
        }
        if(data.getGender() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "gender='" + data.getGender() + "'";
        }
        if(data.getRace() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "race='" + data.getRace() + "'";
        }
        if(data.getMaritalStatus() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "maritalStatus='" + data.getMaritalStatus() + "'";
        }
        if(data.getCellPhone() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "cellPhone='" + data.getCellPhone() + "'";
        }
        if(data.getWorkPhone() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "workPhone='" + data.getWorkPhone() + "'";
        }
        if(data.getHomePhone() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "homePhone='" + data.getHomePhone() + "'";
        }
        if(data.getEmail() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "email='" + data.getEmail() + "'";
        }
        if(data.getAddress() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "address='" + data.getAddress() + "'";
        }

        sql += sets + " where patient_id=" + patientId;

        System.out.println(sql);

        Statement stmt = getConnection().createStatement();
        stmt.executeUpdate(sql);
    }

    public Visit updateVisit(int patientId, int visitId, Visit data, String sessionId) throws SQLException {
        String sql = "update visits set ";
        String sets = "";
        if(data.getAdmissionDate() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "admission_date=now()";
        }
        if(data.getDischargeDate() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "discharge_date=now()";
        }
        if(data.getReason() != null) {
            sets += (sets.isEmpty() ? "" : ", ") + "reason='" + data.getReason() + "'";
        }

        if(!sets.isEmpty()) {
            sql += sets + " where visit_id=" + visitId;
            Statement stmt = getConnection().createStatement();
            System.out.println(sql);
            stmt.executeUpdate(sql);
        }

        //vitals
        if(data.getVitals() != null) {
            sql = "update vitals set ";
            sets += "";
            if (data.getVitals().getHeight() > 0) {
                sets += (sets.isEmpty() ? "" : ", ") + "height=" + data.getVitals().getHeight();
            }
            if (data.getVitals().getWeight() > 0) {
                sets += (sets.isEmpty() ? "" : ", ") + "weight=" + data.getVitals().getWeight();
            }
            if (data.getVitals().getTemperature() > 0) {
                sets += (sets.isEmpty() ? "" : ", ") + "temperature=" + data.getVitals().getTemperature();
            }
            if (data.getVitals().getPulse() > 0) {
                sets += (sets.isEmpty() ? "" : ", ") + "pulse=" + data.getVitals().getPulse();
            }
            if (data.getVitals().getBloodPressure() != null) {
                sets += (sets.isEmpty() ? "" : ", ") + "blood_pressure='" + data.getVitals().getBloodPressure() + "'";
            }

            if(!sets.isEmpty()) {
                sql += sets + " where visit_id = " + visitId;
                Statement stmt = getConnection().createStatement();
                System.out.println(sql);
                stmt.executeUpdate(sql);
            }
        }

        //diagnoses
        if(data.getDiagnoses() != null) {
            sql = "update diagnoses set diagnoses='" + data.getDiagnoses() + "'";

            sql += " where visit_id = " + visitId;
            Statement stmt = getConnection().createStatement();
            System.out.println(sql);
            stmt.executeUpdate(sql);
        }

        //treatments
        if(data.getTreatments() != null) {
            sql = "update treatments set treatment='" + data.getTreatments() + "'";

            sql += " where visit_id = " + visitId;
            Statement stmt = getConnection().createStatement();
            System.out.println(sql);
            stmt.executeUpdate(sql);
        }

        //notes
        /*if(data.getNotes() != null) {
            List<String> notes = data.getNotes();
            for(String note : notes) {
                sql = "update treatments set treatment='" + data.getTreatments() + "'";

                sql += " where visit_id = " + visitId;
                Statement stmt = getConnection().createStatement();
                System.out.println(sql);
                stmt.execute(sql);
            }
        }*/

        if(data.getPrescription() != null) {
            sql = "update prescriptions set ";
            sets += "";
            if (data.getVitals().getBloodPressure() != null) {
                sets += (sets.isEmpty() ? "" : ", ") + "name='" + data.getVitals().getBloodPressure() + "'";
            }
            if (data.getVitals().getBloodPressure() != null) {
                sets += (sets.isEmpty() ? "" : ", ") + "duration='" + data.getVitals().getBloodPressure() + "'";
            }

            if(!sets.isEmpty()) {
                sql += sets + " where visit_id = " + visitId;
                Statement stmt = getConnection().createStatement();
                System.out.println(sql);
                stmt.executeUpdate(sql);
            }
        }

        return getVisit(visitId, sessionId);
    }

    private Visit getVisit(int visitId, String sessionId) throws SQLException {
        String sql = "select visits.visit_id, visits.patient_id, admission_date, discharge_date, reason, result, diagnoses, treatment, height, weight, temperature, pulse, blood_pressure, notes, medicines.name, medicines.dosage, prescriptions.duration from visits " +
                "left outer join diagnoses on visits.visit_id = diagnoses.visit_id " +
                "left outer join treatments on visits.visit_id = treatments.visit_id " +
                "left outer join vitals on visits.visit_id = vitals.visit_id " +
                "left outer join visit_notes on visits.visit_id = visit_notes.visit_id " +
                "left outer join prescriptions on visits.visit_id = prescriptions.visit_id " +
                "left outer join medicines on medicines.med_id = prescriptions.medicine_id " +
                "where visits.visit_id = " + visitId;

        ResultSet resultSet = executeQuery(sessionId, sql);

        if (resultSet.next()) {

            return new Visit(resultSet.getInt(1), resultSet.getInt(2),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6),
                    resultSet.getString(14), resultSet.getString(7),
                    resultSet.getString(8),
                    new Vitals(
                            resultSet.getString(3),
                            resultSet.getInt(10),
                            resultSet.getInt(11),
                            resultSet.getDouble(12),
                            resultSet.getInt(13),
                            resultSet.getString(14)
                    ),
                    new Prescription(
                            resultSet.getString(15),
                            resultSet.getString(16),
                            resultSet.getString(17)
                    )
            );
        } else {
            return null;
        }
    }

    private Prescription getPrescription(int visitId)  {
        try {
            String sql = "select name, dosage, duration from prescriptions join medicines on medicines.med_id = prescriptions.medicine_id where visit_id = " + visitId;
            Statement stmt = getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            if(resultSet.next()) {
                return new Prescription(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Patient getSession(String sessionId) throws SQLException {
        String sql = "SELECT sessions.user_id, patient_info.patient_id FROM sessions join patient_info on patient_info.user_id = sessions.user_id where session_id = '" + sessionId + "';";
        Statement stmt = getConnection().createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        resultSet.next();
        int userId = resultSet.getInt(1);
        int patientId = resultSet.getInt(2);

        return new Patient(userId, patientId);
    }

    public Visit createVisit(int patientId, String sessionId) throws SQLException {
        String sql = "insert into visits (patient_id, admission_date) values(" + patientId + ", now())";
        Statement stmt = getConnection().createStatement();
        stmt.execute(sql);

        sql = "select max(visit_id), admission_date from visits";
        stmt = getConnection().createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        resultSet.next();
        int visitId = resultSet.getInt(1);
        String admDate = resultSet.getString(2);

        Visit visit = new Visit();
        visit.setVisitId(visitId);
        visit.setAdmissionDate(admDate);
        return visit;
    }

    public List<Medicine> getAllMeds() throws SQLException {
        String sql = "select med_id, name, dosage from medicines";
        Statement stmt = getConnection().createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        List<Medicine> meds = new ArrayList<>();
        while(resultSet.next()) {
            meds.add(new Medicine(resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3)));
        }

        return meds;
    }

    public String getMedicines(int patientId, String sessionId) {
        return null;
    }
}
