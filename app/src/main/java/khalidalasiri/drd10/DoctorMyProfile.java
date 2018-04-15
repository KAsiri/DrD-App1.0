package khalidalasiri.drd10;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DoctorMyProfile extends AppCompatActivity {

    //EditText
    EditText fristName;
    EditText middleName;
    EditText lastName;
    EditText etDOB;
    EditText etEmail;
    EditText etPhone;
    EditText etID;
    EditText etNeighborhood;
    EditText etStreet;
    EditText etBuilding;
    EditText etPostal;
    EditText etAddition;
    EditText etDoctorDegree;
    EditText etDoctorRank;
    EditText etIDtype;
    EditText etCountry ;
    EditText etCity ;

    Button btHome;


    // for connection
    private String connection_url;
    private List<HttpCookie> token;
    Context context;
    String[] message;
    String tableURL;
    String userID;

    // Array Adapter
    ArrayAdapter idTypesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_my_profile);
        // Set all the UI elements
        setElements();
        // initializing the variables
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
        getUserInfo(userID);

        btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(onClickListener);
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btHome:
                    Intent home = new Intent(DoctorMyProfile.this, DoctorDashboard.class);
                    home.putExtra("userID", userID);
                    startActivity(home);
                    finish();
                    break;
            }

        }
    };

    private void getUserInfo(String userID) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected()) {
                try {
                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    ConnectionToken connectionToken = new ConnectionToken(context);
                    token = connectionToken.execute(connection_url).get();

                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/User_Information";

                    DoctorProfileAsync doctorProfileAsync = new DoctorProfileAsync(token,context,tableURL,"User_Information");
                    message = doctorProfileAsync.execute(userID,"0","ID").get();   // 0 is the index of the ID on the table on database

                    if(message != null)
                    {
                        fristName.setText(message[2]);
                        middleName.setText(message[3]);
                        lastName.setText(message[4]);
                        etEmail.setText(message[5]);
                        etPhone.setText(message[6]);
                        etIDtype.setText(message[15]);
                        etID.setText(message[16]);
                        etCountry.setText(message[8]);
                        etCity.setText(message[9]);
                        etNeighborhood.setText(message[10]);
                        etStreet.setText(message[11]);
                        etBuilding.setText(message[12]);
                        etPostal.setText(message[13]);
                        etAddition.setText(message[14]);
                        etDoctorDegree.setText(message[18]);
                        etDoctorRank.setText(message[17]);
                    }
                    else
                        Toast.makeText(getApplicationContext(),R.string.error_getValue,Toast.LENGTH_SHORT).show();

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

    // Set the Spinners used on the UI
    private void setElements() {
        // Set EditText
        fristName = findViewById(R.id.fristname);
        middleName = findViewById(R.id.middlename);
        lastName = findViewById(R.id.lastname);
        etPhone = findViewById(R.id.etPhone);
        etID = findViewById(R.id.etID);
        etNeighborhood = findViewById(R.id.etNeighborhood);
        etStreet = findViewById(R.id.etStreet);
        etBuilding = findViewById(R.id.etBuilding);
        etPostal = findViewById(R.id.etPostal);
        etAddition = findViewById(R.id.etAddition);
        etDoctorDegree = findViewById(R.id.etDoctorDegree);
        etDoctorRank = findViewById(R.id.etDoctorRank);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        etIDtype = findViewById(R.id.etIDtype);
        etEmail = findViewById(R.id.etEmail);


    }
}
