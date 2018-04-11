package khalidalasiri.drd10;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by kasir on 3/19/2018.
 */

class DoctorInfoAsync extends AsyncTask<String, Void, String[]> {

    private String tableURL;
    private URL url;
    private String primaryKey;
    private String columnName;
    private int index;
    private String tableName;
    StringBuilder builder;
    private HttpURLConnection httpURLConnection;
    private List<HttpCookie> token;
    ProgressDialog progressDialog ;
    Context context;

    public DoctorInfoAsync(List<HttpCookie> token, Context context, String tableURL, String tableName) {
        this.token = token;
        this.context = context ;
        this.tableURL = tableURL;
        this.tableName = tableName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait while connecting");
        progressDialog.show();
    }

    @Override
    protected String[] doInBackground(String... strings) {

        try {
            primaryKey = strings[0];
            index = Integer.parseInt(strings[1]);
            columnName = strings[2];
            String key = token.get(1).toString().replace("XSRF-TOKEN=","");

            String include= "&include=Treatment_Supervisor";
            builder = connection(key,columnName,primaryKey,tableURL,include);

            JSONObject root = new JSONObject(builder.toString());
            JSONObject Doctor = root.getJSONObject(tableName);
            JSONArray records = Doctor.getJSONArray("records");

            String result[] = new String[10];
            if (records.isNull(0)) {
                result = null;
                return result;
            } else {
                result[0] = "OK";
                result[1] = records.getJSONArray(0).get(0).toString() ; // The Doctor ID
                result[2] = records.getJSONArray(0).get(1).toString() ; // The User ID
                result[3] = records.getJSONArray(0).get(2).toString() ; // The Rank
                result[4] = records.getJSONArray(0).get(3).toString() ; // The Degree

                JSONObject Treatment_Supervisor = root.getJSONObject("Treatment_Supervisor");
                JSONArray records1 = Treatment_Supervisor.getJSONArray("records");

                result[5] = records1.getJSONArray(0).get(1).toString(); // Hospital ID

                tableName = "User_Information";
                tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/"+tableName;
                primaryKey = result[2];
                columnName = "ID";
                builder = connection(key,columnName,primaryKey,tableURL,"");

                JSONObject root2 = new JSONObject(builder.toString());
                JSONObject User_Information = root2.getJSONObject(tableName);
                JSONArray records2 = User_Information.getJSONArray("records");

                result[6] = records2.getJSONArray(0).get(1).toString() ; // The First_Name
                result[7] = records2.getJSONArray(0).get(3).toString() ; // The Family_Name

                Log.d("print",result[5]);


                // done
                // get the userID of the Hospital id in result[5]
                // then get the Hospital name
                tableName = "Hospital";
                tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/"+tableName;
                primaryKey = result[5];
                columnName = "HospitalID";
                builder = connection(key,columnName,primaryKey,tableURL,"");

                JSONObject root4 = new JSONObject(builder.toString());
                JSONObject Hospital = root4.getJSONObject(tableName);
                JSONArray records4 = Hospital.getJSONArray("records");

                result[9] = records4.getJSONArray(0).get(1).toString() ; // The user ID

                tableName = "User_Information";
                tableURL = "http://drd-ksa.com/drdAPI/AppAPI/api.php/"+tableName;
                primaryKey = result[9];
                columnName = "ID";
                builder = connection(key,columnName,primaryKey,tableURL,"");

                JSONObject root3 = new JSONObject(builder.toString());
                JSONObject User_Information1 = root3.getJSONObject(tableName);
                JSONArray records3 = User_Information1.getJSONArray("records");

                if (!records3.isNull(0))
                result[8] = records3.getJSONArray(0).get(1).toString() ; // The First_Name (Hospital name)

                return result;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private StringBuilder connection(String key,String columnName, String primaryKey, String tableURL,String include) {

        try {

            url = new URL(tableURL+"?csrf="+key+include+"&filter="+columnName+",eq,"+ primaryKey);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", token));

            // Receive the Content of the URL and build the String
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            builder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            return builder ;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        progressDialog.dismiss();
    }
}
