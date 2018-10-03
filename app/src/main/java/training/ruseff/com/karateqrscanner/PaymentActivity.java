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
        usernameTextView.setText(user.getName() + " : " + user.getExternalId());
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
                    warningMessageTextView.setText(R.string.no_month_warning);
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

    private void deactivateMonth(int month) {
        cardMonths[month].getCheckBox().setEnabled(false);
        cardMonths[month].getLayout().setBackground(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.payment_month_cell_border_locked));
    }

    private void activateMonth(int month) {
        cardMonths[month].getCheckBox().setEnabled(true);
        cardMonths[month].getLayout().setBackground(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.payment_month_cell_border));
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
        builder.setTitle(R.string.confirm_payment);
        builder.setMessage(getResources().getString(R.string.pay_for) + "\n" + Utils.datesToString(paymentDates));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new MakePayment().execute(paymentDates);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void makeToast(String msg, int textColor) {
        Toast toast = Toast.makeText(PaymentActivity.this, msg, Toast.LENGTH_LONG);
        TextView v = toast.getView().findViewById(android.R.id.message);
        v.setTextColor(textColor);
        v.setGravity(Gravity.CENTER);
        toast.show();
    }

    private class GenerateModelByYearTask extends AsyncTask<Integer, Void, String> {

        int year;

        @Override
        protected String doInBackground(Integer... param) {
            year = param[0];
            HttpCalls http = new HttpCalls();
            return http.getAllPaymentsByUserIdAndYear(user.getInternalId(), year);
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
            boolean success = true;
            for (Date date : param) {
                success = http.makePayment(user.getInternalId(), date).equals(HttpResponse.OK) && success;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            unblockScreen();
            if (result) {
                makeToast(getResources().getString(R.string.successful_payment), Color.GREEN);
                Intent menuActivity = new Intent(PaymentActivity.this, MenuActivity.class);
                PaymentActivity.this.startActivity(menuActivity);
                finish();
            } else {
                makeToast(getResources().getString(R.string.error), Color.RED);
            }
        }

        @Override
        protected void onPreExecute() {
            blockScreen();
        }
    }
}
