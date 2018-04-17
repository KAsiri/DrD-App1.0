package khalidalasiri.drd10;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

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

class PatientRespondInformationAsync extends AsyncTask<String, Void, String[]> {

    private String tableURL;
    private URL url;
    private String primaryKey;
    private String secondPrimaryKey;
    private String firstColumnName;
    private String secondColumnName;
    private int index;
    private String tableName;
    private String include;
    StringBuilder builder;
    private HttpURLConnection httpURLConnection;
    private List<HttpCookie> token;
    ProgressDialog progressDialog ;
    Context context;

    public PatientRespondInformationAsync(List<HttpCookie> token, Context context, String tableURL, String tableName, String include) {
        this.token = token;
        this.context = context ;
        this.tableURL = tableURL;
        this.tableName = tableName;
        this.include = include;
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
            firstColumnName = strings[1];
            secondPrimaryKey = strings[2];
            secondColumnName = strings[3];
            String key = token.get(1).toString().replace("XSRF-TOKEN=","");

            String filter1 = "&filter[]="+firstColumnName+",eq,"+primaryKey;
            String filter2 = "&filter[]="+secondColumnName+",eq,"+secondPrimaryKey;
            builder = connection(key,tableURL,include,filter1,filter2);

            JSONObject root = new JSONObject(builder.toString());
            JSONObject jsonObject1 = root.getJSONObject(tableName);
            JSONArray records = jsonObject1.getJSONArray("records");

            String result[] = new String[15];
            if (records.isNull(0)) {
                result = null;
                return result;
            } else {
                result[0] = "OK";
                result[1] = records.getJSONArray(0).get(1).toString() ; // The userID
                result[2] = records.getJSONArray(0).get(2).toString() ; // The DateOfBirth
                result[3] = records.getJSONArray(0).get(3).toString() ; // The Sex
                result[4] = records.getJSONArray(0).get(4).toString() ; // The TypeOfDiabetes
                result[5] = records.getJSONArray(0).get(5).toString() ; // The Weight
                result[6] = records.getJSONArray(0).get(6).toString() ; // The Height
                result[7] = records.getJSONArray(0).get(7).toString() ; // The BloodType

                JSONObject jsonObject2 = root.getJSONObject("User_Information");
                JSONArray records1 = jsonObject2.getJSONArray("records");

                if (!records1.isNull(0)) {
                    result[8] = records1.getJSONArray(0).get(1).toString(); // The First_Name
                    result[9] = records1.getJSONArray(0).get(3).toString(); // The Family_Name
                }
                JSONObject jsonObject3 = root.getJSONObject("Report");
                JSONArray records2 = jsonObject3.getJSONArray("records");

                if (!records2.isNull(0)) {
                    result[10] = records2.getJSONArray(0).get(1).toString(); // The ReportDate
                    result[11] = records2.getJSONArray(0).get(2).toString(); // The BloodPressure
                    result[12] = records2.getJSONArray(0).get(3).toString(); // The BloodGlucoseAnalysis
                    result[13] = records2.getJSONArray(0).get(4).toString(); // The HeartRate
                }
                return result;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private StringBuilder connection(String key, String tableURL,String include,String filter1,String filter2) {

        try {

            url = new URL(tableURL+"?csrf="+key+include+filter1+filter2);
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
