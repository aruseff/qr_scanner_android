package training.ruseff.com.karateqrscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import training.ruseff.com.karateqrscanner.http.HttpCalls;
import training.ruseff.com.karateqrscanner.http.HttpResponse;
import training.ruseff.com.karateqrscanner.http.JsonConverter;
import training.ruseff.com.karateqrscanner.models.CardMonth;
import training.ruseff.com.karateqrscanner.models.User;
import training.ruseff.com.karateqrscanner.models.YearModel;
import training.ruseff.com.karateqrscanner.utils.Utils;

public class PaymentActivity extends AppCompatActivity {

    TextView usernameTextView;
    TextView yearTextView;
    ImageButton previousYearButton;
    ImageButton nextYearButton;
    CardMonth[] cardMonths;
    TextView warningMessageTextView;
    Button payButton;

    User user;
    Map<Integer, YearModel> models = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        user = getIntent().getParcelableExtra("user");
        checkForNullUser(user);
        initializeFields();
        usernameTextView.setText(user.getName() + " : " + user.getInternalId());
        warningMessageTextView.setVisibility(View.GONE);

        onYearChanged(Utils.getYearFromDate(new Date()));

        previousYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(yearTextView.getText().toString());
                year--;
                yearTextView.setText(String.valueOf(year));
                onYearChanged(year);
            }
        });

        nextYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(yearTextView.getText().toString());
                year++;
                yearTextView.setText(String.valueOf(year));
                onYearChanged(year);
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warningMessageTextView.setVisibility(View.GONE);
                Date[] datesToPay = getPaymentDates();
                if (datesToPay.length != 0) {
                    createConfirmDialog(datesToPay);
                } else {
                    warningMessageTextView.setText("Моле изберете месец за плащане");
                    warningMessageTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public Date[] getPaymentDates() {
        List<Date> dates = new ArrayList<>();
        int year = Integer.parseInt(yearTextView.getText().toString());
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < cardMonths.length; i++) {
            if (cardMonths[i].getCheckBox().isChecked()) {
                calendar.set(year, i, 1);
                dates.add(calendar.getTime());
            }
        }
        return dates.toArray(new Date[0]);
    }

    private void onYearChanged(int year) {
        YearModel model = models.get(year);
        if (model == null) {
            new GenerateModelByYearTask().execute(year);
        } else {
            visualizeByModel(model);
        }
    }

    private void visualizeByModel(YearModel model) {
        yearTextView.setText(String.valueOf(model.getYear()));
        for (int i = 0; i < 12; i++) {
            if (model.getPayedMonths().contains(i)) {
                deactivateMonth(i);
            } else {
                activateMonth(i);
            }
            cardMonths[i].getCheckBox().setChecked(false);
        }
    }

    private Set<Integer> extractPayedMonths(List<Date> payedDates) {
        Set<Integer> payedMonths = new HashSet<>();
        for (Date date : payedDates) {
            int month = Utils.getMonthFromDate(date);
            payedMonths.add(month);
        }
        return payedMonths;
    }

    private void deactivateMonth(int month) {
        cardMonths[month].getCheckBox().setEnabled(false);
        cardMonths[month].getLayout().setBackground(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.month_cell_border_locked));
    }

    private void activateMonth(int month) {
        cardMonths[month].getCheckBox().setEnabled(true);
        cardMonths[month].getLayout().setBackground(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.month_cell_border));
    }

    private void initializeFields() {
        usernameTextView = findViewById(R.id.username);
        yearTextView = findViewById(R.id.year);
        previousYearButton = findViewById(R.id.previousYear);
        nextYearButton = findViewById(R.id.nextYear);
        cardMonths = new CardMonth[12];
        cardMonths[0] = new CardMonth((RelativeLayout) findViewById(R.id.january), (CheckBox) findViewById(R.id.januaryCheck));
        cardMonths[1] = new CardMonth((RelativeLayout) findViewById(R.id.february), (CheckBox) findViewById(R.id.februaryCheck));
        cardMonths[2] = new CardMonth((RelativeLayout) findViewById(R.id.march), (CheckBox) findViewById(R.id.marchCheck));
        cardMonths[3] = new CardMonth((RelativeLayout) findViewById(R.id.april), (CheckBox) findViewById(R.id.aprilCheck));
        cardMonths[4] = new CardMonth((RelativeLayout) findViewById(R.id.may), (CheckBox) findViewById(R.id.mayCheck));
        cardMonths[5] = new CardMonth((RelativeLayout) findViewById(R.id.june), (CheckBox) findViewById(R.id.juneCheck));
        cardMonths[6] = new CardMonth((RelativeLayout) findViewById(R.id.july), (CheckBox) findViewById(R.id.julyCheck));
        cardMonths[7] = new CardMonth((RelativeLayout) findViewById(R.id.august), (CheckBox) findViewById(R.id.augustCheck));
        cardMonths[8] = new CardMonth((RelativeLayout) findViewById(R.id.september), (CheckBox) findViewById(R.id.septemberCheck));
        cardMonths[9] = new CardMonth((RelativeLayout) findViewById(R.id.october), (CheckBox) findViewById(R.id.octoberCheck));
        cardMonths[10] = new CardMonth((RelativeLayout) findViewById(R.id.november), (CheckBox) findViewById(R.id.novemberCheck));
        cardMonths[11] = new CardMonth((RelativeLayout) findViewById(R.id.december), (CheckBox) findViewById(R.id.decemberCheck));
        warningMessageTextView = findViewById(R.id.warningMessage);
        payButton = findViewById(R.id.payButton);
    }

    private void checkForNullUser(User user) {
        if (!Utils.isUserValid(user)) {
            Intent userNotFoundIntent = new Intent(PaymentActivity.this, UserNotFoundActivity.class);
            userNotFoundIntent.putExtra("userInfo", "");
            PaymentActivity.this.startActivity(userNotFoundIntent);
            finish();
        }
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

    private void createConfirmDialog(final Date[] paymentDates) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Потвърждаване за плащане");
        builder.setMessage("Плащане за:\n" + Utils.datesToString(paymentDates));
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new MakePayment().execute(paymentDates);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Не", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private class GenerateModelByYearTask extends AsyncTask<Integer, Void, String> {

        int year;

        @Override
        protected String doInBackground(Integer... param) {
            year = param[0];
            HttpCalls http = new HttpCalls();
            return http.getAllPaymentsByUserIdAndYear(user.getExternalId(), year);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.equals(HttpResponse.CLIENT_ERROR)) {
                JsonConverter json = new JsonConverter();
                Set<Integer> payedMonths = json.fromStringToSetOfNumbers(result);
                YearModel newModel = new YearModel(year, payedMonths);
                models.put(year, newModel);
                visualizeByModel(newModel);
                unblockScreen();
            } else {
                // TODO connection problems
            }
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }

    }

    private class MakePayment extends AsyncTask<Date, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Date... param) {
            HttpCalls http = new HttpCalls();
            return http.makePayment(user.getExternalId(), param[0]).equals(HttpResponse.OK);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            unblockScreen();
            if (result.booleanValue()) {
                Toast toast = Toast.makeText(PaymentActivity.this, "Успешно заплащане!", Toast.LENGTH_LONG);
                TextView v = toast.getView().findViewById(android.R.id.message);
                v.setTextColor(Color.GREEN);
                v.setGravity(Gravity.CENTER);
                toast.show();
                Intent mainActivity = new Intent(PaymentActivity.this, ScanActivity.class);
                PaymentActivity.this.startActivity(mainActivity);
                finish();
            } else {
                Toast toast = Toast.makeText(PaymentActivity.this, "Възникна грешка при заплащането! Моля, опитайте по-късно.", Toast.LENGTH_LONG);
                TextView v = toast.getView().findViewById(android.R.id.message);
                v.setTextColor(Color.RED);
                v.setGravity(Gravity.CENTER);
                toast.show();
            }
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
