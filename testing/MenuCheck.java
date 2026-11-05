
import entity.Menu;
import entity.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuCheck {
    
    public static void main(String[] args) {
        
        // Check standard menu contains 3 dishes
        if (Menu.standard().getItems().size() == 3) {
            System.out.println("OK Standard Menu size");
            System.out.println("Expected: 3" + " Actual: " + Menu.standard().getItems().size());
        }

        // check new Menu(null) and new Menu(List.of()) throws
        try {
            Menu illegalMenu = new Menu(null);
            System.out.println("FAIL Able to create a null menu");
        } catch (IllegalArgumentException e) {
            System.out.println("OK null menu throws IllegalArgumentException");
        }

        try {
            Menu illegalMenu2 = new Menu(List.of());
            System.out.println("FAIL Able to create empty menu");
        } catch (IllegalArgumentException e) {
            System.out.println("OK empty menu throws IllegalArgumentException");
        }
        
        // check adding to getItems() throws
        try {
            MenuItem bacon = new MenuItem("Bacon", 1000, 1);
            Menu.standard().getItems().add(0, bacon);
            System.out.println("FAIL able to add items to menu");
        } catch (UnsupportedOperationException e) {
            System.out.println("OK adding to getItems() throws UnsupportedOperationException");
        }

        // check the constructor copies the list
        MenuItem beef = new MenuItem("Beef", 1000, 1);
        MenuItem chicken = new MenuItem("Chicken", 1000, 1);
        MenuItem fish = new MenuItem("Fish", 1000, 1);

        List<MenuItem> source = new ArrayList<>();   // a list the caller CAN change
        source.add(beef);
        source.add(chicken);
        Menu menu = new Menu(source);

        source.add(fish);   // change the caller's list AFTER building the menu

        if (menu.getItems().size() == 2) {
            System.out.println("OK menu is unaffected by later changes to the source list");
        } else {
            System.out.println("FAIL menu changed when the source list changed");
        }
         
    }
}
