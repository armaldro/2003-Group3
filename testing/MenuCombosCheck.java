import entity.ComboMeal;
import entity.Menu;
import entity.MenuItem;
import java.util.List;

/**
 * Check for the Stage 3 menu, Menu.withCombos(). Kept in its own file so the
 * Stage 1 MenuCheck is not touched.
 */
public class MenuCombosCheck {

    private static int failures = 0;

    public static void main(String[] args) {
        List<MenuItem> items = Menu.withCombos().getItems();

        // size and order: the standard dishes first, the combo at the END
        check("withCombos() has 4 items", items.size() == 4);
        check("item 1 is Salad, 900 cents, 1 unit", is(items.get(0), "Salad", 900, 1));
        check("item 2 is Burger, 1500 cents, 1 unit", is(items.get(1), "Burger", 1500, 1));
        check("item 3 is Pasta, 1800 cents, 2 units", is(items.get(2), "Pasta", 1800, 2));
        check("item 4 is Salad + Pasta, 2430 cents, 3 units", is(items.get(3), "Salad + Pasta", 2430, 3));
        check("item 4 is a ComboMeal", items.get(3) instanceof ComboMeal);
        check("items 1 to 3 are not combos",
                !(items.get(0) instanceof ComboMeal)
                && !(items.get(1) instanceof ComboMeal)
                && !(items.get(2) instanceof ComboMeal));

        // the first three dishes must match the standard menu exactly
        List<MenuItem> standard = Menu.standard().getItems();
        boolean sameAsStandard = true;
        for (int i = 0; i < standard.size(); i++) {
            sameAsStandard = sameAsStandard
                    && is(items.get(i), standard.get(i).getName(),
                          standard.get(i).getPriceCents(), standard.get(i).getPrepUnits());
        }
        check("the first 3 items match Menu.standard()", sameAsStandard);

        // Stage 1 and 2 must be unaffected: standard() is unchanged
        check("Menu.standard() still has 3 items", standard.size() == 3);
        check("Menu.standard() has no combo", !(standard.get(2) instanceof ComboMeal) && standard.size() == 3);

        // the new menu's list is read-only
        try {
            items.add(items.get(0));
            check("withCombos() list is read-only (add was allowed)", false);
        } catch (UnsupportedOperationException e) {
            check("withCombos() list is read-only", true);
        }

        // the rule: customer n orders item (n - 1) mod (menu size), the same rule as
        // Customer.chooseItem on Rifqi's branch. With 4 items, customer 4 gets the combo.
        check("customer 3 orders Pasta", dishFor(3, items).getName().equals("Pasta"));
        check("customer 4 orders the combo", dishFor(4, items) instanceof ComboMeal);
        check("customer 5 orders Salad again", dishFor(5, items).getName().equals("Salad"));
        check("customer 6 orders Burger", dishFor(6, items).getName().equals("Burger"));

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    /** The documented rule for choosing a dish: customer n picks position (n - 1) mod size. */
    private static MenuItem dishFor(int customerNumber, List<MenuItem> items) {
        return items.get((customerNumber - 1) % items.size());
    }

    private static boolean is(MenuItem item, String name, int priceCents, int prepUnits) {
        return item.getName().equals(name)
                && item.getPriceCents() == priceCents
                && item.getPrepUnits() == prepUnits;
    }

    private static void check(String label, boolean passed) {
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label);
    }
}
