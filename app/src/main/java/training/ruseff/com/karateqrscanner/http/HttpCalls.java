package training.ruseff.com.karateqrscanner.http;

import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import training.ruseff.com.karateqrscanner.models.User;
import training.ruseff.com.karateqrscanner.utils.Utils;

public class HttpCalls {

    private final String BASE_URL = "https://karateqrscannerdb.000webhostapp.com";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String addUser(String externalId, String name) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, "{\"externalId\":\"" + externalId + "\",\"name\":\"" + name + "\"}");
        Request request = new Request.Builder()
                .url(BASE_URL + "/users/addUser.php")
                .post(body)
                .build();
        String result = "ERROR";
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // nothing
        }
        return result;
    }

    public String getAllUsers() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/users/getAllUsers.php")
                .build();
        String result = "ERROR";
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // nothing
        }
        return result;
    }

    public String getUserByIdAndName(long externalId, String name) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/users/getUserByIdAndName.php?id=" + externalId + "&name=" + name)
                .build();
        String result = HttpResponse.CLIENT_ERROR;
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // nothing
        }
        return result;
    }

    public String getAllPaymentsByUserIdAndYear(long userId, int year) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/payments/getAllPaymentsByUserIdAndYear.php?userId=" + userId + "&year=" + year)
                .build();
        String result = HttpResponse.CLIENT_ERROR;
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // nothing
        }
        return result;
    }

    public String makePayment(long userId, Date date) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, "{\"userId\":\"" + userId + "\",\"forMonth\":\"" + Utils.getMonthFromDate(date) + "\",\"forYear\":\"" + Utils.getYearFromDate(date) + "\"}");
        Request request = new Request.Builder()
                .url(BASE_URL + "/payments/addPayment.php")
                .post(body)
                .build();
        String result = HttpResponse.CLIENT_ERROR;
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // nothing
        }
        return result;
    }

    public String login(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/admin/login.php?username=" + username + "&password=" + password)
                .build();
        String result = HttpResponse.CLIENT_ERROR;
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                result = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // nothing
        }
        return result;
    }
}
