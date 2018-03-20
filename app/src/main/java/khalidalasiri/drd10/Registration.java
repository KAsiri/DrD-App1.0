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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Registration extends AppCompatActivity {

    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btRegister;
    CheckBox checkBoxTP;
    private String connection_url;
    private List<HttpCookie> token;
    Context context;
    String[] message;
    String tableURL;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        message = new String[3];
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btRegister = findViewById(R.id.btRegister);
        checkBoxTP = findViewById(R.id.checkboxTP);
        tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/User_Information";
        // Register button clicked
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etUsername.getText().toString().isEmpty()
                        && !etEmail.getText().toString().isEmpty()
                        && checkBoxTP.isChecked()) {
                    if (!etPassword.getText().toString().isEmpty() && etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
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
                                    String key = token.get(1).toString().replace("XSRF-TOKEN=","");
                                    message = loginAsync.execute(tableURL+"?csrf="+key+"&filter=Username,eq,"
                                            + etUsername.getText().toString().replaceAll("\\s", ""),"").get();
                                    // Post the data and go to "Complete Registration page"
                                    if(message[0].equals("Invalid Login"))
                                    {
                                        //Encode the data into JSON format
                                        JSONObject jsonData = new JSONObject();

                                        try {
                                            jsonData.put("First_Name", "");
                                            jsonData.put("EMail", etEmail.getText().toString());
                                            jsonData.put("Phone", "");
                                            jsonData.put("Username", etUsername.getText().toString());
                                            jsonData.put("Password", etPassword.getText().toString());
                                            jsonData.put("UserType", "3");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        //Post the Data
                                        DataPoster dataPoster = new DataPoster(tableURL,token,context);
                                        userID = dataPoster.execute(jsonData.toString()).get();
                                        // Go to the next page
                                        Intent toCompleteRegistration = new Intent(Registration.this, CompleteRegistration.class);
                                        toCompleteRegistration.putExtra("userID",userID);
                                        startActivity(toCompleteRegistration);
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), R.string.error_usernameExist, Toast.LENGTH_LONG).show();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_noInternet, Toast.LENGTH_LONG).show();
                        }

                    } else
                        Toast.makeText(getApplicationContext(), R.string.passwordNotMatch, Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getApplicationContext(), R.string.reqiredFiled, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void LoginPage(View view) {
        Intent toLogin = new Intent(Registration.this, LoginActivity.class);
        startActivity(toLogin);
    }
}
