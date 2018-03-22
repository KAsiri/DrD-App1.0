package khalidalasiri.drd10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CompleteRegistration extends AppCompatActivity {

    Spinner spinnerIdType;
    Spinner spinnerCountries;
    Spinner spinnerCities;
    Spinner spinnerSex;
    Spinner spinnerTypeOfD;
    Spinner spinnerTypeOfBlood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_reistration);

        setSpinners();

    }

    // Set the Spinners used on the UI
    private void setSpinners() {
        String[] idTypes = { "National ID", "Eqamah Number", "Passport ID"};
        spinnerIdType = findViewById(R.id.spinnerIdType);
        ArrayAdapter idTypesAdapter = new ArrayAdapter(this,R.layout.spinner_item,idTypes);
        idTypesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerIdType.setAdapter(idTypesAdapter);

        String[] countries = { "Saudi Arabia", "United Arab Emirates", "Kuwait"};
        spinnerCountries = findViewById(R.id.spinnerCountries);
        ArrayAdapter countriesAdapter = new ArrayAdapter(this,R.layout.spinner_item,countries);
        countriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(countriesAdapter);

        String[] cities = { "Abha","Dammam","Al Baha", "Jizan","Najran","Hail","Makkah AL-Mukkaramah","AL-Madinah Al-Munawarah","Al Qaseem"
                ,"Riyadh","Jeddah","Al-Khobar","Taif","Tabouk","Jubail"};
        Arrays.sort(cities);
        spinnerCities = findViewById(R.id.spinnerCities);
        ArrayAdapter citiesAdapter = new ArrayAdapter(this,R.layout.spinner_item,cities);
        citiesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCities.setAdapter(citiesAdapter);

        String[] sex = { "Male", "Female"};
        spinnerSex = findViewById(R.id.spinnerSex);
        ArrayAdapter sexAdapter = new ArrayAdapter(this,R.layout.spinner_item,sex);
        sexAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSex.setAdapter(sexAdapter);

        String[] typeOfD = { "Type A", "Type B", "Type C"};
        spinnerTypeOfD = findViewById(R.id.spinnerTypeOfD);
        ArrayAdapter typeOfDAdapter = new ArrayAdapter(this,R.layout.spinner_item,typeOfD);
        typeOfDAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeOfD.setAdapter(typeOfDAdapter);

        String[] typeOfBlood = { "O+","O-","AB+", "AB-","A+","A-","B+","B-"};
        spinnerTypeOfBlood = findViewById(R.id.spinnerTypeOfBlood);
        ArrayAdapter typeOfBloodAdapter = new ArrayAdapter(this,R.layout.spinner_item,typeOfBlood);
        typeOfBloodAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeOfBlood.setAdapter(typeOfBloodAdapter);

    }
}
