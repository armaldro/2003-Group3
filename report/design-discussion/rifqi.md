# Design Discussion: Rifqi

Scope: Customer, RegularCustomer, VIPCustomer, CriticCustomer, CustomerState,
and the UML class diagram. (Menu, Order, Chef, Cashier, Waiter, Table,
Restaurant and the controller are covered by teammates.)

## 1. Key abstractions and their responsibilities

| Class | Responsibility | Collaborates with | Stage |
| ----- | -------------- | ----------------- | ----- |
| Customer (abstract) | Holds what every customer has: an id, satisfaction from 0 to 100, a state, a table, an order, the turn they were served and the turn they became ready to pay. Changes these only through one method per game event (`seat`, `placeOrder`, `markServed`, `markReadyToPay`, `markPaid`, `leave`). Applies its own waiting-turn loss, never going below 0, and picks its own dish. Does not decide when events happen, free tables, cancel orders or count revenue. | Table, Order, Menu, MenuItem, CustomerState; used by Restaurant, Waiter, Cashier and GameController | 1 (`recoverSatisfaction` in 3) |
| RegularCustomer | Loses 8 satisfaction per waiting turn and pays full price. Everything else comes from Customer. | Customer | 1 |
| VIPCustomer | Loses 5 per waiting turn and pays 10% less, rounded to the nearest cent through `Money.percentOf`. | Customer, Money | 2 |
| CriticCustomer | Loses 12 per waiting turn and pays full price. Twist: always orders the dish with the most preparation units (the first one listed if two tie), so with the standard menu a Critic always orders Pasta. | Customer, Menu, MenuItem | 2 |
| CustomerState | Lists the seven stages a customer passes through: WAITING, SEATED, ORDERED, EATING, READY_TO_PAY, PAID and LEFT. Does not change a customer's state; Customer does. | Customer | 1 |

## 2. Design principles, only where the code genuinely shows them

- **OCP (Open/Closed):** `Customer.getSatisfactionLoss()` is abstract, and
  `applyDiscount()` and `chooseItem()` can be overridden. Adding the VIP and
  the Critic in Stage 2 meant two new classes. GameController, Waiter and
  Cashier were not changed: the controller already called
  `customer.loseSatisfaction()`, `customer.chooseItem(menu)` and
  `customer.applyDiscount(price)`, and each object gives its own answer. The
  only change to Customer in Stage 2 was Javadoc. In Stage 3, `chooseItem`
  already worked with the combo menu because it uses the menu's size, not a
  fixed 3.
- **LSP (Liskov Substitution):** any subclass can stand in for a Customer.
  All three follow the same order and service flow and the same state
  changes; an override only changes a number (the loss, the price) or which
  dish is picked, and still returns a dish from the menu and a price no higher
  than the menu price. `VIPCriticCheck` holds all three types in `Customer`
  variables and gets 8/5/12 and 1500/1350/1500 cents from the same calls.
- **SRP (Single Responsibility):** Customer only looks after its own data.
  `leave()` only sets the state to LEFT: cancelling the order, freeing the
  table and removing the customer are done by Order, Table and the controller.
  Without this, Customer would need references to the Restaurant and would
  change whenever the payment or seating rules changed.
- **ISP (Interface Segregation):** not applicable. There are no interfaces in
  my classes, and the brief warns against adding interfaces only to show a
  principle.
- **DIP (Dependency Inversion):** partly. The turn loop works with the
  abstract `Customer` type and never checks which kind of customer it has
  (there is no `instanceof` in the project). Only the arrival code names a
  concrete class, because that is where a customer is created.

## 3. Trade-offs and one alternative design

- **Loss logic written once in the parent.** `loseSatisfaction()` lives in
  Customer and asks the subclass for the amount through
  `getSatisfactionLoss()`, so the "never below 0" rule exists in one place.
  The cost: a subclass can only change how much it loses, not how the loss
  works (for example, losing more after five turns) without overriding the
  whole method.
