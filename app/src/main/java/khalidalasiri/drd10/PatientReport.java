package khalidalasiri.drd10;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PatientReport extends AppCompatActivity {

    String userID;
    String patientID;
    String doctorID;

    private List<HttpCookie> token;
    String connection_url;
    String message[];
    String tableURL;
    Context context;

    Button btHome;
    Button btSubmit;
    EditText etBP;
    EditText etBG;
    EditText etHR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_report);

        context = this;
        // get the User ID from Extra
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
        // get the Patient ID from Extra
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

        // get the Doctor ID for this Patient
        doctorID = getDoctorID(patientID);
        if(doctorID != null)
        {
            Toast.makeText(getApplicationContext(),doctorID,Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.error_noDoctorSelected,Toast.LENGTH_LONG).show();
        }

        // initial the UI elements
        btHome = findViewById(R.id.btHome);
        btSubmit = findViewById(R.id.btSubmit);
        etBP = findViewById(R.id.etBP);
        etBG = findViewById(R.id.etBG);
        etHR = findViewById(R.id.etHR);

        // onClick
        btHome.setOnClickListener(onClickListener);
        btSubmit.setOnClickListener(onClickListener);


    }

    private String getDoctorID(String patientID) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();

            if (ni != null && ni.isConnected()) {
                try {
                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    ConnectionToken connectionToken = new ConnectionToken(context);
                    token = connectionToken.execute(connection_url).get();
                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Specialist";

                    GetAsync getAsync = new GetAsync(token,context,tableURL,"Specialist");
                    message = getAsync.execute(patientID,"1","PatientID").get();

                    if(message == null)
                        Toast.makeText(getApplicationContext(), R.string.error_getValue, Toast.LENGTH_SHORT).show();
                    else
                        return message[1];
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_noInternet), Toast.LENGTH_LONG).show();
            }
        }

        return null;
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btHome:
                    Intent home = new Intent(PatientReport.this, PatientDashboard.class);
                    home.putExtra("userID", userID);
                    home.putExtra("PatientID",patientID);
                    startActivity(home);
                    break;
                case R.id.btSubmit:
                    if(doctorID != null)
                    {
                        Toast.makeText(getApplicationContext(),doctorID,Toast.LENGTH_LONG).show();
                        // TODO: 4/3/2018 Submit the Data

                        Intent myDoctor = new Intent(PatientReport.this, PatientDashboard.class);
                        myDoctor.putExtra("userID", userID);
                        myDoctor.putExtra("PatientID",patientID);
                        startActivity(myDoctor);
                        break;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.error_noDoctorSelected,Toast.LENGTH_LONG).show();
                    }

            }

        }
    };
}
