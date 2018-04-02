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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by kasir on 3/19/2018.
 */

class GetAsync extends AsyncTask<String, Void, String[]> {

    private String tableURL;
    private URL url;
    private String primaryKey;
    private String columnName;
    private int index;
    private String tableName;
    private HttpURLConnection httpURLConnection;
    private List<HttpCookie> token;
    ProgressDialog progressDialog ;
    Context context;

    public GetAsync(List<HttpCookie> token, Context context,String tableURL,String tableName) {
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
            url = new URL(tableURL+"?csrf="+key+"&filter="+columnName+",eq,"+ primaryKey);
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
            JSONObject User_Information = root.getJSONObject(tableName);
            JSONArray records = User_Information.getJSONArray("records");

            String result[] = new String[2];
            if (records.isNull(0)) {
                result[0] = null;
                return result;
            } else {
                result[0] = "OK";
                result[1] = records.getJSONArray(0).get(index).toString() ; // The Patient ID
                return result;

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
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        progressDialog.dismiss();
    }
}
