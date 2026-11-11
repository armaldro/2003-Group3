# Design Discussion: Danial

Scope: Money, MenuItem, Menu, ComboMeal, Order, OrderStatus, Chef, Cashier,
PricingPolicy. (Customers, Waiter, Host, Table and the controller are covered
by teammates.)

## 1. Key abstractions and their responsibilities

| Class | Responsibility | Collaborates with | Stage |   
| ----- | -------------- | ----------------- | ----- |
| Chef | Does one preparation unit on an order it is given. Holds no state. | Order, controller | 1 |
| Money | Converts an amount in cents to a dollar string, and works out a percentage of an amount, rounded to the nearest cent. Rejects a negative amount and a percentage outside 0 to 100. Holds no state or pricing rules, and does not know where its output is shown. | ComboMeal, PricingPolicy (Stage 3), VIPCustomer (Stage 2) | 1 |
| MenuItem | Holds a dish's name, price in cents and preparation units, which cannot change once created. Rejects a blank name, a price of 0 or less, and fewer than 1 preparation unit. Does not know how it is displayed or ordered. | Menu, ComboMeal, Order | 1 |
| Menu | Owns the list of dishes customers choose from and hands out a read-only copy, so callers cannot change the menu. Stage 3 adds withCombos(), which appends one combo. Does not decide which dish a customer orders. | Restaurant, MenuItem, ComboMeal | 1, 3 |
| ComboMeal | A menu item made of two other menu items: its price is 90% of their combined price and its preparation units are their sum. Rejects a null component and a combo as a component. Does not change how Order or Chef treat a dish. | extends MenuItem, calls Money, built by Menu | 3 |
| Order | Holds one dish, its locked price, cooking progress and status, and guards its own status changes (cook, serve, pay, cancel). Rejects a null dish and a price of 0 or less. Does not know its customer. | MenuItem, OrderStatus, Chef, Cashier, Waiter; held by Customer | 1 |
| OrderStatus | Lists the stages an order passes through and gives each a display label. Does not decide when an order changes stage; Order does. | Order, Restaurant, Waiter | 1 |
| PricingPolicy | Holds the percentage each pricing rule charges (normal 100%, Happy Hour 80%) and works out an order's price. Does not decide when Happy Hour is active; the controller passes that in. | Money, Waiter or controller | 3 |
| Cashier | Takes payment from at most one customer per call: the one who has waited longest, and never one who became ready this turn. Returns the customer who paid, or null. Holds no state, and leaves revenue and freeing the table to the controller (to be confirmed). | abstract Customer, Order, controller | 1 |

[1]

## 2. Design principles, only where the code genuinely shows them

For each one: name the principle, point to the exact class or method, and say
what would have gone wrong without it. Leave out any that do not apply.

- **SRP:** Single Responsibility Principle. PricingPolicy class. Initially supposed to be a method of Money class, adding it there would caused Money to change for display and for promotions - more than one responsibility. With PricingPolicy as a separate class, changing the Happy Hour rate touches only PricingPolicy class.
- **OCP:** Open-Closed Principle. ComboMeal class. Closed: adding combos didn't require changing MenuItem, Order or Chef. Open: ComboMeal extends MenuItem and supplies its own computed name, price and units. [2] ComboMealCheck puts a combo into an Order and cooks it through NOT_READY to READY, with no change to Order.
- **LSP:** Liskov Substitution Principle. ComboMealCheck. ComboMealCheck builds new Order(saladPasta, saladPasta.getPriceCents()), cooks it three times and gets NOT_READY then READY. Order didn’t need to know it was a combo.
- **ISP / DIP:** not Applicable, no interface used. 

## 3. Trade-offs and one alternative design

- **Trade-offs I accepted** : items in menuItem do not need their own subclass. With this decision, a dish cannot have its own unique behaviour. However, this makes it easier to create new items, just input different name, price and prepUnit as a parameter of menuItem class call.  
- **Alternative considered:** : Create a subclass for each item in menuItem. Rejected because this will create empty classes with no behaviour of their own and have more files to maintain especially when creating custom menu items. [3]

## 4. How the player's choices change the result

**A combo on the menu (ComboMeal, Menu.withCombos)**: With one combo at the end of the menu, customer 4 orders it. it's $24.30 but needs 3 units, more than any other dish, so it takes up the chef for three turns. [4] Choosing to cook it first can delay the others. Menu class creates a list of menu items and Rifqi's customer class choose to order from it based on the index of the menu item. e.g. customer 1 orders menu item at index 0 (e.g pasta) and customer 2 orders the next menu item in the list.

## 5. Open points with teammates

- **Decision**: who enforces “READY this turn, served next turn”. **Agreed with**: Teo. **Outcome**: the controller does, so Order and Chef stay unaware of turns.
- **Decision**: who decides what a customer orders? **Agreed with**: Rifqi. **Outcome**: Customer orders based on index of menuItem list, so Menu doesn't have to know which item is being ordered by the customer.

## Acknowledgement of AI assistance

- **Tool**: Claude Code (Claude Sonnet 5.5). Full conversation in gai-paper-trail/danial-claude-log.md.
- **Generated by the AI**: Skeletons with TODOs of OrderStatus, Order, Cashier, ComboMeal, PricingPolicy, and Menu.withCombos() method. Check files for MenuItemCheck, MenuCheck, OrderCheck, ChefCheck, CashierCheck, ComboMealCheck, PricingPolicyCheck, MenuCombosCheck.
- **Explanation and review**: SOLID principles, immutability, and final, copying lists defensively, enums, instanceof, why PricingPolicy is separate from money and code review of my classes.
- **Written by me**: all method logic in Money, MenuItem, Menu, Order, Chef, Cashier, ComboMeal and PricingPolicy, and the design decisions.
- **How I checked it**: I ran each check, and removed or broke a rule on purpose to confirm it printed FAIL.


1. Claude helped generate the table and the wording of each row
2. Claude suggested framing ComboMeal as an OCP example and explained the caveat that Menu was extended.
3. Claude explained that a proper trade-off and alternative example should include the cost of the trade-off and alternative. 
4. Claude suggested using specific examples to explain how a player's choice affect the result
