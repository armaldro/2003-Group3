
import entity.MenuItem;

public class MenuItemCheck {
    public static void main(String[] args) {
        String[] names = {"burger", null, " ", "pasta", "salad"};
        int[] pricesCents = {500, 10, 20, -2, 10};
        int[] preps = {1, 2, 3, 4, 0};
        boolean[] shouldSucceed = {true, false, false, false, false};

        for (int i = 0; i < names.length; i++) {
            boolean result = testItem(names[i], pricesCents[i], preps[i]);
            if (result == shouldSucceed[i]) {
                System.out.println("OK " + "Row " + (i + 1) + " Expected: " + shouldSucceed[i] + " Actual: " + result);
                
                System.out.println("---------------------------------");
            } else {
                System.out.println("FAIL" + "Row " + (i + 1) + " Expected: "+ shouldSucceed[i] + " Actual: " + result);
                System.exit(1);
            }
        }

    }

    public static boolean testItem(String name, int priceCents, int prepUnits) {

        try {
            MenuItem dish = new MenuItem(name, priceCents, prepUnits);
            System.out.println("Item created! " + "Expected name=" + dish.getName() + " Expected priceCents=" + dish.getPriceCents() + " Expected prepUnits=" + dish.getPrepUnits());

            return true;
        } catch (IllegalArgumentException e) {
            System.out.print("caught: ");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
