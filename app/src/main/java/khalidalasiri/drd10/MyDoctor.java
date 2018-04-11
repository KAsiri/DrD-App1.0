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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyDoctor extends AppCompatActivity {

    String userID;
    String patientID;
    String doctorID;

    private List<HttpCookie> token;
    String connection_url;
    String message[];
    String tableURL;
    Context context;

    Button btHome;
    Button btSearch;
    Button btSelect;
    Button btChange;
    EditText etCode;

    TextView tvDoctorName;
    TextView tvHospitalName;
    TextView tvDoctorDegree;
    TextView tvDoctorRank;

    LinearLayout section1;
    LinearLayout section2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);

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

        // initial the UI elements
        btHome = findViewById(R.id.btHome);
        btSearch =findViewById(R.id.btSearch);
        btSelect = findViewById(R.id.btSelect);
        btChange = findViewById(R.id.btChange);
        section1 = findViewById(R.id.section1);
        section2 = findViewById(R.id.section2);
        etCode = findViewById(R.id.etCode);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvHospitalName = findViewById(R.id.tvHospitalName);
        tvDoctorDegree = findViewById(R.id.tvDoctorDegree);
        tvDoctorRank = findViewById(R.id.tvDoctorRank);


        // get the Doctor ID for this Patient
        doctorID = getDoctorID(patientID);
        if (doctorID != null) {
            section1.setVisibility(View.GONE);
            section2.setVisibility(View.VISIBLE);
            btSelect.setVisibility(View.GONE);
            getDoctorInfo(doctorID);
        } else {
            btChange.setVisibility(View.GONE);
        }
        btSearch.setOnClickListener(onClickListener);
        btSelect.setOnClickListener(onClickListener);
        btChange.setOnClickListener(onClickListener);

        // onClick
        btHome.setOnClickListener(onClickListener);
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

                    GetAsync getAsync = new GetAsync(token, context, tableURL, "Specialist");
                    message = getAsync.execute(patientID, "1", "PatientID").get();

                    if (message == null)
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
                    Intent home = new Intent(MyDoctor.this, PatientDashboard.class);
                    home.putExtra("userID", userID);
                    home.putExtra("PatientID",patientID);
                    startActivity(home);
                    finish();
                    break;
                case R.id.btSearch:
                    if(!etCode.getText().toString().isEmpty())
                    {
                        getDoctorInfo(etCode.getText().toString());
                        btSelect.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.enterDoctorCode,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btSelect:
                    if (doctorID != null) {
                        if (!etCode.getText().toString().isEmpty()) {
                            Log.d("UpdateDoctorID",etCode.getText().toString());
                            setMyDoctor(etCode.getText().toString(), "PUT");
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.enterDoctorCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        setMyDoctor(message[1], "POST");
                    }
                    section1.setVisibility(View.GONE);
                    btSelect.setVisibility(View.GONE);
                    btChange.setVisibility(View.VISIBLE);
                    break;
                case R.id.btChange:
                    section1.setVisibility(View.VISIBLE);
                    btChange.setVisibility(View.GONE);
                    section2.setVisibility(View.GONE);
                    break;


            }

        }
    };

    private void getDoctorInfo(final String doctorID) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();

            if (ni != null && ni.isConnected()) {
                try {
                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    ConnectionToken connectionToken = new ConnectionToken(context);
                    token = connectionToken.execute(connection_url).get();

                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Doctor";

                    DoctorInfoAsync doctorInfoAsync = new DoctorInfoAsync(token, context, tableURL, "Doctor");
                    message = doctorInfoAsync.execute(doctorID, "1", "DoctorID").get();

                    if(message == null)
                    {
                        Toast.makeText(getApplicationContext(), R.string.error_doctorNotFound,Toast.LENGTH_SHORT).show();
                        tvDoctorName.setText("");
                        tvHospitalName.setText("");
                        tvDoctorDegree.setText("");
                        tvDoctorRank.setText("");
                    }
                    else
                    {
                        section2.setVisibility(View.VISIBLE);
                        tvDoctorName.setText(message[6]+" "+message[7]);
                        tvHospitalName.setText(message[8]);
                        tvDoctorDegree.setText(message[4]);
                        tvDoctorRank.setText(message[3]);

                    }
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

    private void setMyDoctor(String doctorID, String method) {
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

                    // encode the data into json for User_Information
                    JSONObject jsonData = new JSONObject();
                    try {
                        jsonData.put("PatientID", patientID);
                        jsonData.put("DoctorID", doctorID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // TODO: 4/12/2018
                    // fix the filter on url problem
//                    if(method.equals("PUT"))
//                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Specialist"+"/"+patientID;
//                    else
                        tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Specialist";
                    //Post the Data
                    DataPoster dataPoster = new DataPoster(tableURL, method, token, context);
                    String respond = dataPoster.execute(jsonData.toString()).get();


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

}
