package khalidalasiri.drd10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class NewRespond extends AppCompatActivity {

    String userID;
    String doctorID;
    String patientID;
    String reportID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_respond);

        // get the ID from Extra
        getIDfromExtra(savedInstanceState);


    }

    private void getIDfromExtra(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userID = null;
            } else {
                userID = extras.getString("userID");
            }
        } else {
            userID = (String) savedInstanceState.getSerializable("userID");
        }


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                doctorID = null;
            } else {
                doctorID = extras.getString("DoctorID");
            }
        } else {
            doctorID = (String) savedInstanceState.getSerializable("DoctorID");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                patientID = null;
            } else {
                patientID = extras.getString("PatientID");
            }
        } else {
            patientID = (String) savedInstanceState.getSerializable("PatientID");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                reportID = null;
            } else {
                reportID = extras.getString("ReportID");
            }
        } else {
            reportID = (String) savedInstanceState.getSerializable("ReportID");
        }
    }
}
