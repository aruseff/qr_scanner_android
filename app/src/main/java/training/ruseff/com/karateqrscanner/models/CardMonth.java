package training.ruseff.com.karateqrscanner.models;

import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class CardMonth {

    private RelativeLayout layout;
    private CheckBox checkBox;

    public CardMonth(RelativeLayout layout, CheckBox checkBox) {
        this.layout = layout;
        this.checkBox = checkBox;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
