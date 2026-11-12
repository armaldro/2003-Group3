import entity.CriticCustomer;
import entity.Customer;
import entity.Menu;
import entity.MenuItem;
import entity.RegularCustomer;
import entity.VIPCustomer;
import java.util.List;
 
/**
 * Checks the Stage 2 customers. Each one is held in a Customer variable, so
 * the different behaviour comes from the object itself (polymorphism), not
 * from type checks.
 */
public class VIPCriticCheck {
 
    private static int failures = 0;
 
    public static void main(String[] args) {
        Menu menu = Menu.standard();
 
        // VIP: loses 5 per turn, 10% off, same food rule as a Regular
        Customer vip = new VIPCustomer(2);
        check("VIP loses 5 per turn", vip.getSatisfactionLoss(), 5);
        vip.loseSatisfaction();
        check("VIP: 100 - 5 = 95", vip.getSatisfaction(), 95);
        check("VIP pays $13.50 for a $15 Burger", vip.applyDiscount(1500), 1350);
        check("VIP pays $8.10 for a $9 Salad", vip.applyDiscount(900), 810);
        check("VIP pays $16.20 for an $18 Pasta", vip.applyDiscount(1800), 1620);
        check("VIP customer 2 orders Burger (same rule as Regular)", vip.chooseItem(menu).getName(), "Burger");
 
        // Critic: loses 12 per turn, full price, always the slowest dish
        Customer critic = new CriticCustomer(1);
        check("Critic loses 12 per turn", critic.getSatisfactionLoss(), 12);
        for (int i = 0; i < 9; i++) {
            critic.loseSatisfaction();
        }
        check("Critic stops at 0 after 9 turns (9 x 12 = 108)", critic.getSatisfaction(), 0);
        check("Critic pays full price for an $18 Pasta", critic.applyDiscount(1800), 1800);
        for (int id = 1; id <= 3; id++) {
            check("Critic " + id + " orders Pasta, the slowest dish",
                    new CriticCustomer(id).chooseItem(menu).getName(), "Pasta");
        }
 
        // the twist shows on turn 10: customer 4 would order Salad as a Regular, but a Critic orders Pasta
        check("Regular customer 4 orders Salad", new RegularCustomer(4).chooseItem(menu).getName(), "Salad");
        check("Critic customer 4 still orders Pasta", new CriticCustomer(4).chooseItem(menu).getName(), "Pasta");

        // tie: two dishes with the same, highest prep units -> the first one listed
        Menu tied = new Menu(List.of(new MenuItem("Soup", 800, 2), new MenuItem("Steak", 2500, 2),
                new MenuItem("Toast", 400, 1)));
        check("Critic picks the first of two equally slow dishes", new CriticCustomer(1).chooseItem(tied).getName(),
                "Soup");
 
        // the same call gives each type's own answer
        Customer[] customers = {new RegularCustomer(1), new VIPCustomer(2), new CriticCustomer(3)};
        int[] losses = {8, 5, 12};
        int[] burgerPrices = {1500, 1350, 1500};
        String[] names = {"Regular", "VIP", "Critic"};
        for (int i = 0; i < customers.length; i++) {
            String type = customers[i].getClass().getSimpleName();
            check(type + ".getSatisfactionLoss() through a Customer variable",
                    customers[i].getSatisfactionLoss(), losses[i]);
            check(type + ".applyDiscount(1500) through a Customer variable",
                    customers[i].applyDiscount(1500), burgerPrices[i]);
            check(type + ".getTypeName() through a Customer variable",
                    customers[i].getTypeName(), names[i]);
        }
 
        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }
 
    /** Compares an actual value with the expected one and prints OK or FAIL. */
    private static void check(String label, Object actual, Object expected) {
        boolean passed = expected.equals(actual);
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label
                + (passed ? "" : " (expected " + expected + ", actual " + actual + ")"));
    }
}
