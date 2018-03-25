package khalidalasiri.drd10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PatientDashboard extends AppCompatActivity {

    Button btProfile;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

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
        btProfile = findViewById(R.id.btProfile);

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userDB = new Intent(PatientDashboard.this, CompleteRegistration.class);
                userDB.putExtra("ID", userID);
                startActivity(userDB);
            }
        });
    }
}
