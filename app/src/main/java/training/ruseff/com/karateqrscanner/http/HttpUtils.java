package training.ruseff.com.karateqrscanner.http;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import training.ruseff.com.karateqrscanner.app.MyApplication;

public class HttpUtils {

    public static boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) MyApplication.getAppContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
