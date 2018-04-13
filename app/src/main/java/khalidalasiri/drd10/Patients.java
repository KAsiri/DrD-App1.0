package khalidalasiri.drd10;

public class Patients {

    // class to store the data from Report table and handel it for the ReportProvider
    // "PatientID","Name","Sex","TypeOfDiabetes"
    String PatientID;
    String PatientName;
    String Sex;
    String TypeOfDiabetes;

    public Patients(String patientID, String patientName, String sex, String typeOfDiabetes) {
        PatientID = patientID;
        PatientName = patientName;
        Sex = sex;
        TypeOfDiabetes = typeOfDiabetes;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getTypeOfDiabetes() {
        return TypeOfDiabetes;
    }

    public void setTypeOfDiabetes(String typeOfDiabetes) {
        TypeOfDiabetes = typeOfDiabetes;
    }
}
