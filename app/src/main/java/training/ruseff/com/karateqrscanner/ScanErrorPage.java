package training.ruseff.com.karateqrscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanErrorPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_error_page);

        final String message = getIntent().getStringExtra("message");
        TextView warningMessage = findViewById(R.id.warningMessage);
        warningMessage.setText(message);

        Button tryAgainButton = findViewById(R.id.tryAgainButton);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
