package khalidalasiri.drd10;

public class Report {

    // class to store the data from Report table and handel it for the ReportProvider
    // "ReportID","ReportDate","BloodPressure","BloodGlucoseAnalysis","HeartRate"
    private String ReportID;
    private String ReportDate;
    private String BloodPressure;
    private String BloodGlucoseAnalysis;
    private String HeartRate;

    public Report(String reportID, String reportDate, String bloodPressure, String bloodGlucoseAnalysis, String heartRate) {
        ReportID = reportID;
        ReportDate = reportDate;
        BloodPressure = bloodPressure;
        BloodGlucoseAnalysis = bloodGlucoseAnalysis;
        HeartRate = heartRate;
    }

    public String getReportID() {
        return ReportID;
    }

    public void setReportID(String reportID) {
        ReportID = reportID;
    }

    public String getReportDate() {
        return ReportDate;
    }

    public void setReportDate(String reportDate) {
        ReportDate = reportDate;
    }

    public String getBloodPressure() {
        return BloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        BloodPressure = bloodPressure;
    }

    public String getBloodGlucoseAnalysis() {
        return BloodGlucoseAnalysis;
    }

    public void setBloodGlucoseAnalysis(String bloodGlucoseAnalysis) {
        BloodGlucoseAnalysis = bloodGlucoseAnalysis;
    }

    public String getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(String heartRate) {
        HeartRate = heartRate;
    }
}
