package khalidalasiri.drd10;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.Date ;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CompleteRegistration extends AppCompatActivity {

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
    Button btSave;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_reistration);
        // Set all the UI elements
        setElements();
        // initializing the variables
        context = this;
        message = new String[3];
        // get the User ID from Extra
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userID= null;
            } else {
                userID= extras.getString("userID");
            }
        } else {
            userID= (String) savedInstanceState.getSerializable("userID");
        }

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fristName.getText().toString().isEmpty() &&
                        !lastName.getText().toString().isEmpty() &&
                        !etPhone.getText().toString().isEmpty() &&
                        !etDOB.getText().toString().isEmpty() &&
                        !etWeight.getText().toString().isEmpty() &&
                        !etHeight.getText().toString().isEmpty() &&
                        !spinnerCountries.getSelectedItem().equals(getString(R.string.selectUrCoun)) &&
                        !spinnerCities.getSelectedItem().equals(getString(R.string.selectUrCoun)) &&
                        !spinnerSex.getSelectedItem().equals("") &&
                        !spinnerTypeOfD.getSelectedItem().equals("") &&
                        !spinnerTypeOfBlood.getSelectedItem().equals(""))
                {
                    // Check the Internet Connection
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

                                // encode the data into JSon for User_Information
                                JSONObject jsonData = new JSONObject();
                                try {
                                    jsonData.put("First_Name", fristName.getText().toString());
                                    jsonData.put("Father_Name", middleName.getText().toString());
                                    jsonData.put("Family_Name", lastName.getText().toString());
                                    jsonData.put("Phone", etPhone.getText().toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/User_Information/"+userID;
                                //Post the Data
                                DataPoster dataPoster = new DataPoster(tableURL,"PUT",token,context);
                                String respond = dataPoster.execute(jsonData.toString()).get();

                                // encode the data into JSon for User_Address table
                                JSONObject jsonData2 = new JSONObject();
                                try {
                                    jsonData2.put("UserID", userID);
                                    jsonData2.put("Country", spinnerCountries.getSelectedItem().toString());
                                    jsonData2.put("City", spinnerCities.getSelectedItem().toString());
                                    jsonData2.put("Neighborhood", etNeighborhood.getText().toString());
                                    jsonData2.put("StreetName", etStreet.getText().toString());
                                    jsonData2.put("BuildingNumber", etBuilding.getText().toString());
                                    jsonData2.put("PostalCode", etPostal.getText().toString());
                                    jsonData2.put("AdditionNumber", etAddition.getText().toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/User_Address";
                                //Post the Data
                                DataPoster dataPoster2 = new DataPoster(tableURL,"POST",token,context);
                                String respond2 = dataPoster2.execute(jsonData2.toString()).get();

                                // TODO: 3/23/2018
                                // Create 3 spinner for get the date of birth and save it in String dob in yyyy-mm-dd format

                                // encode the data into JSon for Patient table
                                JSONObject jsonData3 = new JSONObject();
                                try {
                                    jsonData3.put("UserID", userID);
                                    // TODO: 3/24/2018 Done
                                    // there is problem on submit the DOB, the problem was in database

                                    Date dob = new SimpleDateFormat("dd-mm-yyyy").parse(etDOB.getText().toString());

                                    jsonData3.put("DateOfBirth", new SimpleDateFormat("yyyy-MM-dd").format(dob));
                                    jsonData3.put("Sex", spinnerSex.getSelectedItem().toString());
                                    jsonData3.put("TypeOfDiabetes", spinnerTypeOfD.getSelectedItem().toString());
                                    jsonData3.put("Weight", etWeight.getText().toString());
                                    jsonData3.put("Height", etHeight.getText().toString());
                                    jsonData3.put("BloodType", spinnerTypeOfBlood.getSelectedItem().toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Patient";
                                //Post the Data
                                DataPoster dataPoster3 = new DataPoster(tableURL,"POST",token,context);
                                String respond3 = dataPoster3.execute(jsonData3.toString()).get();

                                // encode the data into JSon for Identification_Number table
                                JSONObject jsonData4 = new JSONObject();
                                try {
                                    jsonData4.put("UserID", userID);
                                    jsonData4.put("IdentificationType", spinnerIdType.getSelectedItem().toString());
                                    jsonData4.put("IdentificationNumber", etID.getText().toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Identification_Number";
                                //Post the Data
                                DataPoster dataPoster4 = new DataPoster(tableURL,"POST",token,context);
                                String respond4 = dataPoster4.execute(jsonData4.toString()).get();

                                Log.d("respond:",respond);
                                Log.d("respond1:",respond2);
                                Log.d("respond2:",respond3);
                                Log.d("respond3:",respond4);

                                // TODO: 3/23/2018      Done
                                // fix the if statement
                                if(!respond.isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), R.string.dataSavedSuccessfully, Toast.LENGTH_SHORT).show();
                                    // Go to the next page
                                    Intent userDB = new Intent(CompleteRegistration.this, PatientDashboard.class);
                                    userDB.putExtra("ID", userID);
                                    startActivity(userDB);
                                }
                                else
                                    Toast.makeText(getApplicationContext(), R.string.error_notSaved, Toast.LENGTH_SHORT).show();


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_noInternet, Toast.LENGTH_LONG).show();
                    }

                }
                else
                    // Show Error "Please complete the required field"
                    Toast.makeText(getApplicationContext(),R.string.error_requiredField,Toast.LENGTH_LONG).show();
            }
        });
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
        btSave = findViewById(R.id.btSave);
        // Set Spinners

        String[] idTypes = {getString(R.string.selectUridType),"National ID", "Eqamah Number", "Passport ID"};
        spinnerIdType = findViewById(R.id.spinnerIdType);
        ArrayAdapter idTypesAdapter = new ArrayAdapter(this, R.layout.spinner_item, idTypes);
        idTypesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerIdType.setAdapter(idTypesAdapter);

        String[] countries = {getString(R.string.selectUrCoun),"Saudi Arabia", "United Arab Emirates", "Kuwait"};
        spinnerCountries = findViewById(R.id.spinnerCountries);
        ArrayAdapter countriesAdapter = new ArrayAdapter(this, R.layout.spinner_item, countries);
        countriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countriesAdapter);

        String[] cities = {getString(R.string.selectUrCit),"Abha", "Dammam", "Al Baha", "Jizan", "Najran", "Hail", "Makkah AL-Mukkaramah", "Al-Madinah Al-Munawarah", "Al Qaseem"
                , "Riyadh", "Jeddah", "Al-Khobar", "Taif", "Tabouk", "Jubail"};
        Arrays.sort(cities);
        spinnerCities = findViewById(R.id.spinnerCities);
        ArrayAdapter citiesAdapter = new ArrayAdapter(this, R.layout.spinner_item, cities);
        citiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCities.setAdapter(citiesAdapter);

        String[] sex = {"","Male", "Female"};
        spinnerSex = findViewById(R.id.spinnerSex);
        ArrayAdapter sexAdapter = new ArrayAdapter(this, R.layout.spinner_item, sex);
        sexAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSex.setAdapter(sexAdapter);

        String[] typeOfD = {"","Type A", "Type B", "Type C"};
        spinnerTypeOfD = findViewById(R.id.spinnerTypeOfD);
        ArrayAdapter typeOfDAdapter = new ArrayAdapter(this, R.layout.spinner_item, typeOfD);
        typeOfDAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeOfD.setAdapter(typeOfDAdapter);

        String[] typeOfBlood = {"","O+", "O-", "AB+", "AB-", "A+", "A-", "B+", "B-"};
        spinnerTypeOfBlood = findViewById(R.id.spinnerTypeOfBlood);
        ArrayAdapter typeOfBloodAdapter = new ArrayAdapter(this, R.layout.spinner_item, typeOfBlood);
        typeOfBloodAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeOfBlood.setAdapter(typeOfBloodAdapter);

    }
}
