package training.ruseff.com.karateqrscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    final AlphaAnimation buttonClickAnimation = new AlphaAnimation(1F, 0.8F);
    Button scanButton;
    Button allUsersButton;
    Button searchUserButton;
    Button addUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initFields();
        // TODO fix the default focus on buttons
    }

    private void initFields() {
        scanButton = findViewById(R.id.scanButton);
        allUsersButton = findViewById(R.id.allUsersButton);
        searchUserButton = findViewById(R.id.searchButton);
        addUserButton = findViewById(R.id.addUserButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClickAnimation);
                Intent scanActivity = new Intent(MenuActivity.this, ScanActivity.class);
                MenuActivity.this.startActivity(scanActivity);
            }
        });

        allUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClickAnimation);
                Intent allUsersActivity = new Intent(MenuActivity.this, AllUsersActivity.class);
                MenuActivity.this.startActivity(allUsersActivity);
            }
        });

        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClickAnimation);

            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClickAnimation);
                Intent addUserActivity = new Intent(MenuActivity.this, AddUserActivity.class);
                MenuActivity.this.startActivity(addUserActivity);
            }
        });
    }
}
