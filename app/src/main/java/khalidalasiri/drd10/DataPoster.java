package khalidalasiri.drd10;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

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

/**
 * Created by kasir on 3/6/2018.
 */

public class DataPoster extends AsyncTask<String,String,String> {

    List<HttpCookie> token ;

    public DataPoster(List<HttpCookie>  token) {
        this.token = token;
    }

    @Override
    protected String doInBackground(String... params) {

        String jsonData = params[0];
        String JsonResponse = null;

        //Connecting to the Server
        URL url = null;
        try {
            String key = token.get(1).toString().replace("XSRF-TOKEN=","");
            Log.d("token",key);
            url = new URL("http://drd-ksa.com/drdAPI/api.php/User_Information?csrf="+key);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Cookie", TextUtils.join(";",token));

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(jsonData);
            writer.close();
            InputStream inputStream = connection.getInputStream();
            if (inputStream == null)
            {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                stringBuffer.append(inputLine + "\n");
            if (stringBuffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            JsonResponse = stringBuffer.toString();
            //response data
            Log.d("Response",JsonResponse);
            //send to post execute
            return JsonResponse;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
