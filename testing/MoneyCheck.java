
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
        int[] centsInputs = {1999, 1800, 1000, 1000, 5, 1};
        int[] percentages = {15, 10, 50, 25, 50, 50};
        int[] percentExpected = {300, 180, 500, 250, 3, 1};

        for (int i = 0; i < centsInputs.length; i++) {
            int actual = Money.percentOf(centsInputs[i], percentages[i]);
            String result = actual == percentExpected[i] ? "OK " : "FAIL";

            System.out.println(result + " percentOf(" + centsInputs[i] + ", "
                               + percentages[i] + ") = " + actual
                               + " (expected " + percentExpected[i] + ")");
        }
    }
}
