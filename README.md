# CT2003 Group 3: Restaurant Rush

CT2003 Object-Oriented Design & Programming, 26S1, CCDS, NTU. Lab group COMP1, Group 3.

Restaurant Rush is a turn-based restaurant management game played on the command line, written in Java. The player is the manager. Staff time is limited, so each turn the manager decides which customers to seat, whose order to take, which dish the kitchen works on next, and what to serve.

## Group Members

| Name | Lab Group | Responsibilities |
| ---- | --------- | ---------------- |
| Muhamad Rifqi bin Zulkiflie | COMP1 | Customers (Regular, VIP, Critic) and satisfaction; UML class diagram |
| Muhammad Danial Haziq bin Burhan | COMP1 | Menu items, ComboMeal, Order, Chef, Cashier and pricing; design discussion |
| Muhammad Kenz s/o Jafar | COMP1 | Tables, Waiter, Host, console display and input; testing and functional evidence |
| Teo Soon How | COMP1 | Turn sequence, scoring and Happy Hour timing; stage tags, README and report |

## The Game

### Base setting

The game always offers this setting, so that the TA can reproduce our runs.

| Setting  | Value |
| -------- | ----- |
| Turns    | 18 (the game always runs all 18) |
| Tables   | 2 |
| Staff    | 1 Waiter (one task per turn), 1 Chef (one preparation unit per turn), 1 Cashier (one payment per turn). From Stage 3, 1 Host (seats one waiting customer per turn) |
| Arrivals | One customer at the start of turns 1, 4, 7, 10, 13 and 16. In Stage 2, turn 4 is a VIP and turn 10 is a Critic. From Stage 3, the Critic arrives on turn 7 instead (see below). The rest are Regular |
| Happy Hour | From Stage 3. The manager can start it once per game, at the start of any turn. It lasts 2 turns, starting with that one. Orders taken during Happy Hour are 20% off, after any VIP discount. At the start of each Happy Hour turn, every customer still waiting for a table or food recovers 15 satisfaction, up to 100 |
| Victory  | After turn 18: at least 4 paid customers, revenue of at least $45, and average satisfaction of paid customers of at least 60 |

| Menu item | Price  | Preparation units | From |
| --------- | ------ | ----------------- | ---- |
| Salad     | $9.00  | 1 | Stage 1 |
| Burger    | $15.00 | 1 | Stage 1 |
| Pasta     | $18.00 | 2 | Stage 1 |
| Salad + Pasta combo | $24.30 (90% of $27.00) | 3 (1 + 2) | Stage 3 |

Customer n orders menu item n, counting round the menu, so with four items customer 5 orders the Salad again.

| Customer | Satisfaction lost per unserved turn | Pays | From |
| -------- | ----------------------------------- | ---- | ---- |
| Regular  | 8  | Full price | Stage 1 |
| VIP      | 5  | 10% off (a $15.00 Burger bills $13.50) | Stage 2 |
| Critic   | 12 | Full price. Always orders the dish that takes longest to cook: Pasta in Stage 2, the Salad + Pasta combo from Stage 3 | Stage 2 |

### Turn sequence

1. **Arrivals:** the scheduled customer joins the waiting queue with satisfaction 100.
2. **Manager choices:** from Stage 3, the manager is first offered Happy Hour while it is unused, then the Host may seat one waiting customer at the first free table. The Waiter then seats a customer, takes an order, serves a READY dish or waits. The Chef advances one order by one unit or waits.
3. **Kitchen and serving:** the Waiter acts before the Chef. A dish finished this turn is served next turn at the earliest.
4. **Eating and payment:** a served customer eats for one turn, then becomes READY_TO_PAY. The Cashier takes one payment per turn, oldest first.
5. **Waiting and abandonment:** unserved customers lose satisfaction at turn end. A customer who reaches 0 leaves without paying.
6. **Result:** after turn 18, Victory if all three targets are met, otherwise Defeat.

## Stages

