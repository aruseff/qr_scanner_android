package training.ruseff.com.karateqrscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserNotFoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_not_fount);

        final String userInfo = getIntent().getStringExtra("userInfo");
        TextView notFoundName = findViewById(R.id.notFoundName);
        notFoundName.setText(userInfo);

        Button tryAgainButton = findViewById(R.id.tryAgainButton);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(UserNotFoundActivity.this, ScanActivity.class);
                UserNotFoundActivity.this.startActivity(mainActivity);
                finish();
            }
        });

    }
}
