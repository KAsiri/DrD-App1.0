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

class PatientInfoAsync extends AsyncTask<String, Void, String[]> {

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

    public PatientInfoAsync(List<HttpCookie> token, Context context, String tableURL, String tableName) {
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

            String include= "&include=User_Address,Identification_Number,Patient";
            builder = connection(key,columnName,primaryKey,tableURL,include);

            JSONObject root = new JSONObject(builder.toString());
            JSONObject User_Information = root.getJSONObject(tableName);
            JSONArray records = User_Information.getJSONArray("records");

            String result[] = new String[24];
            if (records.isNull(0)) {
                result = null;
                return result;
            } else {
                result[0] = "OK";
                result[1] = records.getJSONArray(0).get(0).toString() ; // The ID
                result[2] = records.getJSONArray(0).get(1).toString() ; // The First Name
                result[3] = records.getJSONArray(0).get(2).toString() ; // The Second Name
                result[4] = records.getJSONArray(0).get(3).toString() ; // The Last Name
                result[5] = records.getJSONArray(0).get(4).toString() ; // The Email
                result[6] = records.getJSONArray(0).get(5).toString() ; // The Phone
                result[7] = records.getJSONArray(0).get(6).toString() ; // The Username

                JSONObject User_Address = root.getJSONObject("User_Address");
                JSONArray records1 = User_Address.getJSONArray("records");

                if (!records1.isNull(0)) {
                    result[8] = records1.getJSONArray(0).get(1).toString(); // The Country
                    result[9] = records1.getJSONArray(0).get(2).toString(); // The City
                    result[10] = records1.getJSONArray(0).get(3).toString(); // The Neighborhood
                    result[11] = records1.getJSONArray(0).get(4).toString(); // The StreetName
                    result[12] = records1.getJSONArray(0).get(5).toString(); // The BuildingNumber
                    result[13] = records1.getJSONArray(0).get(6).toString(); // The PostalCode
                    result[14] = records1.getJSONArray(0).get(7).toString(); // The AdditionNumber
                }
                JSONObject Identification_Number = root.getJSONObject("Identification_Number");
                JSONArray records2 = Identification_Number.getJSONArray("records");

                if (!records2.isNull(0)) {
                    result[15] = records2.getJSONArray(0).get(1).toString(); // The IdentificationType
                    result[16] = records2.getJSONArray(0).get(2).toString(); // The IdentificationNumber
                }
                JSONObject Patient = root.getJSONObject("Patient");
                JSONArray records3 = Patient.getJSONArray("records");

                if (!records3.isNull(0)) {
                    result[17] = records3.getJSONArray(0).get(2).toString(); // The DateOfBirth
                    result[18] = records3.getJSONArray(0).get(3).toString(); // The Sex
                    result[19] = records3.getJSONArray(0).get(4).toString(); // The TypeOfDiabetes
                    result[20] = records3.getJSONArray(0).get(5).toString(); // The Weight
                    result[21] = records3.getJSONArray(0).get(6).toString(); // The Height
                    result[22] = records3.getJSONArray(0).get(7).toString(); // The BloodType
                }
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