Each stage is a separate runnable version, tagged in this repository.

| Stage | Adds | Tag |
| ----- | ---- | --- |
| 1. Basic operation | Regular customers, menu, Waiter, Chef, Cashier, full turn sequence, both endings | `stage-1` |
| 2. Customer behaviour | VIP and Critic customers | `stage-2` |
| 3. New role and menu policy | Host, ComboMeal, Happy Hour | `stage-3` |

### Stage 1 → Stage 2

Stage 2 adds VIP and Critic customers. Each type's behaviour lives in its own class, so the new types needed no change to `Waiter`, `Chef`, `Cashier` or `Order`, and the only turn-sequence change is in `handleArrivals`. Compare: https://github.com/armaldro/2003-Group3/compare/stage-1...stage-2

**New files**

| File | Owner | Why |
| ---- | ----- | --- |
| `src/main/java/entity/VIPCustomer.java` | Rifqi | The VIP: loses 5 satisfaction per turn and gets 10% off |
| `src/main/java/entity/CriticCustomer.java` | Rifqi | The Critic: loses 12 satisfaction per turn, pays full price, and always orders the dish that takes longest to cook |
| `src/main/java/control/Arrival.java` | Soon How | One scheduled arrival: the turn and which type of customer arrives |
| `testing/VIPCriticCheck.java` | Rifqi | Checks the VIP's and the Critic's satisfaction loss, prices, dish choice and type names |
| `testing/stage2-victory-input.txt`, `testing/stage2-victory-output.txt` | Kenz | A reproducible winning Stage 2 game with the VIP (turn 4) and the Critic (turn 10): 5 paid, $73.50, average satisfaction 76. Replay with `java -cp out Main < testing/stage2-victory-input.txt` |
| `testing/stage2-defeat-input.txt`, `testing/stage2-defeat-output.txt` | Kenz | A reproducible losing Stage 2 game: the Waiter waits every turn, the Critic runs out of patience and leaves, and nobody pays |

**Changed files**

| File | Owner | Why |
| ---- | ----- | --- |
| `src/main/java/control/GameConfig.java` | Soon How | The arrival schedule now says which type of customer arrives on each turn: a VIP on turn 4 and a Critic on turn 10. The Critic is customer 4, who would order a Salad as a Regular, so its Pasta order shows the Critic's twist. The setting also refuses a schedule that is out of turn order or runs past the last turn |
| `src/main/java/control/GameController.java` | Soon How, Kenz | `handleArrivals` now asks the arrival schedule to create each customer, because Stage 1 hard-coded `new RegularCustomer(...)`. Nothing else in the turn sequence changed. Kenz also switched the payment step to `Restaurant.getCashier()`, and added `getServedCustomers()` and `getUnhappyDepartures()` so the turn displays can show them |
| `src/main/java/entity/Restaurant.java` | Kenz | Holds the Cashier, through `getCashier()`, alongside the Waiter and Chef |
| `src/main/java/boundary/ConsoleUI.java` | Kenz | Shows each customer's type, for example "Customer 4 (Critic)", by calling `getTypeName()` on the customer, with no type checks. Adds the running score at the start of each turn (revenue and average satisfaction of paid customers), the end-of-turn results (served, unhappy departures, revenue, average, turns used), and "N/A" for the average until someone pays, which the brief requires (sections 3.1 and 3.5) |
| `src/main/java/boundary/InputReader.java` | Kenz | Prints each choice read from a piped file, so replay outputs are readable; Javadoc names the right replay file |
| `src/main/java/entity/Staff.java`, `Table.java`, `enums/WaiterTask.java` | Kenz | Javadoc fixes |
| `src/main/java/entity/Customer.java`, `RegularCustomer.java` | Rifqi | `getTypeName()`, so each customer's type can be shown, and Javadoc fixes |
| `src/main/java/enums/CustomerState.java` | Rifqi | Javadoc fixes |
| `testing/GameControllerCheck.java` | Soon How | Checks the arrival schedule, the VIP and the Critic in play (satisfaction loss, dish, price and revenue), and a full winning and losing Stage 2 game |
| `testing/stage1-*-output.txt` | Kenz | Regenerated with the echoed input and the customer type |
| `README.md` | Soon How | The Stage 2 customers and arrivals, and this section |

