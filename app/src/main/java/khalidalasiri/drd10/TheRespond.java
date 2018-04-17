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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TheRespond extends AppCompatActivity {

    String userID;
    String patientID;
    String reportID;

    // UI Components
    EditText etDateTime;
    EditText etBP;
    EditText etBG;
    EditText etHR;
    EditText etRDateTime;
    EditText etRespond;
    Button btHome;

    // for connection
    private String connection_url;
    private List<HttpCookie> token;
    Context context;
    String[] message;
    String tableURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_respond);
        context = this;
        getIDfromExtra(savedInstanceState);
        etDateTime = findViewById(R.id.etDateTime);
        etBP = findViewById(R.id.etBP);
        etBG = findViewById(R.id.etBG);
        etHR = findViewById(R.id.etHR);
        etRDateTime = findViewById(R.id.etRDateTime);
        etRespond = findViewById(R.id.etRespond);
        btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(onClickListener);

        getRespondData();
    }

    private void getRespondData() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();

            if (ni != null && ni.isConnected()) {
                try {
                    // make the Connection to API
                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    ConnectionToken connectionToken = new ConnectionToken(context);
                    token = connectionToken.execute(connection_url).get();

                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Report";
                    RespondAsync respondAsync = new RespondAsync(token,context,tableURL,"Report");
                    message = respondAsync.execute(reportID,"0","Report.ReportID").get();

                    if(message != null)
                    {
                        etDateTime.setText(message[1]);
                        etBP.setText(message[2]);
                        etBG.setText(message[3]);
                        etHR.setText(message[4]);

                        if (message[5] != null && message[6] != null)
                        {
                            etRDateTime.setText(message[5]);
                            etRespond.setText(message[6]);
                        }
                        else
                            etRespond.setText(R.string.doctorNotRespond);
                    }
                    else
                        Toast.makeText(getApplicationContext(),R.string.error_getValue,Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Show Error "No Internet Connection"
            Toast.makeText(getApplicationContext(), R.string.error_noInternet, Toast.LENGTH_LONG).show();
        }
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btHome:
                    Intent home = new Intent(TheRespond.this, History.class);
                    home.putExtra("userID", userID);
                    home.putExtra("PatientID", patientID);
                    startActivity(home);
                    finish();
                    break;
            }
        }
    };

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
