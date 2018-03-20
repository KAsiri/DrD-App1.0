package khalidalasiri.drd10;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Registration extends AppCompatActivity {

    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btRegister;
    CheckBox checkBoxTP ;
    private String connection_url;
    private List<HttpCookie> token;
    Context context;
    String []message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this ;
        message = new String[3];
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btRegister = findViewById(R.id.btRegister);
        checkBoxTP = findViewById(R.id.checkboxTP);
        // Register button clicked
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etPassword.getText().toString().isEmpty() && etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
                    if (!etUsername.getText().toString().isEmpty()
                            && !etEmail.getText().toString().isEmpty()
                            && checkBoxTP.isChecked())
                    {
                        // Check the Internet Connection
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo ni = null;
                        if (cm != null) {
                            ni = cm.getActiveNetworkInfo();

                            if (ni != null && ni.isConnected()) {
                                try {
                                    // make the Connection to API
                                    connection_url = "http://drd-ksa.com/drdAPI/AppAPI/api.php/";
                                    ConnectionToken connectionToken = new ConnectionToken(context);
                                    token = connectionToken.execute(connection_url).get();

                                    // Check the username availability with the server
                                    LoginAsync loginAsync = new LoginAsync(token, context);
                                    message = loginAsync.execute("http://drd-ksa.com/drdAPI/api.php/User_Information?filter=Username,eq,"
                                                    + etUsername.getText().toString().replaceAll("\\s", "")).get();

                                    Log.d("print the message",message[0]);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_noInternet), Toast.LENGTH_LONG).show();
                        }

                    }
                    else
                        Toast.makeText(getApplicationContext(),R.string.reqiredFiled,Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),R.string.passwordNotMatch,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void LoginPage(View view) {
        Intent toLogin = new Intent(Registration.this, LoginActivity.class);
        startActivity(toLogin);
    }
}
