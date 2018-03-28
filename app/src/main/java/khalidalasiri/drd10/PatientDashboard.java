package khalidalasiri.drd10;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

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
        btProfile = findViewById(R.id.btProfile);
        toolbar = findViewById(R.id.toolbar);
        tvUserName = findViewById(R.id.tvUserName);

        tvUserName.setText("Hello, "+userID);

        btProfile.setOnClickListener(onClickListener);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTheBarChart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            // TODO: 3/28/2018 change the action
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_feedback:
                Toast.makeText(getApplicationContext(),"Feedback",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
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

            }

        }
    };

}
