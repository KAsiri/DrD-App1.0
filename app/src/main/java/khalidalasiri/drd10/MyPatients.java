package khalidalasiri.drd10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.HttpCookie;
import java.util.List;

public class MyPatients extends AppCompatActivity {

    String userID;
    String doctorID;

    private List<HttpCookie> token;
    String connection_url;
    String message[];
    String tableURL;
    Context context;

    RecyclerView rvReportHistory;
    HistoryRVAdapter historyRVAdapter;
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
        // get the Patient ID from Extra
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

        btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(onClickListener);

        // RecyclerView of the Daily report
        rvReportHistory = findViewById(R.id.rvReportHistory);
        tvNoData = findViewById(R.id.tvNoData);
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
}
