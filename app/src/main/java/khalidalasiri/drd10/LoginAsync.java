package khalidalasiri.drd10;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
 * Created by kasir on 3/19/2018.
 */

class LoginAsync extends AsyncTask<String, Integer, String[]> {

    private URL url;
    private HttpURLConnection httpURLConnection;
    private List<HttpCookie> token;
    ProgressBar bar;

    public LoginAsync(List<HttpCookie> token) {
        this.token = token;
    }

    public void setProgressBar(ProgressBar bar) {
        this.bar = bar;
    }
    @Override
    protected String[] doInBackground(String... strings) {
        String password;

        try {
            Log.d("print","Check");
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

            String result[] = new String[3];
            if (records.isNull(0)) {
                result[0] = "Invalid Login";
                return result;
            } else {
                Log.d("print", password);
                if (password.equals(records.getJSONArray(0).get(8).toString())) {
                    //Log.d("print", "Login Successfully");

                    result[0] = "Login Successfully";
                    result[1] = records.getJSONArray(0).get(0).toString() ;
                    result[2] = records.getJSONArray(0).get(9).toString() ;
                    return result;
                }
                else {
                    //Log.d("print", "Invalid Password");
                    result[0] = "Invalid Password";
                    return result;
                }

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



    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        bar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        bar.setProgress(0);
        bar.setVisibility(View.INVISIBLE);
    }
}
