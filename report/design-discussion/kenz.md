# Design Discussion: Kenz

Scope: Table, Staff, Waiter, Host, Restaurant, WaiterTask,
InvalidActionException, ConsoleUI and InputReader. (Customers, the menu,
orders, Chef, Cashier and the controller are covered by teammates.)

## 1. Key abstractions and their responsibilities

| Class | Responsibility | Collaborates with | Stage |
| ----- | -------------- | ----------------- | ----- |
| Table | Holds a table number and at most one customer. `seat()` refuses a table that is already taken; `free()` empties it when its customer pays or leaves. Does not decide who sits where. | Customer; created and owned by Restaurant | 1 |
| Staff (abstract) | Holds the "one action per turn" rule once for every role: `startTurn()` resets it, `checkCanAct()` refuses a second action, `markActed()` uses the action up. Each role must say what it is through the abstract `getRole()`. Does not know what any action does. | InvalidActionException; extended by Waiter and Host | 1 |
| Waiter | Seats a queued customer, takes a seated customer's order, serves a READY dish, or waits, at most once per turn. Checks its own rules before changing anything, so a refused action does not use up the turn. Does not set prices or decide when a dish became READY. | Staff, Restaurant, Customer, Order, Table | 1 |
| Host | Seats at most one queued customer per turn, on top of the Waiter's task. Seating goes through `Restaurant.seat()`, the same as the Waiter. Cannot take orders or serve. | Staff, Restaurant, Customer, Table | 3 |
| Restaurant | Owns the tables, the waiting queue, the list of every customer who arrived, the menu and the staff. `seat()` is the only method that seats anyone, and it checks every rule before changing anything. Answers lookups such as `freeTables()`, `customersToServe()` and `ordersToCook()`, and hands out read-only copies of its lists. Does not run the turn or keep score. | Table, Waiter, Host, Chef, Cashier, Menu, Customer, Order | 1 (Host in 3) |
| WaiterTask | Lists the Waiter's four tasks (SEAT, TAKE_ORDER, SERVE, WAIT) with the label shown to the player. Each task keeps the same number every turn. | ConsoleUI, GameController | 1 |
| InvalidActionException | A checked exception for a choice that breaks a game rule. Its message is the reason shown to the player. | thrown by Staff, Waiter, Host, Restaurant; caught by GameController | 1 |
| ConsoleUI | Prints everything the player sees: the restaurant state, each customer with their type, the choices, error reasons, the running score at the start of each turn, the end-of-turn results, the Host and Happy Hour prompts and the final result. Holds no game rules and changes nothing. | Restaurant, Customer, Table, WaiterTask, Money | 1 (type label in 2, Host and Happy Hour in 3) |
| InputReader | The only class that reads input. Asks again until it gets a whole number in range, and stops with a clear error if a replay file runs out. Prints the number back in piped runs, so replay outputs are readable. Does not decide whether a choice is allowed by the rules. | GameController | 1 |

## 2. Design principles, only where the code genuinely shows them

- **SRP (Single Responsibility):** each rule lives in one place.
  `Restaurant.seat()` holds the seating rules (the customer must be queued, the
  table must be free), so `Waiter.seat()` and `Host.seat()` do not repeat them.
  `Staff` holds only the turn rule. The boundary is split the same way:
  `InputReader` only checks "is this a number from min to max?", while whether
  that choice is allowed is decided by Restaurant, Waiter and Host. Without
  this, a change to the seating rules would have to be made in two classes and
  could drift apart.
- **OCP (Open/Closed):** Stage 3 added the Host as a new class. `Staff`,
  `Waiter`, `Table` and the seating rules did not change; `Restaurant` only
  gained a field and `getHost()`. In Stage 2, `ConsoleUI` showed the VIP and
  the Critic without any change for new types: its `label()` method calls
  `customer.getTypeName()`, and each customer class answers for itself, with
  no `instanceof` check.
- **LSP (Liskov Substitution):** Waiter and Host can each stand in for
  `Staff`. `startTurn()`, `hasActed()` and the refusal of a second action
  behave the same for both, and `HostCheck` shows a Host and a Waiter each
  using their own action in the same turn. This principle is also why Host
  does not extend Waiter (section 3).
- **ISP (Interface Segregation):** not applicable. My classes have no
  interfaces, and the brief warns against adding them only to show a
  principle.
- **DIP (Dependency Inversion):** not applicable. `GameController` is given
  its `ConsoleUI` and `InputReader` by `Main` (dependency injection), but it
  depends on these concrete classes rather than on an abstraction, so this is
  not dependency inversion.

## 3. Trade-offs and one alternative design

- **Checked exception for rule breaks.** `InvalidActionException` extends
  `Exception`, so the compiler forces every caller to handle a rule break, and
  a bad choice is always explained and retried instead of crashing the game.
  The cost: every action method carries `throws InvalidActionException`, and
  the controller needs a `try`/`catch` around each one.
- **Check everything first, then act.** `Restaurant.seat()` and the Waiter's
  methods check every rule before changing anything, and call `markActed()`
  last. A refused seat cannot leave a customer half-seated (out of the queue
  but at no table). The cost: each rule has to be written as a check up front,
  which makes the methods a little longer.
- **The display gets plain numbers.** `ConsoleUI` receives revenue, paid count
  and average as ints, and only decides how to show them (for example "N/A"
  when nobody has paid). It holds no rules. The cost: long parameter lists,
  such as `showTurnResults()` with seven.
