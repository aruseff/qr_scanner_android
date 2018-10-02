package training.ruseff.com.karateqrscanner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import training.ruseff.com.karateqrscanner.http.HttpCalls;
import training.ruseff.com.karateqrscanner.utils.Utils;

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
                hideKeyboard();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(validateForm(username, password)) {
                    new LoginAsync().execute(username, password);
                }
            }
        });
    }

    private boolean validateForm(String username, String password) {
        boolean toContinue = true;
        if(Utils.isNullOrEmpty(username)) {
            usernameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.warning_icon, 0);
            toContinue = false;
        } else {
            usernameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        if(Utils.isNullOrEmpty(password)) {
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.warning_icon, 0);
            toContinue = false;
        } else {
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        return toContinue;
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

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                usernameEditText.setText("");
                passwordEditText.setText("");
                Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
                LoginActivity.this.startActivity(menuActivity);
            } else {
                enter.setText("Неуспешен вход");
                int colorFrom = Color.RED;
                int colorTo = ((ColorDrawable)enter.getBackground()).getColor();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(2000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        enter.setBackgroundColor((int) animator.getAnimatedValue());
                    }
                });
                colorAnimation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        enter.setText("Влез");
                    }
                });
                colorAnimation.start();
            }
        }
    }

}
