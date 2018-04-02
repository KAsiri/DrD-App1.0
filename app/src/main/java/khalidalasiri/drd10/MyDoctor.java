package khalidalasiri.drd10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyDoctor extends AppCompatActivity {

    String userID;
    String patientID;

    // UI elements
    Button btHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);

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


        // initial the UI elements
        btHome = findViewById(R.id.btHome);


        // onClick
        btHome.setOnClickListener(onClickListener);
    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btHome:
                    Intent home = new Intent(MyDoctor.this, PatientDashboard.class);
                    home.putExtra("ID", userID);
                    home.putExtra("PatientID",patientID);
                    startActivity(home);
                    break;
            }

        }
    };
}
