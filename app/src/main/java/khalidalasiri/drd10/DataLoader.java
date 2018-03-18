package khalidalasiri.drd10;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kasir on 3/4/2018.
 */

public class DataLoader extends AsyncTaskLoader<List<UserInformation>> {
    public DataLoader(Context context) {
        super(context);
    }

    @Override
    public List<UserInformation> loadInBackground() {

        Log.d("Test","InBG");
        List<UserInformation> userInformationsList = new ArrayList<>();
        try {

            //Connecting to the Server
            URL url = new URL("http://drd-ksa.com/drdAPI/api.php/User_Information");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Receive the Content of the URL and build the String
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            // JSON Parsing

            JSONObject root = new JSONObject(builder.toString());
            JSONObject User_Information = root.getJSONObject("User_Information");
            JSONArray records = User_Information.getJSONArray("records");
            for (int i=0;i<records.length();i++)
            {
                UserInformation userInformation = new UserInformation(
                        records.getJSONArray(i).getInt(0),
                        records.getJSONArray(i).get(1).toString(), // FirstName
                        records.getJSONArray(i).get(5).toString(), // Email
                        records.getJSONArray(i).get(7).toString(),  // Username
                        records.getJSONArray(i).get(8).toString(),  //Password
                        records.getJSONArray(i).get(9).toString());    // UserType
                userInformationsList.add(userInformation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInformationsList;
    }
}


