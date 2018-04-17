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

public class NewRespond extends AppCompatActivity {

    String userID;
    String doctorID;
    String patientID;
    String reportID;

    // UI Components
    EditText etPID;
    EditText etPaitentName;
    EditText etDateTime;
    EditText etAge;
    EditText etWeight;
    EditText etHeight;
    EditText etSex;
    EditText etTypeOfBlood;
    EditText etTypeOfD;
    EditText etBP;
    EditText etBG;
    EditText etHR;
    EditText etRespond;
    Button btSubmit;
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
        setContentView(R.layout.activity_new_respond);

        context = this;
        // get the ID from Extra
        getIDfromExtra(savedInstanceState);

        etPID = findViewById(R.id.etPID);
        etPaitentName = findViewById(R.id.etPatientName);
        etDateTime = findViewById(R.id.etDateTime);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etSex = findViewById(R.id.etSex);
        etTypeOfBlood = findViewById(R.id.etTypeOfBlood);
        etTypeOfD = findViewById(R.id.etTypeOfD);
        etBP = findViewById(R.id.etBP);
        etBG = findViewById(R.id.etBG);
        etHR = findViewById(R.id.etHR);
        etRespond = findViewById(R.id.etRespond);
        btSubmit = findViewById(R.id.btSubmit);
        btHome = findViewById(R.id.btHome);

        etPID.setText(patientID);
        btHome.setOnClickListener(onClickListener);
        btSubmit.setOnClickListener(onClickListener);

        getPatientReport();
    }

    private void getPatientReport() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected()) {
                try {
                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    ConnectionToken connectionToken = new ConnectionToken(context);
                    token = connectionToken.execute(connection_url).get();

                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Patient";
                    String include = "&include=User_Information,Report";
                    PatientRespondInformationAsync respondInformationAsync
                            = new PatientRespondInformationAsync(token, context, tableURL, "Patient", include);
                    message = respondInformationAsync.execute(patientID, "Patient.PatientID", reportID, "Report.ReportID").get();   // 0 is the index of the ID on the table on database

                    if (message != null) {
                        etPaitentName.setText(message[8] + " " + message[9]);
                        etDateTime.setText(message[10]);
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy");
                        int today = Integer.parseInt(df.format(c));
                        String age = String.valueOf(today - Integer.parseInt(message[2].substring(0,4)));
                        Log.d("DOB",message[2].substring(0,4));
                        Log.d("this year",String.valueOf(today));
                        etAge.setText(age);
                        etWeight.setText(message[5]);
                        etHeight.setText(message[6]);
                        etSex.setText(message[3]);
                        etTypeOfBlood.setText(message[7]);
                        etTypeOfD.setText(message[4]);
                        etBP.setText(message[11]);
                        etBG.setText(message[12]);
                        etHR.setText(message[13]);
                    } else
                        Toast.makeText(getApplicationContext(), R.string.error_getValue, Toast.LENGTH_SHORT).show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_noInternet), Toast.LENGTH_LONG).show();
            }
        }
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btHome:
                    Intent home = new Intent(NewRespond.this, DoctorDashboard.class);
                    home.putExtra("userID", userID);
                    home.putExtra("DoctorID", doctorID);
                    startActivity(home);
                    finish();
                    break;
                case R.id.btSubmit:

                    if(!etRespond.getText().toString().isEmpty())
                    {
                        createTheRespond();
                    }
                    else
                        Toast.makeText(getApplicationContext(),R.string.error_requiredField,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void createTheRespond() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected()) {
                try {
                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    ConnectionToken connectionToken = new ConnectionToken(context);
                    token = connectionToken.execute(connection_url).get();

                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Respond";

                    // get Date and Time
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String today = df.format(c);

                    // encode the data into json for User_Information
                    JSONObject jsonData = new JSONObject();
                    try {
                        jsonData.put("ReportID", reportID);
                        jsonData.put("RespondDate", today);
                        jsonData.put("RespondContent", etRespond.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    DataPoster dataPoster = new DataPoster(tableURL,"POST",token,context);
                    String result = dataPoster.execute(jsonData.toString()).get();

                    Log.d("result",result);
                    if (!result.isEmpty())
                    {
                        Intent home = new Intent(NewRespond.this, DoctorDashboard.class);
                        home.putExtra("userID", userID);
                        home.putExtra("DoctorID", doctorID);
                        startActivity(home);
                        finish();
                        Toast.makeText(getApplicationContext(),R.string.dataSavedSuccessfully,Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),R.string.error_notSaved,Toast.LENGTH_SHORT).show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_noInternet), Toast.LENGTH_LONG).show();
            }
        }

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
