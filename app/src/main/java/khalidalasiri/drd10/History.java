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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class History extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Report>> {

    String userID;
    String patientID;

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
        setContentView(R.layout.activity_history);

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
                patientID = null;
            } else {
                patientID = extras.getString("PatientID");
            }
        } else {
            patientID = (String) savedInstanceState.getSerializable("PatientID");
        }

        btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(onClickListener);

        // RecyclerView of the Daily report
        rvReportHistory = findViewById(R.id.rvReportHistory);
        tvNoData = findViewById(R.id.tvNoData);
        loadReportHistory();

    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btHome:
                    Intent home = new Intent(History.this, PatientDashboard.class);
                    home.putExtra("userID", userID);
                    home.putExtra("PatientID", patientID);
                    startActivity(home);
                    finish();
                    break;
            }
        }
    };

    private void loadReportHistory() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (cm != null) {
            ni = cm.getActiveNetworkInfo();

            if (ni != null && ni.isConnected()) {
                try {
                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    ConnectionToken connectionToken = new ConnectionToken(context);
                    token = connectionToken.execute(connection_url).get();
                    tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/Patient_History";

                    rvReportHistory.setLayoutManager(new LinearLayoutManager(this));
                    historyRVAdapter = new HistoryRVAdapter(context,new ArrayList<Report>(),userID,patientID);
                    rvReportHistory.setAdapter(historyRVAdapter);
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

    @Override
    public Loader<List<Report>> onCreateLoader(int id, Bundle args) {
        String tableName = "Report";
        String filter = "";
        return new ReportLoader(context,tableURL,patientID,"Patient_History.PatientID",tableName,filter,token);
    }

    @Override
    public void onLoadFinished(Loader<List<Report>> loader, List<Report> data) {
        if (data.isEmpty()) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            historyRVAdapter = new HistoryRVAdapter(this, data,userID,patientID);
            rvReportHistory.setAdapter(historyRVAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Report>> loader) {

    }
}