**Deleted files:** none.

**Stage 1 evidence:** the `testing/stage1-*` replays were recorded when every customer was Regular, and they match the game at the `stage-1` tag. They no longer fit the Stage 2 schedule, because the Critic's Pasta on turn 10 needs a second unit of cooking. A winning Stage 2 game gives 5 paid, $73.50 and an average satisfaction of 76.

### Stage 2 → Stage 3

Stage 3 adds the Host, the Salad + Pasta combo and Happy Hour. The Host is a new `Staff` subclass, the combo is a new `MenuItem` subclass, and the Happy Hour price is a separate `PricingPolicy` applied after the customer's own discount, so `Waiter`, `Chef`, `Cashier` and `Order` did not change. The turn sequence gains two steps before the Waiter: the Happy Hour offer and recovery, then the Host. Compare: https://github.com/armaldro/2003-Group3/compare/stage-2...stage-3

**New files**

| File | Owner | Why |
| ---- | ----- | --- |
| `src/main/java/entity/Host.java` | Kenz | The Host: a second member of staff who can seat one waiting customer per turn, so the Waiter is free to take an order on the same turn |
| `src/main/java/entity/ComboMeal.java` | Danial | A menu item made of two dishes: 90% of their combined price, and their preparation units added together |
| `src/main/java/enums/PricingPolicy.java` | Danial | Normal price or Happy Hour price (80%), applied when an order is taken |
| `src/main/java/control/HappyHour.java` | Soon How | Happy Hour timing: whether it has been used, and whether it is on during a turn, with the turns left |
| `testing/HostCheck.java` | Kenz | Checks the Host's seating and waiting |
| `testing/ComboMealCheck.java`, `testing/MenuCombosCheck.java`, `testing/PricingPolicyCheck.java` | Danial | Check the combo's price and units, the Stage 3 menu, and the Happy Hour price |
| `testing/CustomerCheck.java`, `testing/SatisfactionRecoveryCheck.java` | Rifqi | Check the customer's states and satisfaction, and the Happy Hour recovery up to 100 |
| `testing/HappyHourCheck.java` | Soon How | Checks Happy Hour timing: unused at first, on for 2 turns, then off, and only once per game |
| `testing/stage3-victory-input.txt`, `testing/stage3-victory-output.txt` | Soon How | A reproducible winning Stage 3 game. Happy Hour starts on turn 7, when the Critic arrives, and the Host seats each new customer: 5 paid, $75.24, average satisfaction 83 |
| `testing/stage3-defeat-input.txt`, `testing/stage3-defeat-output.txt` | Soon How | A reproducible losing Stage 3 game: Happy Hour is never used, the Host and Waiter wait every turn, the Regular on turn 1 and the Critic run out of patience, and nobody pays |

**Changed files**

| File | Owner | Why |
| ---- | ----- | --- |
| `src/main/java/control/GameConfig.java` | Soon How | The setting now holds the menu, Happy Hour's length (2 turns) and its recovery (15). The Critic moves from turn 10 to turn 7: with four menu items a Regular customer 4 would also order the combo, so on turn 10 the Critic's twist would not show. As customer 3 the Critic orders the combo where a Regular would order the Pasta |
| `src/main/java/control/GameController.java` | Soon How | The Happy Hour step (offer while unused, recovery at the start of each Happy Hour turn) and the Host step, both before the Waiter. The Happy Hour price is applied when an order is taken. The restaurant now uses the menu from the setting |
| `src/main/java/entity/Menu.java` | Danial | `withCombos()`: the standard dishes with the Salad + Pasta combo added at the end, so the standard dishes keep their positions |
| `src/main/java/entity/Customer.java` | Rifqi | `recoverSatisfaction(points)` and `MAX_SATISFACTION`, for the Happy Hour recovery |
| `src/main/java/entity/Restaurant.java` | Kenz | Holds the Host, through `getHost()` |
| `src/main/java/boundary/ConsoleUI.java` | Kenz | Shows the Host's choices, the Happy Hour offer and the Happy Hour status |
| `testing/GameControllerCheck.java` | Soon How | The Stage 1 and 2 checks now decline Happy Hour and let the Host wait, so they test the same rules. New checks for the Host, Happy Hour, the recovery, the Critic on turn 7 and a full winning and losing Stage 3 game |
| `README.md` | Soon How | The Stage 3 rules, and this section |

