
import util.Money;

public class MoneyCheck {
    public static void main(String[] args) {

        // test format method
        int[] inputs = {0, 5, 50, 100, 1080, -5, -50, -108000};
        String[] expected = {"$0.00", "$0.05", "$0.50", "$1.00", "$10.80", "-$0.05", "-$0.50", "-$1080.00"};

        for (int i = 0; i < inputs.length; i++) {
            String actual = Money.format(inputs[i]);
            String result = actual.equals(expected[i]) ? "OK " : "FAIL";
            System.out.println(result + " format(" + inputs[i] + ") = " + actual + " (expected " + expected[i] + ")");
        }

        System.out.println("----------------------------------------------------");

        // test percentOf method
                // test percentOf method
        int[] centsInputs = {1999, 1800, 1000, 1000, 5, 1, 2700, 1500, 1500};
        int[] percentages = {15, 10, 50, 25, 50, 50, 90, 100, 0};
        int[] percentExpected = {300, 180, 500, 250, 3, 1, 2430, 1500, 0};

        for (int i = 0; i < centsInputs.length; i++) {
            int actual = Money.percentOf(centsInputs[i], percentages[i]);
            String result = actual == percentExpected[i] ? "OK " : "FAIL";

            System.out.println(result + " percentOf(" + centsInputs[i] + ", "
                               + percentages[i] + ") = " + actual
                               + " (expected " + percentExpected[i] + ")");
        }

        // test chained discounts: VIP (90%) then Happy Hour (80%) on a $15.00 Burger
        int vipPrice = Money.percentOf(1500, 90);
        int happyHourPrice = Money.percentOf(vipPrice, 80);
        String chainedResult = happyHourPrice == 1080 ? "OK " : "FAIL";
        System.out.println(chainedResult + " VIP then Happy Hour on 1500 = " + happyHourPrice
                           + " (expected 1080)");

        // test percentOf rejects a negative percentage
        try {
            Money.percentOf(1500, -1);
            System.out.println("FAIL able to use a negative percentage");
        } catch (IllegalArgumentException e) {
            System.out.println("OK negative percentage throws IllegalArgumentException");
        }

        // test percentOf rejects a percentage above 100
        try {
            Money.percentOf(1500, 101);
            System.out.println("FAIL able to use a percentage above 100");
        } catch (IllegalArgumentException e) {
            System.out.println("OK percentage above 100 throws IllegalArgumentException");
        }

        // test percentOf rejects a negative amount
        try {
            Money.percentOf(-1, 50);
            System.out.println("FAIL able to use a negative amount");
        } catch (IllegalArgumentException e) {
            System.out.println("OK negative amount throws IllegalArgumentException");
        }
    }
}
