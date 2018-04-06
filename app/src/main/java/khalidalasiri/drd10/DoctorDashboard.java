package khalidalasiri.drd10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;

public class DoctorDashboard extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvUserName;
    Button btProfile;
    Button btPatients;

    BarChart bcAvarage;
    ListView lvDailyReport;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

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
        toolbar = findViewById(R.id.toolbar);
        tvUserName = findViewById(R.id.tvUserName);
        btProfile = findViewById(R.id.btProfile);
        btPatients = findViewById(R.id.btPatients);


        tvUserName.setText("Hello, " + userID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

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
                Intent logout = new Intent(DoctorDashboard.this, LoginActivity.class);
                startActivity(logout);
                finish();
                Toast.makeText(getApplicationContext(), R.string.logout_successfully, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btProfile:
                    Intent userDB = new Intent(DoctorDashboard.this, MyProfile.class);
                    userDB.putExtra("userID", userID);
                    startActivity(userDB);
                    break;
                case R.id.btPatients:
                    Intent myDoctor = new Intent(DoctorDashboard.this, MyDoctor.class);
                    myDoctor.putExtra("userID", userID);
                    startActivity(myDoctor);
                    break;

            }

        }
    };
}
