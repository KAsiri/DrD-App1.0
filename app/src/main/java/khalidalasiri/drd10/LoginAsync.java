package khalidalasiri.drd10;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

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

/**
 * Created by kasir on 3/19/2018.
 */

class LoginAsync extends AsyncTask<String, Void, String> {

    URL url;
    HttpURLConnection httpURLConnection;
    List<HttpCookie> token;

    public LoginAsync(List<HttpCookie> token) {
        this.token = token;
    }

    @Override
    protected String doInBackground(String... strings) {
        String password;
        try {
            password = strings[1];
            url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", token));

            // Receive the Content of the URL and build the String
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            JSONObject root = new JSONObject(builder.toString());
            JSONObject User_Information = root.getJSONObject("User_Information");
            JSONArray records = User_Information.getJSONArray("records");

            if (records.isNull(0)) {
                return "Invalid Login";
            } else {
                Log.d("print", password);
                if (password.equals(records.getJSONArray(0).get(8).toString()))
                    //Log.d("print", "Login Successfully");
                    return "Login Successfully";
                else
                    //Log.d("print", "Invalid Password");
                    return "Invalid Password";

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