**Deleted files:** none.

**Stage 2 evidence:** the `testing/stage2-*` replays match the game at the `stage-2` tag. They no longer fit the Stage 3 game, because each turn now starts with the Happy Hour offer and the Host. To replay them, check out `stage-2` first.

## Repository Structure

```
.
├── src/main/java/        Java source code (package layout follows the ECB pattern)
│   ├── entity/           Entity classes (customers, staff, Table, Order, menu items, ...)
│   ├── control/          Control classes (game rules and the turn sequence)
│   ├── boundary/         Boundary classes (command-line display and input)
│   ├── enums/            Enumerations (order status, customer status, ...)
│   └── util/             Helpers (input validation, money formatting, ...)
├── docs/javadoc/         Generated Javadoc HTML
├── diagrams/             UML class diagram (Visual Paradigm project + exported images)
├── report/               PDF report
│   └── reflections/      Each member's individual reflection, one file per member (merged into the report)
├── forms/                Signed Declaration of Original Work and Declarations on Use of GAI
├── testing/              Test notes, including reproducible victory and defeat runs
└── gai-paper-trail/      Prompts and outputs from any GAI use (required by the NTU GAI policy)
```

## Build & Run

Requires JDK 17 or later.

In Git Bash, macOS or Linux:

```bash
# compile
javac -d out $(find src/main/java -name "*.java")

# run
java -cp out Main

# replay a recorded run
java -cp out Main < testing/stage3-victory-input.txt

# generate Javadoc
javadoc -d docs/javadoc -sourcepath src/main/java -subpackages entity:control:boundary:enums:util
```

In Windows PowerShell, the compile and replay commands are different. The run and Javadoc commands are the same as above.

```powershell
# compile
javac -d out (Get-ChildItem src\main\java -Recurse -Filter *.java).FullName

# replay a recorded run
Get-Content testing\stage3-victory-input.txt | java -cp out Main
```

## Submission Deadline

**Fri 13 Nov 2026, 11:59 PM (SGT, UTC+8)**, through "Group Assignment Submission" on NTULearn. Attempts are unlimited and the last attempt with a mark counts.

## Submission Checklist

One zip per group. The brief names it `LABGroup_AssignmentGrp.zip` (for us, `COMP1_Grp3.zip`), but the deliverables sheet says `CT2003_AssignmentGroupNumber.zip`, so confirm with the TA which one applies.

- [ ] Source code with build and run instructions, and Javadoc
- [ ] README with the repository URL and the stage comparisons
- [ ] Tags `stage-1`, `stage-2` and `stage-3` on runnable commits
- [ ] PDF report: final class diagram, design discussion and functional evidence
- [ ] Individual reflection from each member in the report, drafted in `report/reflections/`: Rifqi, Danial, Kenz, Soon How
- [ ] Signed Declaration of Original Work
- [ ] Each member's Declaration on Use of GAI Assistance, plus the paper trail
- [ ] Oral presentation of about 15 minutes, with every member presenting a part

## Marking Weightage

| Component | Marks |
| --------- | ----- |
| UML Class Diagram | 30 |
| Design Consideration | 20 |
| Implementation Code | 25 |
| Demonstration & Report | 25 |
