package khalidalasiri.drd10;

import android.app.Application;
import android.content.Context;
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
    private View login_progress;
    private Button btLogin;
    private Button btRegister;

    List<HttpCookie> token;
    String url;
    String message ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // defined the UI elements
        mEmailView = findViewById(R.id.actvEmail);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);

        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((!etPassword.getText().toString().isEmpty()) && (!mEmailView.getText().toString().isEmpty())) {

                    url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                    try {
                        token = new ConnectionToken().execute(url).get();
                        message = new LoginAsync(token).execute("http://drd-ksa.com/drdAPI/api.php/User_Information?filter=Username,eq,"
                                        + mEmailView.getText().toString().replaceAll("\\s", ""),
                                etPassword.getText().toString()).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
