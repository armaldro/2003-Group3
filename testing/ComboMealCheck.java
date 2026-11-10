import entity.ComboMeal;
import entity.MenuItem;
import entity.Order;
import enums.OrderStatus;

public class ComboMealCheck {

    private static int failures = 0;

    public static void main(String[] args) {
        MenuItem salad = new MenuItem("Salad", 900, 1);
        MenuItem burger = new MenuItem("Burger", 1500, 1);
        MenuItem pasta = new MenuItem("Pasta", 1800, 2);

        // the brief's example: Salad + Pasta = $24.30 and 3 units
        ComboMeal saladPasta = new ComboMeal(salad, pasta);
        check("Salad + Pasta: name", saladPasta.getName().equals("Salad + Pasta"));
        check("Salad + Pasta: price is 2430 cents", saladPasta.getPriceCents() == 2430);
        check("Salad + Pasta: 3 prep units", saladPasta.getPrepUnits() == 3);

        // a different pair: Burger + Salad = 90% of 2400 = 2160 cents, 2 units
        ComboMeal burgerSalad = new ComboMeal(burger, salad);
        check("Burger + Salad: price is 2160 cents", burgerSalad.getPriceCents() == 2160);
        check("Burger + Salad: 2 prep units", burgerSalad.getPrepUnits() == 2);

        // the same dish twice is allowed: 90% of 1800 = 1620 cents, 2 units
        ComboMeal saladSalad = new ComboMeal(salad, salad);
        check("Salad + Salad: accepted, price is 1620 cents", saladSalad.getPriceCents() == 1620);
        check("Salad + Salad: 2 prep units", saladSalad.getPrepUnits() == 2);

        // the components are kept
        check("getFirst returns the first component", saladPasta.getFirst() == salad);
        check("getSecond returns the second component", saladPasta.getSecond() == pasta);

        // invalid components
        checkRejected("null first component", null, salad);
        checkRejected("null second component", salad, null);
        checkRejected("both components null", null, null);
        checkRejected("a combo as the first component", saladPasta, salad);
        checkRejected("a combo as the second component", salad, saladPasta);

        // a combo works anywhere a MenuItem does: it can be ordered and cooked
        Order order = new Order(saladPasta, saladPasta.getPriceCents());
        check("a combo can be ordered (price locked at 2430)", order.getPriceCents() == 2430);
        order.cookOneUnit();
        order.cookOneUnit();
        check("combo order is NOT_READY after 2 of 3 units", order.getStatus() == OrderStatus.NOT_READY);
        order.cookOneUnit();
        check("combo order is READY after 3 of 3 units", order.getStatus() == OrderStatus.READY);

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    /** Expects building a combo from these components to throw IllegalArgumentException. */
    private static void checkRejected(String label, MenuItem first, MenuItem second) {
        try {
            new ComboMeal(first, second);
            check("reject " + label + " (no exception thrown)", false);
        } catch (IllegalArgumentException e) {
            check("reject " + label + " -> " + e.getMessage(), true);
        }
    }

    private static void check(String label, boolean passed) {
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label);
    }
}