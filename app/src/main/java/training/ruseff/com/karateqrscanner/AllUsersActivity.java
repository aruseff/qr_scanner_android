package training.ruseff.com.karateqrscanner;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import training.ruseff.com.karateqrscanner.adapters.UsersListAdapter;
import training.ruseff.com.karateqrscanner.http.HttpCalls;
import training.ruseff.com.karateqrscanner.http.JsonConverter;
import training.ruseff.com.karateqrscanner.models.User;

public class AllUsersActivity extends AppCompatActivity {

    ListView usersListView;
    TextView messageTextView;
    RelativeLayout progressBar;
    RelativeLayout searchBar;
    EditText searchEditText;
    ArrayAdapter<User> userAdapter;

    private int lastVisiblePosition = 0;
    private boolean scrollDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        initFields();
        new HttpAsync().execute();
    }

    private void initFields() {
        usersListView = findViewById(R.id.usersListView);
        messageTextView = findViewById(R.id.messageTextView);
        progressBar = findViewById(R.id.progressBar);
        searchBar = findViewById(R.id.searchBar);
        searchEditText = findViewById(R.id.searchEditText);

        usersListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lastVisiblePosition == firstVisibleItem) {
                    return;
                }
                if (firstVisibleItem > lastVisiblePosition) {
                    // scroll down
                    if (!scrollDown) {
                        if(firstVisibleItem != 0) {
                            hideSearchBar();
                            scrollDown = true;
                        }
                    }
                } else {
                    if (scrollDown) {
                        showSearchBar();
                        scrollDown = false;
                    }
                }
                lastVisiblePosition = firstVisibleItem;
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userAdapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showErrorMessage(String msg, boolean changeColor) {
        messageTextView.setText(msg);
        if (changeColor) {
            messageTextView.setTextColor(Color.RED);
        }
        usersListView.setVisibility(View.GONE);
        messageTextView.setVisibility(View.VISIBLE);
    }

    private void showListView(ArrayList<User> users) {
        if (users == null || users.isEmpty()) {
            showErrorMessage(getResources().getString(R.string.users_not_found_error), false);
        } else {
            userAdapter = new UsersListAdapter(users, AllUsersActivity.this);
            usersListView.setAdapter(userAdapter);
            messageTextView.setVisibility(View.GONE);
            usersListView.setVisibility(View.VISIBLE);
        }
    }

    private void hideSearchBar() {
        searchBar.animate().translationY(-(searchBar.getHeight()*2)).setInterpolator(new AccelerateInterpolator(2)).start();
        usersListView.setPadding(0,0,0,0);
    }

    private void showSearchBar() {
        searchBar.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
        usersListView.setPadding(0,searchBar.getHeight(),0,0);

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

    private class HttpAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... param) {
            HttpCalls http = new HttpCalls();
            return http.getAllUsers();
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }

        @Override
        protected void onPostExecute(String result) {
            unblockScreen();
            if (result.equals("ERROR")) {
                showErrorMessage(getResources().getString(R.string.error), true);
            } else {
                JsonConverter json = new JsonConverter();
                ArrayList<User> users = json.fromStringToListOfUsers(result);
                showListView(users);
            }
        }
    }
}
