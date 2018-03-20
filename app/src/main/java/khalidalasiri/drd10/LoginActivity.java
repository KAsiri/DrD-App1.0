package khalidalasiri.drd10;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText etPassword;
    private Button btLogin;
    private Button btRegister;
    private List<HttpCookie> token;
    String connection_url;
    String message[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // defined the UI elements
        mEmailView = findViewById(R.id.actvEmail);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        message = new String[3];
        final Context context = this;

        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((!etPassword.getText().toString().isEmpty()) && (!mEmailView.getText().toString().isEmpty())) {

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo ni = null;
                    if (cm != null) {
                        ni = cm.getActiveNetworkInfo();

                        if (ni != null && ni.isConnected()) {
                            try {

                                connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                                ConnectionToken connectionToken = new ConnectionToken(context);
                                token = connectionToken.execute(connection_url).get();

                                LoginAsync loginAsync = new LoginAsync(token, context);
                                message = loginAsync.execute("http://drd-ksa.com/drdAPI/api.php/User_Information?filter=Username,eq,"
                                                + mEmailView.getText().toString().replaceAll("\\s", ""),
                                        etPassword.getText().toString()).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (message[0].equals("Invalid Password") || message[0].equals("Invalid Login")) {
                                Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                            } else if (message[0].equals("Login Successfully")) {
                                Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                                if (message[2].equals("3")) {
                                    Intent userDB = new Intent(LoginActivity.this, PatientDashboard.class);
                                    userDB.putExtra("ID", message[1]);
                                    startActivity(userDB);
                                } else if (message[2].equals("2")) {
                                    Intent userDB = new Intent(LoginActivity.this, DoctorDashboard.class);
                                    userDB.putExtra("ID", message[1]);
                                    startActivity(userDB);
                                }
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_noInternet), Toast.LENGTH_LONG).show();
                        }
                    }


                } else
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            }
        });

        btRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userRegis = new Intent(LoginActivity.this, Registration.class);
                startActivity(userRegis);
            }
        });
    }

}
