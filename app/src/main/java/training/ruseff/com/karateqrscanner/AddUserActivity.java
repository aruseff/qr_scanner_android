package training.ruseff.com.karateqrscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import training.ruseff.com.karateqrscanner.http.HttpCalls;
import training.ruseff.com.karateqrscanner.http.HttpResponse;

public class AddUserActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private QRCodeReaderView qrCodeReaderView;
    RelativeLayout mainLayout;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
    private boolean isQrCodeRead = false;
    private ImageView editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        mainLayout = findViewById(R.id.mainLayout);
        editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddUserPage(-1);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }
    }

    @Override
    public void onQRCodeRead(String userInfo, PointF[] points) {
        if (!isQrCodeRead) {
            getUserByUserInfo(userInfo);
            isQrCodeRead = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isQrCodeRead = false;
        unblockScreen();
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(AddUserActivity.this, new String[]{
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else {
            Snackbar.make(mainLayout, "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void initQRCodeReaderView() {
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.startCamera();
    }

    private void getUserByUserInfo(String userInfo) {
        if (userInfo == null) {
            callErrorPage("Неправилен формат на QR кода");
            return;
        }
        try {
            long id = Long.parseLong(userInfo);
            new AddUserActivity.AddUserTask().execute(id);
        } catch (Exception e) {
            callErrorPage("Неправилен формат на QR кода");
        }
    }

    private void callErrorPage(String message) {
        Intent userNotFoundIntent = new Intent(AddUserActivity.this, ScanErrorPage.class);
        userNotFoundIntent.putExtra("message", message);
        AddUserActivity.this.startActivity(userNotFoundIntent);
    }

    private void callAddUserPage(long id) {
        Intent addUserIntent = new Intent(AddUserActivity.this, AddUserByNameActivity.class);
        addUserIntent.putExtra("userId", id);
        AddUserActivity.this.startActivity(addUserIntent);
    }

    private void blockScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        findViewById(R.id.wholeScreenLayout).setVisibility(View.VISIBLE);
    }

    private void unblockScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        findViewById(R.id.wholeScreenLayout).setVisibility(View.GONE);
    }

    private class AddUserTask extends AsyncTask<Long, Void, String> {
        long id;

        @Override
        protected String doInBackground(Long... param) {
            id = param[0];
            HttpCalls http = new HttpCalls();
            return http.getUserById(param[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals(HttpResponse.CLIENT_ERROR)) {
                callErrorPage("Възникна проблем, моля опитайте отново!");
            } else if (result.equals(HttpResponse.USER_NOT_FOUND)) {
                callAddUserPage(id);
            } else {
                callErrorPage("Трениращ с това ID " + id + " вече съществува!");
            }
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }
    }
}
