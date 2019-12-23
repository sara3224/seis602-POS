import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class Helper {
    private static DecimalFormat df2Digit = new DecimalFormat("#.00");
    private static DecimalFormat df4Digit = new DecimalFormat("#.0000");
    private static DecimalFormat df2DigitNoRounding = new DecimalFormat("#.00");

    {
        df2Digit.setMinimumFractionDigits(2);
    }
    public static double roundUp(double dbl) {
        df2Digit.setRoundingMode(RoundingMode.UP);
        return Double.parseDouble(df2Digit.format(dbl));
    }

    public static String digit2Doubles(double dbl) {
        df2Digit.setRoundingMode(RoundingMode.UP);
        return df2Digit.format(dbl);
    }

    public static double roundUp4Digit(double dbl) {
        return Double.parseDouble(df4Digit.format(dbl));
    }

    public static double twoDigitNoRound(double dbl) {
        return Double.parseDouble(df2DigitNoRounding.format(dbl));
    }

    public static boolean isSameDay(Date reportDate2, Date salesDate) {
        Calendar requestedDate = Calendar.getInstance();
        requestedDate.setTime(reportDate2);

        Calendar salesRecordCal = Calendar.getInstance();
        salesRecordCal.setTime(salesDate);

        return requestedDate.get(Calendar.DAY_OF_YEAR) == salesRecordCal.get(Calendar.DAY_OF_YEAR) &&
                requestedDate.get(Calendar.YEAR) == salesRecordCal.get(Calendar.YEAR);
    }
}
