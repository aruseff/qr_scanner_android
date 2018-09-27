package training.ruseff.com.karateqrscanner.models;

        import java.util.Set;

public class YearModel {

    private int year;
    private Set<Integer> payedMonths;

    public YearModel(int year, Set<Integer> payedMonths) {
        this.year = year;
        this.payedMonths = payedMonths;
    }

    public int getYear() {
        return year;
    }

    public Set<Integer> getPayedMonths() {
        return payedMonths;
    }
}
