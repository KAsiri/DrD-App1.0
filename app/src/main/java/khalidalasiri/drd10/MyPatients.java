package khalidalasiri.drd10;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyPatients extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Patients>>  {

    String userID;
    String doctorID;

    private List<HttpCookie> token;
    String connection_url;
    String message[];
    String tableURL;
    Context context;

    RecyclerView rvPatientsList;
    PatientsRVAdapter patientsRVAdapter;
    TextView tvNoData;

    Button btHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pateints);

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
        // get the Doctor ID from Extra
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

        Toast.makeText(getApplicationContext(),doctorID,Toast.LENGTH_SHORT).show();
        btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(onClickListener);

        // RecyclerView of the Daily report
        rvPatientsList = findViewById(R.id.rvPatientsList);
        tvNoData = findViewById(R.id.tvNoData);
        loadPatientsList();
    }

    private void loadPatientsList() {
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

                    rvPatientsList.setLayoutManager(new LinearLayoutManager(this));
                    patientsRVAdapter = new PatientsRVAdapter(context,new ArrayList<Patients>());
                    rvPatientsList.setAdapter(patientsRVAdapter);
                    getSupportLoaderManager().initLoader(0, null, this).forceLoad();
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
                    Intent home = new Intent(MyPatients.this, DoctorDashboard.class);
                    home.putExtra("userID", userID);
                    startActivity(home);
                    finish();
                    break;
            }
        }
    };

    @Override
    public Loader<List<Patients>> onCreateLoader(int id, Bundle args) {
        String tableName = "Specialist";
        String filter = "&filter=Specialist.DoctorID,eq,";
        String include = "&include=Patient,User_Information";
        return new PatientsLoader(context,tableURL,doctorID,"DoctorID",tableName,include,filter,token);
    }

    @Override
    public void onLoadFinished(Loader<List<Patients>> loader, List<Patients> data) {
        if (data.isEmpty()) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            patientsRVAdapter = new PatientsRVAdapter(this, data);
            rvPatientsList.setAdapter(patientsRVAdapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Patients>> loader) {

    }
}
