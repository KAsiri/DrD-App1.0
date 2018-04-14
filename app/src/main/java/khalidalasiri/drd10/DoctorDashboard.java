package khalidalasiri.drd10;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DoctorDashboard extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvUserName;
    Button btProfile;
    Button btPatients;

    BarChart bcAvarage;
    ListView lvDailyReport;
    String userID;
    String doctorID;

    private List<HttpCookie> token;
    String connection_url;
    String message[];
    String tableURL;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

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
        toolbar = findViewById(R.id.toolbar);
        tvUserName = findViewById(R.id.tvUserName);
        btProfile = findViewById(R.id.btProfile);
        btPatients = findViewById(R.id.btPatients);


        // get the Doctor ID
        doctorID = getDoctorID(userID);
        if (doctorID == null)
        {
                Toast.makeText(getApplicationContext(),R.string.errorDoctorIDNotFound,Toast.LENGTH_LONG).show();
        }
        tvUserName.setText("Hello, " + userID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btProfile.setOnClickListener(onClickListener);
        btPatients.setOnClickListener(onClickListener);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private String getDoctorID(String userID) {
        doctorID = null;
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
                    GetAsync getAsync = new GetAsync(token,context,tableURL,"Doctor");
                    message = getAsync.execute(userID,"0","UserID").get();   // 0 is the index of the Doctor ID on the table on database

                    if(message == null)
                    {
                        Toast.makeText(getApplicationContext(), R.string.error_getValue, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        doctorID = message[1];
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
        return doctorID ;
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
                    Intent userDB = new Intent(DoctorDashboard.this, DoctorMyProfile.class);
                    userDB.putExtra("userID", userID);
                    startActivity(userDB);
                    break;
                case R.id.btPatients:
                    Intent myPatients = new Intent(DoctorDashboard.this, MyPatients.class);
                    myPatients.putExtra("userID", userID);
                    myPatients.putExtra("DoctorID", doctorID);
                    startActivity(myPatients);
                    break;

            }

        }
    };
}
