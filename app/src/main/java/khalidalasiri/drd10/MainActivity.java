package khalidalasiri.drd10;

import android.content.Intent;
import android.media.session.MediaSession;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<UserInformation>> {

    Button post;
    List<HttpCookie>  token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(0,null,this).forceLoad();

        post = findViewById(R.id.post);
post.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent n = new Intent(MainActivity.this , LoginActivity.class);
        startActivity(n);
    }
});

    }

    public void senddatatoserver(View v) {

        JSONObject postData = new JSONObject();

        try {
            postData.put("First_Name", "Khalid");
            postData.put("EMail", "k@k.com");
            postData.put("Phone", "555333");
            postData.put("Username", "kkk");
            postData.put("Password", "k123456");
            postData.put("UserType", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (postData.length() > 0) {
            new DataPoster(token).execute(String.valueOf(postData));
        }
    }

    @Override
    public Loader<List<UserInformation>> onCreateLoader(int id, Bundle args) {
        Log.d("Test","Loader");
        return new DataLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<UserInformation>> loader, List<UserInformation> data) {

        Log.d("Test","onLoad");
    }

    @Override
    public void onLoaderReset(Loader<List<UserInformation>> loader) {

    }
}