- **`applyDiscount()` has a default instead of being abstract.** Regular and
  Critic both pay full price, so the default in Customer avoids writing the
  same method twice. The cost: a new customer type that forgets to override it
  silently pays full price, and the compiler will not warn about it.
- **The food rule uses the customer's id.** Customer n orders item
  (n - 1) mod menu size. Runs are reproducible, which the brief requires, but
  customers do not react to prices, Happy Hour or how busy the kitchen is.
- **Alternative considered:** one `Customer` class with a `CustomerType` enum
  and a `switch` on the type in `getSatisfactionLoss()`, `applyDiscount()` and
  `chooseItem()`. Rejected because every new type would mean editing several
  switches in one class, which is the chain of type checks the brief rules
  out. With subclasses, each type's rules sit in one small file.
- **Alternative Critic twist considered:** a Critic who gains satisfaction
  when served quickly. Rejected because the controller would need to call a
  new method when serving, while "orders the slowest dish" only overrides
  `chooseItem()` and changes no one else's code.

## 4. How the player's choices change the result

Each customer type punishes waiting differently. A Critic loses 12 per
waiting turn and leaves after 9 unserved turns (9 x 12 = 108), a Regular after
13 (13 x 8 = 104), and a VIP only after 20 (20 x 5 = 100), which is longer
than the 18-turn game, so a VIP never walks out. The Critic also orders Pasta,
which takes the Chef 2 turns. So seating, ordering and cooking for the Critic
first protects both the paid-customer target (4) and the average satisfaction
target (60), while the VIP can safely wait a little longer.

Serving a VIP earns less: a Burger brings in $13.50 instead of $15.00, so with
a VIP in the run the $45 revenue target needs slightly more sales.

Happy Hour (Stage 3) gives every customer still waiting for a table or food
+15 at the start of each of its two turns, capped at 100. A Regular who has
waited two turns goes from 84 to 99. It helps most when switched on while
several customers are waiting, but every order taken during it is 20% cheaper,
so it trades revenue for satisfaction.

Satisfaction only drops while a customer is unserved, so the value recorded
at payment is the value when their dish arrived. Serving quickly is the only
way to raise the average.

## 5. Open points with teammates

- **Arrivals for Stage 2** (Soon How): the controller still creates only
  RegularCustomer. Proposed: a VIP on turn 4 and a Critic on turn 10, still
  six arrivals.
- **Happy Hour recovery** (Soon How): at the start of each Happy Hour turn, call
  `customer.recoverSatisfaction(15)` for customers where `isUnserved` is true.
- **README:** document the food rule and the Critic twist.
- **UML:** Chef and Cashier do not extend Staff, and Restaurant does not hold
  the Cashier yet (Danial, Kenz). The class diagram will show the code as it is
  at stage-3.

## Acknowledgement of AI assistance

- **Tool**: Claude (Anthropic), used through Claude Code. Full conversation in
  gai-paper-trail/.
- **Written by me, with step-by-step hints**: CustomerState, and in Customer
  the fields, constructor, getters, `loseSatisfaction`, `seat`, `placeOrder`,
  `markServed`, `markReadyToPay`, `markPaid`, `leave` and `applyDiscount`;
  RegularCustomer.
- **Generated by the AI** (after the instructor approved AI use):
  `Customer.chooseItem`, VIPCustomer, CriticCustomer,
  `Customer.recoverSatisfaction` and `MAX_SATISFACTION`, the Javadoc fixes,
  CustomerCheck, VIPCriticCheck and SatisfactionRecoveryCheck, the starter
  class diagram, and a first draft of this discussion, which I reviewed and
  edited.
- **Explanation and review**: abstract classes, overriding, encapsulation,
  constructors, getters, enums, why money is stored in cents, and a review of
  each method I wrote.
- **How I checked it**: I compiled the whole project and ran the Stage 1
  victory input after each change (still "Result: Victory"), and ran the three
  check files, which all print "All checks passed." Claude also confirmed that
  changing the Regular loss from 8 to 7 on purpose makes CustomerCheck print
  FAIL.