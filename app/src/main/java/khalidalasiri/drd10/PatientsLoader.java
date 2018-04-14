package khalidalasiri.drd10;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PatientsLoader extends AsyncTaskLoader<List<Patients>> {

    private String tableURL;
    private URL url;
    private String primaryKey;
    private String columnName;
    private int index;
    private String tableName;
    private String filter;

    private String include;
    private HttpURLConnection httpURLConnection;
    private List<HttpCookie> token;

    public PatientsLoader(Context context, String tableURL,String primaryKey, String columnName, String tableName, String include,String filter, List<HttpCookie> token) {
        super(context);
        this.tableURL = tableURL;
        this.url = url;
        this.primaryKey = primaryKey;
        this.columnName = columnName;
        this.index = index;
        this.tableName = tableName;
        this.filter = filter;
        this.include = include;
        this.httpURLConnection = httpURLConnection;
        this.token = token;
    }


    @Override
    public List<Patients> loadInBackground() {
        List<Patients> patientsList = new ArrayList<>();

        try {
            String key = token.get(1).toString().replace("XSRF-TOKEN=","");
            url = new URL(tableURL+"?csrf="+key+include+filter+primaryKey);
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
            JSONObject Specialist = root.getJSONObject(tableName);
            JSONArray records = Specialist.getJSONArray("records");

            // Read the data from Specialist, UserInformation and Patient table

            for (int i=0; i < records.length();i++)
            {
                JSONObject Patient = root.getJSONObject("Patient");
                JSONArray records1 = Patient.getJSONArray("records");

                JSONObject User_Information = root.getJSONObject("User_Information");
                JSONArray records2 = User_Information.getJSONArray("records");

                Patients patients = new Patients(records.getJSONArray(i).get(0).toString(),
                        records2.getJSONArray(i).get(1).toString(),
                        records1.getJSONArray(i).get(3).toString(),
                        records1.getJSONArray(i).get(4).toString());

                patientsList.add(patients);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patientsList;
    }
}
