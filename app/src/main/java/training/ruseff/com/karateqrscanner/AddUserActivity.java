package training.ruseff.com.karateqrscanner;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import training.ruseff.com.karateqrscanner.http.HttpCalls;
import training.ruseff.com.karateqrscanner.utils.Utils;

public class AddUserActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText idEditText;
    Button registerButton;
    RelativeLayout progressBar;
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        initFields();
    }

    private void initFields() {
        nameEditText = findViewById(R.id.nameEditText);
        idEditText = findViewById(R.id.idEditText);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        messageTextView = findViewById(R.id.messageTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageTextView.setVisibility(View.GONE);
                String externalId = idEditText.getText().toString();
                String name = nameEditText.getText().toString();
                if(Utils.isNullOrEmpty(externalId) || Utils.isNullOrEmpty(name)) {
                    fieldValidationError();
                } else {
                    new HttpAsync().execute(externalId, name);
                }
            }
        });
    }

    private void fieldValidationError() {
        showErrorMessage("Моля попълнете полетата");
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

    private void showErrorMessage(String s) {
        messageTextView.setText(s);
        messageTextView.setTextColor(Color.RED);
        messageTextView.setVisibility(View.VISIBLE);
    }

    private void showSuccessMessage(String s) {
        messageTextView.setText(s);
        messageTextView.setTextColor(Color.GREEN);
        messageTextView.setVisibility(View.VISIBLE);
    }

    private class HttpAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... param) {
            HttpCalls http = new HttpCalls();
            return http.addUser(param[0], param[1]);
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }

        @Override
        protected void onPostExecute(String result) {
            unblockScreen();
            if(result.equals("OK")) {
                showSuccessMessage("Успешно добавен потребител");
            } else {
                showErrorMessage("Възникна проблем. Моля опитайте отново");
            }
        }
    }
}