- **Read-only copies of lists.** `getTables()`, `getWaitingQueue()` and
  `getCustomers()` return `List.copyOf(...)`, so no other class can add or
  remove a customer without going through Restaurant. The cost: a new list is
  built on each call, which is fine for a game with two tables and six
  customers.
- **Alternative considered: Host extends Waiter.** The Host seats customers
  exactly as the Waiter does, so inheriting from Waiter would have reused
  `seat()`. Rejected because the Host would also inherit `takeOrder()` and
  `serve()`, which a Host must not do. A Host could then be used anywhere a
  Waiter is expected and told to serve food, which breaks the Liskov
  Substitution Principle. A cleaner option would be an abstract
  `SeatingStaff` between Staff and both roles. We kept Host and Waiter as
  separate `Staff` subclasses because the shared code is three lines that
  only call `Restaurant.seat()`, where the rules already live once, and
  adding a new layer would have changed the Stage 1 Waiter class for little
  gain.

## 4. How the player's choices change the result

The Waiter is the bottleneck: one task per turn across seating, ordering and
serving, with only 2 tables. Every turn a customer waits unserved costs them
satisfaction (8 for a Regular, 5 for a VIP, 12 for a Critic), and the average
satisfaction of paid customers must reach 60.

- **The fastest path.** The brief's example shows one Regular ordering Pasta,
  given the Waiter's full attention: seated on turn 1, order taken on turn 2,
  served on turn 4 and paid on turn 6, at satisfaction 76. Every turn the
  Waiter spends on someone else first costs that customer another 8.
- **Order of tasks matters.** In our Stage 2 replay, the player alternates
  seating, ordering and serving so that no one waits long: 5 customers pay,
  revenue is $73.50 and the average satisfaction is 76, which meets all three
  targets (4 paid, $45, 60). In the defeat replay the Waiter waits every turn:
  nobody is served, the Critic leaves after 9 turns (9 x 12 = 108), a Regular
  after 13 (13 x 8 = 104), and nobody pays.
- **Tables are the second constraint.** A table stays taken until its customer
  pays, so seating a new arrival can be impossible even when the Waiter is
  free. Serving and paying out the earlier customer first frees the table
  sooner.
- **The Host (Stage 3) relieves the Waiter.** With the Host seating one
  customer per turn, the Waiter's single task can go to ordering or serving
  instead. If the Host acts before the Waiter, as planned, a customer the Host
  seats can have their order taken in the same turn, saving a waiting turn (8
  satisfaction for a Regular).
- **Mistakes are not punished twice.** An invalid choice explains why it
  failed and asks again without using up the Waiter's or Host's turn, so the
  result depends on the player's decisions, not on typing errors.

## 5. Open points with teammates

- **Decision:** who enforces "a dish that became READY this turn is served next
  turn". **Agreed with:** Soon How and Danial. **Outcome:** the controller runs
  the Waiter before the Chef, so any order that is READY when the Waiter acts
  was finished on an earlier turn. Waiter, Order and Chef stay unaware of
  turns.
- **Decision:** one class or two for console input. **Agreed with:** Soon How.
  **Outcome:** `InputReader` is the only class reading `System.in`, and
  `ConsoleUI` only prints. Two Scanners on the same input were breaking piped
  replay files.
- **Decision:** the Host step in the turn. **Agreed with:** Soon How.
  **Outcome:** planned for the Host to act before the Waiter (build plan).
  `Host`, `Restaurant.getHost()` and the ConsoleUI methods (`showHostChoices`,
  `showHostIdle`, `showHappyHourStatus`, `showHappyHourOffer`) are merged; the
  GameController step that calls them is Soon How's and was not yet in `dev`
  when this was written.
- **Still open:** Chef and Cashier do not extend `Staff`, so their
  one-action-per-turn limit is enforced by the controller rather than by the
  Staff rule.

## Acknowledgement of AI assistance

- **Tools:** Claude (claude.ai) and Claude Code (Claude Opus 5.5). The full
  conversations are in my GAI paper trail in `gai-paper-trail/`.
- **Generated by the AI:** boilerplate files `Table`, `WaiterTask` and
  `InvalidActionException`; skeletons with numbered TODOs for `Staff`,
  `Waiter`, `Restaurant` and `InputReader`; `WaiterCheck`. At my request,
  Claude then wrote the code for the `Restaurant` TODOs (14 to 23), the
  `Waiter` TODOs (6 to 13) and `InputReader.readChoice()`, and later `Host`,
  `Restaurant.getHost()`
  and `getCashier()`, the ConsoleUI type label, turn status, end-of-turn
  results, "N/A" average, Host and Happy Hour display methods, the
  InputReader echo, Javadoc fixes, `HostCheck` and the Stage 2 replay files.
- **Written by me:** the `Staff` TODOs (1 to 5), with step-by-step guidance and
  review from Claude, and the decisions about how my classes fit the team's
  design.
- **Explanation and review:** what each TODO meant, why Restaurant had to come
  before Waiter, why `markActed()` is called last, why Host should not extend
  Waiter (LSP), Git and GitHub help, and a review of the project against the
  brief.
- **How I checked it:** I ran `WaiterCheck` and `HostCheck` and the
  stage 1 and 2 replay files, and studied each method so that I can explain
  it.
- **This document:** drafted by Claude from my conversation history and my
  class diagram, then checked by me.
