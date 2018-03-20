package khalidalasiri.drd10;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by kasir on 3/8/2018.
 */

public class ConnectionToken extends AsyncTask<String,Void,List<HttpCookie> >{

    String server_response;
    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    ProgressDialog progressDialog ;
    Context context;

    public ConnectionToken (Context context)
    {
        this.context = context ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Connect to the Server");
        progressDialog.show();
    }

    @Override
    protected List<HttpCookie> doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        Log.d("print","ConnectionToken");
        // Username and Password on JSON format
        JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("username", "admin");
            jsonData.put("password", "admin");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ToDO check the Internet Connection

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(jsonData.toString());
            writer.close();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);
            }
            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                }
            }
            Log.d("Cookies",msCookieManager.getCookieStore().getCookies().toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msCookieManager.getCookieStore().getCookies();
    }

    @Override
    protected void onPostExecute(List<HttpCookie> s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }

// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

}
