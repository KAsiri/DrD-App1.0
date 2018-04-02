package khalidalasiri.drd10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PatientDashboard extends AppCompatActivity {

    Button btProfile;
    Button btDoctor;
    Button btHistory;
    Button btReport;

    Toolbar toolbar;
    TextView tvUserName;

    BarChart bcAvarage;
    ListView lvDailyReport;
    String userID;
    String patientID;

    private List<HttpCookie> token;
    String connection_url;
    String message[];
    String tableURL;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

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
        // initial the views
        btProfile = findViewById(R.id.btProfile);
        btReport = findViewById(R.id.btReport);
        btDoctor = findViewById(R.id.btDoctor);
        btHistory = findViewById(R.id.btHistory);
        toolbar = findViewById(R.id.toolbar);
        tvUserName = findViewById(R.id.tvUserName);

        tvUserName.setText("Hello, " + userID);

        // add action to buttons
        btProfile.setOnClickListener(onClickListener);
        btHistory.setOnClickListener(onClickListener);
        btDoctor.setOnClickListener(onClickListener);
        btReport.setOnClickListener(onClickListener);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTheBarChart();

        // get Patient ID
        patientID = getPatientID(userID);

    }

    private String getPatientID(String userID) {
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
                    GetAsync getAsync = new GetAsync(token,context,tableURL,"Patient");
                    message = getAsync.execute(userID,"0","UserID").get();   // 0 is the index of the patine ID on the table on database

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // TODO: 3/28/2018 change the action
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_feedback:
                Toast.makeText(getApplicationContext(), "Feedback", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTheBarChart() {
        bcAvarage = findViewById(R.id.bcAvarage);

        // Example of coding
        // TODO: 3/27/2018 Delete
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(4f, 0));
        barEntries.add(new BarEntry(5f, 1));
        barEntries.add(new BarEntry(7f, 2));
        barEntries.add(new BarEntry(6f, 3));

        BarDataSet barDataSet = new BarDataSet(barEntries, "BG");
        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("First");
        theDate.add("Second");
        theDate.add("Third");
        theDate.add("Fourth");

        BarData theData = new BarData(barDataSet);
        bcAvarage.setData(theData);
        bcAvarage.setTouchEnabled(true);
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btProfile:
                    Intent userDB = new Intent(PatientDashboard.this, CompleteRegistration.class);
                    userDB.putExtra("ID", userID);
                    startActivity(userDB);
                    break;
                case R.id.btDoctor:
                    Intent myDoctor = new Intent(PatientDashboard.this, CompleteRegistration.class);
                    myDoctor.putExtra("ID", userID);
                    startActivity(myDoctor);
                    break;
                case R.id.btReport:
                    Intent newReport = new Intent(PatientDashboard.this, PatientReport.class);
                    newReport.putExtra("ID", userID);
                    newReport.putExtra("PatientID",patientID);
                    startActivity(newReport);
                    break;
                case R.id.btHistory:
                    Intent reportHistory = new Intent(PatientDashboard.this, CompleteRegistration.class);
                    reportHistory.putExtra("ID", userID);
                    startActivity(reportHistory);
                    break;

            }

        }
    };

}
