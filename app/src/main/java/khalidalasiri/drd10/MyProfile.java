package khalidalasiri.drd10;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyProfile extends AppCompatActivity {

    //EditText
    EditText fristName;
    EditText middleName;
    EditText lastName;
    EditText etDOB;
    EditText etPhone;
    EditText etID;
    EditText etNeighborhood;
    EditText etStreet;
    EditText etBuilding;
    EditText etPostal;
    EditText etAddition;
    EditText etWeight;
    EditText etHeight;

    // Button
    Button btUpdate;

    //Spinners
    Spinner spinnerIdType;
    Spinner spinnerCountries;
    Spinner spinnerCities;
    Spinner spinnerSex;
    Spinner spinnerTypeOfD;
    Spinner spinnerTypeOfBlood;

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
        setContentView(R.layout.activity_my_profile);
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

    }

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

                    PatientInfoAsync patientInfoAsync = new PatientInfoAsync(token,context,tableURL,"User_Information");
                    message = patientInfoAsync.execute(userID,"0","ID").get();   // 0 is the index of the ID on the table on database

                    if(message != null)
                    {
                        fristName.setText(message[2]);
                        middleName.setText(message[3]);
                        lastName.setText(message[4]);
                        etDOB.setText(message[17]);
                        etPhone.setText(message[6]);
                        etID.setText(message[16]);
                        etNeighborhood.setText(message[10]);
                        etStreet.setText(message[11]);
                        etBuilding.setText(message[12]);
                        etPostal.setText(message[13]);
                        etAddition.setText(message[14]);
                        etWeight.setText(message[20]);
                        etHeight.setText(message[21]);

                        // TODO: 4/12/2018
                        // set the spinner value
                        Log.d("print",message[15]);
                        String sp = message[15];
                        Log.d("spinner",String.valueOf(idTypesAdapter.getPosition(message[15])));

                        spinnerIdType.setSelection(idTypesAdapter.getPosition(message[15]));
                        spinnerCountries.setPrompt(message[8]);
                        spinnerCities.setPrompt(message[9]);
                        spinnerSex.setPrompt(message[18]);
                        spinnerTypeOfBlood.setPrompt(message[22]);
                        spinnerTypeOfD.setPrompt(message[19]);
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
        etDOB = findViewById(R.id.etDOB);
        etPhone = findViewById(R.id.etPhone);
        etID = findViewById(R.id.etID);
        etNeighborhood = findViewById(R.id.etNeighborhood);
        etStreet = findViewById(R.id.etStreet);
        etBuilding = findViewById(R.id.etBuilding);
        etPostal = findViewById(R.id.etPostal);
        etAddition = findViewById(R.id.etAddition);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);

        // Set EditText
        btUpdate = findViewById(R.id.btUpdate);
        // Set Spinners

        String[] idTypes = {getString(R.string.selectUridType), "National ID", "Eqamah Number", "Passport ID"};
        spinnerIdType = findViewById(R.id.spinnerIdType);
        idTypesAdapter = new ArrayAdapter(this, R.layout.spinner_item, idTypes);
        idTypesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerIdType.setAdapter(idTypesAdapter);


        String[] countries = {getString(R.string.selectUrCoun), "Saudi Arabia", "United Arab Emirates", "Kuwait"};
        spinnerCountries = findViewById(R.id.spinnerCountries);
        ArrayAdapter countriesAdapter = new ArrayAdapter(this, R.layout.spinner_item, countries);
        countriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countriesAdapter);

        String[] cities = {getString(R.string.selectUrCit), "Abha", "Dammam", "Al Baha", "Jizan", "Najran", "Hail", "Makkah AL-Mukkaramah", "Al-Madinah Al-Munawarah", "Al Qaseem"
                , "Riyadh", "Jeddah", "Al-Khobar", "Taif", "Tabouk", "Jubail"};
        Arrays.sort(cities);
        spinnerCities = findViewById(R.id.spinnerCities);
        ArrayAdapter citiesAdapter = new ArrayAdapter(this, R.layout.spinner_item, cities);
        citiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCities.setAdapter(citiesAdapter);

        String[] sex = {"", "Male", "Female"};
        spinnerSex = findViewById(R.id.spinnerSex);
        ArrayAdapter sexAdapter = new ArrayAdapter(this, R.layout.spinner_item, sex);
        sexAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSex.setAdapter(sexAdapter);

        String[] typeOfD = {"", "Type A", "Type B", "Type C"};
        spinnerTypeOfD = findViewById(R.id.spinnerTypeOfD);
        ArrayAdapter typeOfDAdapter = new ArrayAdapter(this, R.layout.spinner_item, typeOfD);
        typeOfDAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeOfD.setAdapter(typeOfDAdapter);

        String[] typeOfBlood = {"", "O+", "O-", "AB+", "AB-", "A+", "A-", "B+", "B-"};
        spinnerTypeOfBlood = findViewById(R.id.spinnerTypeOfBlood);
        ArrayAdapter typeOfBloodAdapter = new ArrayAdapter(this, R.layout.spinner_item, typeOfBlood);
        typeOfBloodAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeOfBlood.setAdapter(typeOfBloodAdapter);

    }
}
