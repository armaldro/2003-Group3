
import util.Money;

public class MoneyCheck {
    public static void main(String[] args) {
        int[] inputs = {0, 5, 50, 100, 1080, -5, -50, -108000};
        String[] expected = {"$0.00", "$0.05", "$0.50", "$1.00", "$10.80", "-$0.05", "-$0.50", "-$1080.00"};

        for (int i = 0; i < inputs.length; i++) {
            String actual = Money.format(inputs[i]);
            String result = actual.equals(expected[i]) ? "OK " : "FAIL";
            System.out.println(result + " format(" + inputs[i] + ") = " + actual + " (expected " + expected[i] + ")");
        }
    }
}
