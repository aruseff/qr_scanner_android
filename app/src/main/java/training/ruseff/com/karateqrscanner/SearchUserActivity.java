package training.ruseff.com.karateqrscanner;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        new HttpAsync().execute("");
    }

    private class HttpAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... param) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://karateqrscannerdb.000webhostapp.com/users/getAllUsers.php")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response != null && response.body() != null) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("---------------------------------------------");
            System.out.println(result);
            System.out.println("---------------------------------------------");
        }
    }
}
