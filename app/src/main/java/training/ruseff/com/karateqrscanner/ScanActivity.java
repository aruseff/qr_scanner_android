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
import android.widget.RelativeLayout;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import training.ruseff.com.karateqrscanner.http.HttpCalls;
import training.ruseff.com.karateqrscanner.http.HttpResponse;
import training.ruseff.com.karateqrscanner.http.JsonConverter;
import training.ruseff.com.karateqrscanner.models.User;

public class ScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private QRCodeReaderView qrCodeReaderView;
    RelativeLayout mainLayout;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
    private boolean isQrCodeRead = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mainLayout = findViewById(R.id.mainLayout);

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
                    ActivityCompat.requestPermissions(ScanActivity.this, new String[]{
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
            callErrorPage("");
        } else {
            try {
                long id = Long.parseLong(userInfo.substring(userInfo.lastIndexOf(" ") + 1));
                String name = userInfo.substring(0, userInfo.lastIndexOf(" "));
                new GetUserTask().execute(new User(0, name, id));
            } catch (Exception e) {
                callErrorPage(userInfo);
            }
        }
    }

    private void callErrorPage(String userInfo) {
        Intent userNotFoundIntent = new Intent(ScanActivity.this, UserNotFoundActivity.class);
        userNotFoundIntent.putExtra("userInfo", userInfo);
        ScanActivity.this.startActivity(userNotFoundIntent);
    }

    private void callPaymentPage(User user) {
        Intent paymentIntent = new Intent(ScanActivity.this, PaymentActivity.class);
        paymentIntent.putExtra("user", user);
        ScanActivity.this.startActivity(paymentIntent);
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

    private class GetUserTask extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... param) {
            HttpCalls http = new HttpCalls();
            return http.getUserByIdAndName(param[0].getExternalId(), param[0].getName());
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals(HttpResponse.CLIENT_ERROR) || result.equals(HttpResponse.USER_NOT_FOUND)) {
                callErrorPage("");
            } else {
                JsonConverter json = new JsonConverter();
                User user = json.fromStringToUser(result);
                callPaymentPage(user);
            }
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }
    }
}
