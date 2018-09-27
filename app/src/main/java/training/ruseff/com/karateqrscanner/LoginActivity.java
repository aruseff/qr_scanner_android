package training.ruseff.com.karateqrscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import training.ruseff.com.karateqrscanner.http.HttpCalls;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button enter;
    RelativeLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFields();
    }

    private void initFields() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        enter = findViewById(R.id.enter);
        progressBar = findViewById(R.id.progressBar);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO check if empty
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                new LoginAsync().execute(username, password);
            }
        });
    }

    private void blockScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void unblockScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);
    }

    private class LoginAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... param) {
            HttpCalls http = new HttpCalls();
            return http.login(param[0], param[1]);
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }

        @Override
        protected void onPostExecute(String result) {
            unblockScreen();
            if(result.equals("true")) {
                Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
                LoginActivity.this.startActivity(menuActivity);
            } else {

            }
        }
    }
}
