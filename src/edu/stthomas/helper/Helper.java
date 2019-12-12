package edu.stthomas.helper;


import java.math.RoundingMode;
import java.text.DecimalFormat;

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
}
