# GAI Paper Trail: Muhammad Danial Haziq bin Burhan

CT2003 Restaurant Rush, lab group COMP1, Group 3.

- **Tool:** Claude Code (Claude Sonnet 5.5), cloud session
- **Used by:** Muhammad Danial Haziq bin Burhan
- **Scope:** my part (Money, menu items, Order, Chef, Cashier, pricing).
- **Format:** verbatim. Each prompt is exactly as typed; each Claude reply is copied in full, text only. Tool calls (file reads, commands) are not reproduced. File paths of the sandbox and attached-file paths appear as typed.
- **Instructor decision (as relayed by Danial, 4 Nov 2026):** after Danial emailed Dr Li Fang, she approved the uses he asked about (GAI-generated starter code, design suggestions, help with bugs, and so on) and said only a FULL communication log with the AI must be provided. The written reply should be kept and cited in the declaration. Before this reply, Claude was used for explanation and review only, apart from one starter skeleton that was later reverted.
- **Omitted:** automatic system/context messages that Claude Code attaches to a prompt (e.g. email, date, environment notes). These were not typed by me.

## Summary of GAI use

_To be completed by Danial in his own words: what Claude generated (a starter skeleton, later reverted), and what was explanation or review of code I wrote._

---

## Request 1

**Prompt (Danial):**

> @"/root/.claude/uploads/97e97b25-89d5-52a2-9af3-74d41088f7eb/08962f2f-CT2003_Restaurant_Rush_Group_Assignment_Oral_Presentation.docx" sup man, this repo is my group project! As you can see in the README, my name is danial and i have been tasked to write the java code for those functionalities, and I aim to write it on my own based on my understanding, of course, i will ask you questions and ask for guidance where necessary, and also want starter code, or a boilerplate so i can be set in the right direction.
>
> ive also included the project specifications file so you have context on what this project is about. Please read it through, understand it, and use it to guide me as I come up with the functions. 
>
> lets do this together!

**Claude's reply:**

Let me look at the repo and the spec first.

I've read the brief and the README. Next I'll scaffold your part, keeping the logic for you to write.

It compiles; the earlier failure was just `grep` finding nothing to print. Committing and pushing now.

Hey Danial, I've read the brief and the README, and I've pushed a starter skeleton to `claude/nifty-curie-tn8ng0`. It compiles, and every method that holds logic is a TODO for you to write.

## What your part covers

The README gives you menu items, ComboMeal, Order, Chef, Cashier and pricing, plus the design discussion. The spec rules that touch your classes:

- **Money** is integer cents, shown as `$10.80`.
- **Chef:** one preparation unit per turn. The Chef may work on an order the Waiter took that same turn.
- **READY timing:** a dish that turns READY this turn can't be served until next turn at the earliest.
- **Cashier:** it takes one payment per turn, oldest first. A customer who became READY_TO_PAY this turn pays next turn at the earliest.
- **Invalid actions** must explain why they failed and must not use up the staff task. That is why I added `InvalidActionException`.
- **Pricing stacks across stages:** VIP gets 10% off, then Happy Hour takes 20% off, and the price is locked when the order is taken. A combo is 90% of its parts' combined price, e.g. Salad + Pasta = $24.30 and 3 units.
- **Stage tags:** `stage-1` must not contain any Stage 3 features, so don't write `ComboMeal` yet. It comes later, and it works because `MenuItem` is a normal class that a combo can extend.

## What's in the skeleton

| File | What it is |
| --- | --- |
| `enums/OrderStatus` | `IN_PROGRESS`, `READY`, `SERVED`, `PAID`, `CANCELLED` |
| `util/Money` | `format()` and `applyPercent()` stubs |
| `util/InvalidActionException` | Shared by the whole team |
| `entity/MenuItem` | Name, price in cents and prep units, with getters |
| `entity/Menu` | The three base dishes, plus a `choiceFor()` stub |
| `entity/Order` | Item, locked bill, units done, status, with stubs for `advance`, `serve`, `markPaid` and `cancel` |
| `entity/Chef` | A `prepare(Order)` stub |

## Decisions I left for you

- Do Salad, Burger and Pasta need their own subclasses? They differ only in data, so I'd say no.
- When does an order become READY and still not be servable that turn? The turn controller is Teo's, so agree the mechanism with him.
- Who decides what a customer orders: `Menu` or the Customer? The Critic in Stage 2 may want a different rule, so settle this with Rifqi.

## What I didn't do

- **Cashier** isn't scaffolded, because it needs Rifqi's `Customer` (satisfaction, READY_TO_PAY). Ask him what API he'll expose, then we'll build it.
- **Staff base class:** I didn't create one. Ask the team whether `Waiter`, `Chef` and `Cashier` should share one.
- **Order status in `Order`:** I assumed `Customer` holds its `Order`, and the bill is passed in, already discounted by customer type. Check this with Rifqi.

## Heads-up on AI use

The README says Claude-assisted work needs a paper-trail entry and a declaration. Log this skeleton in `gai-paper-trail`, and keep your own prompts and my replies there. I left Teo's log untouched.

## Next step

Start with `Money.format` and `MenuItem`, since they have no dependencies. Write them, then paste them here and I'll review and ask questions rather than hand you answers. Want to begin there?

---

## Request 2

**Prompt (Danial):**

> @"/root/.claude/uploads/97e97b25-89d5-52a2-9af3-74d41088f7eb/eb7c6e8c-Guidelines_and_Principles_for_the_implementation_of_GAI_Policy_for_Take_Home_Assignments_for_NTU_Courses.pdf" @"/root/.claude/uploads/97e97b25-89d5-52a2-9af3-74d41088f7eb/5535c5b7-Declaration_on_Use_of_GAI_Assistance.pdf" ive added the guidlines on GAI usage as well, can you read through it and tell me if what i am asking you to do now violates any rules stated? specifically generation of boilerplate

**Claude's reply:**

I don't have the PDFs' contents yet, so I'll read them properly before answering.

Short answer: I can't say for certain, because I haven't seen the one document that decides it. Generating the skeleton is probably fine under two of the three permitted categories and not fine under the strictest one.

## What the guidelines say

The general NTU policy doesn't set one rule. The instructor picks one category per assignment, and the choices are:

| Category | What GAI may do |
| --- | --- |
| (i) | Generate **key ideas only**. The final write-up is the student's own work. |
| (ii) | Help generate a **first text**, after the student has given detailed prompts. The student then refines it, and the submission must be that student's work. |
| (iii) | Refine **syntax and grammar** only. It can be combined with (i) or (ii). |
| (iv) | **No GAI at all** (the declaration form's option iv; the guideline text lists it as "vi"). |

Whichever category applies, you must also do three things:

- **Declare** your use honestly. The form is individual, even for group projects.
- **Footnote or cite in the text** any wording, fact or idea that came from the GAI.
- **Submit a digital paper trail** with the prompts and the GAI outputs. A missing or late trail can be treated as a possible integrity violation in itself.

The policy also says the whole submission can't be produced by GAI, and that you're mainly assessed on your own contribution. It allows an oral exam on your work, and the brief's presentation requires each member to present a part they understand.

## Does the boilerplate break a rule?

- **If CT2003 chose (i), key ideas only:** probably yes. Compilable class stubs with Javadoc go beyond ideas, and Javadoc completeness is part of the Implementation Code marks.
- **If CT2003 chose (ii), first text:** probably acceptable. The skeleton is a first draft you then rewrite. The condition is that you gave detailed prompts first, and my prompt wasn't very detailed. Your paper trail would need to show that.
- **If CT2003 chose (iv), none:** it would be a violation.

What I read says only that GAI use must be declared and have a paper trail. I haven't seen the course's deliverables sheet that the README mentions, or the section of the brief that says which category applies.

## What I'd do

1. **Find out the category** from the deliverables sheet, or ask the TA or instructor in writing. This is the step that settles it.
2. **Keep an honest record** whatever the answer. That means the paper-trail entry with this session's prompts and my outputs, a footnote or in-text credit for the skeleton, and your own signed declaration ticking the right boxes.
3. **If it turns out to be (i) only**, delete my skeleton and write your classes from scratch. I've already given you the class list and design questions as ideas. After that I'd stick to explaining concepts and reviewing what you write.
4. **Until you know**, don't build on my files. I can stop at ideas and guidance, and you write everything yourself.

I won't mark any of this as permitted when I haven't seen the instructor's choice. Do you want me to draft the question to the TA?

---

## Request 3

**Prompt (Danial):**

> ill write the email, give me points to write about, for now i have:
>
> * can we use GAI to write starter code e.g. class definition, parameters
> * can we use GAI to ask for help when we get stuck and lost

**Claude's reply:**

Here are points to add. Your two are good; the rest close the gaps that decide the answer.

**Your two questions, sharpened**
1. Can we use GAI to generate starter code, such as class definitions, attributes, method signatures and Javadoc with empty method bodies? Would that count as category (i), (ii) or (iii)?
2. Can we ask GAI for help when stuck? Please separate what you'd allow:
   - explaining concepts or design principles (SRP, polymorphism, etc.)
   - reviewing or critiquing code we wrote ourselves
   - explaining a compiler error or finding a bug
   - suggesting how to fix a bug, versus writing the fix

**Gaps that matter for marks**
3. Is GAI-generated Javadoc or code comments allowed? Javadoc completeness is marked, so this one matters.
4. Can GAI help with design work, such as suggesting class structure, responsibilities or design patterns, or reviewing our UML? The design discussion is worth 20 marks.
5. Can GAI help write the PDF report, README or presentation script, or only fix grammar (category iii)?
6. Can GAI help with the test notes, for example by suggesting test cases or reproducible victory and defeat runs?

**Declaration and paper trail**
7. Is the paper trail one group document or one per member? The Declaration on Use of GAI is individual, but our repo has a single shared log, so should each member keep their own?
8. What counts as "significant" enough to footnote: a whole class, a method, or a single idea? Is a comment such as `// idea from Claude` in the code enough, or do you want a footnote in the report?
9. Where should the paper trail be submitted: the NTULearn section, the repo's `gai-paper-trail/` folder, or both? What format do you want?

**Group-specific**
10. If one member uses GAI and another doesn't, does that affect group marks, or only the declaration of the member who used it?
11. We already have an entry that used GAI before we knew the rules, covering the README and the `Main.java` placeholder. Should we declare it retrospectively? Telling them upfront shows good faith.

**Suggested framing:** open with one line saying you want to follow the policy and are asking before building further. Quote the assignment brief, "Group Assignment: Restaurant Rush", and name your group, COMP1 Group 3. End by asking for a reply in writing so you can cite it in the declaration.

Keep the email short. Number the questions and put the four sub-options of point 2 as bullets, so the TA can reply with a letter or number per item.

Until they reply, I'd treat everything as category (i), ideas only. I'll explain concepts and review code you've written, and I won't generate code for you. Should I stick to that?

---

## Request 4

**Prompt (Danial):**

> Dear Dr Li Fang,
>
> I have some questions regarding GAI use in  CT2003 group assignment:
>
> * can we use GAI to generate starter code, such as class definition, attributes, methods? We write the body of the code
> * Can we use GAI to help with design work such as suggesting class structure?
> * Is the paper trail one group document or one per member? should each member create their own log if we use GAI?
> * We already generated the README and the base files of our Github repo using GAI, should we declare it? Or must we write it on our own? 
>
>
> Hope to hear from you soon!
>
> Sincerely, 
> Muhd Danial Haziq

**Claude's reply:**

The email is clear, but I'd change five things before you send it.

1. **You dropped your "ask for help when stuck" question.** It's probably the one you'll use most, so put it back.
2. **Say who you are.** Dr Li Fang needs your name, course code, lab group and the assignment title, or the reply may be vague. Include `CT2003, COMP1, Group 3, Restaurant Rush`.
3. **Be specific about "base files".** Say what was generated: the README and a placeholder `Main.java`, by Teo. Also say the starter skeleton for your classes (stubs and Javadoc) was generated after that. Otherwise the answer may only cover the README.
4. **Ask about Javadoc.** It's marked, and AI-written Javadoc on stubs is a grey area.
5. **Ask for a reply in writing.** You can then cite it in your declaration.

Here's a tightened version:

> Subject: CT2003 Group Assignment (COMP1 Group 3): clarification on GAI use
>
> Dear Dr Li Fang,
>
> I am Muhd Danial Haziq from COMP1 Group 3. We want to follow the GAI policy for the Restaurant Rush assignment, so we are asking before building further. Could you clarify the following?
>
> 1. **Starter code:** may we use GAI to generate starter code, meaning class definitions, attributes and method signatures with empty bodies, while we write all the method logic ourselves? Does that include GAI-written Javadoc on those stubs?
> 2. **Design help:** may we use GAI to suggest class structure and responsibilities, or to review our UML?
> 3. **Getting stuck:** may we ask GAI to explain concepts, explain compiler errors, or review code we wrote ourselves? Is suggesting a fix allowed, or only explaining the problem?
> 4. **Paper trail:** is it one group document or one per member? Each member's declaration is individual, so should each member keep their own log?
> 5. **Work already done:** before we read the policy, a teammate used GAI to write our README and a placeholder `Main.java`, and I used it to generate a skeleton of my classes. Should we declare this retrospectively, or must we redo it ourselves?
>
> Could you reply in writing so we can cite it in our declarations? Thank you.
>
> Sincerely,
> Muhd Danial Haziq

Two things to check before you send:

- **Your teammates:** the GAI log and the README say Teo used GAI for the README and `Main.java`. Check with them first, since point 5 discloses it, and they may want to be named or to word it themselves.
- **The skeleton:** the stubs I pushed are still on the branch. Whatever Dr Li replies, leave them untouched for now, and don't build on them until you hear back.

Meanwhile I'll stick to explaining concepts and reviewing code you write yourself, with no code generation.

---

## Request 5

**Prompt (Danial):**

> alright i sent it, now i think we can remove the changes youve made, and tell me where i can start first

**Claude's reply:**

I'll undo it with a revert commit instead of rewriting history. That keeps the record honest and needs no force-push.

The skeleton is gone. A revert commit on `claude/nifty-curie-tn8ng0` restored the empty folders. The history still shows that I added and removed it. Keep it that way, because it matches your paper trail and your declaration.

## Where to start

Start with the classes that depend on nobody else, in this order:

1. **`Money` (util).** Write `format(cents)`, so 1080 becomes `$10.80`. It's a small win, and every other class uses it.
2. **`MenuItem`.** It has a name, a price in cents and prep units. Decide whether Salad, Burger and Pasta need subclasses or are just three objects with different data. Your answer is a good point for the design discussion.
3. **`Menu`.** It holds the three base dishes. Hold off on the "who picks the dish" rule until you've talked to Rifqi.
4. **`OrderStatus` enum.** It tracks READY, SERVED, PAID and so on.
5. **`Order`.** It has an item, a bill locked at the time of ordering, units done and a status. Every state change should go through a method that rejects illegal moves.
6. **`Chef`.** It advances one order by one unit.
7. **`Cashier`.** Do this last, because it needs Rifqi's `Customer`.

Before you code, spend ten minutes on paper. For each class, write what it knows and what it does, and what it must not do. For example, the Chef shouldn't print anything or pick which order to cook. Those notes will feed straight into your class diagram and design discussion.

Two things to settle with the team now:
- **With Rifqi:** what API will `Customer` expose, and who decides what each customer orders?
- **With Teo:** how will the controller know a dish became READY this turn, so it can't be served until next turn?

Write `Money.format` and paste it here whenever you want a review. I'll point out problems and ask questions without writing it for you. That's consistent with treating this as ideas-only until Dr Li replies.

---

## Request 6

**Prompt (Danial):**

> ok so based on my own planning and what i understand,
>
> 1. Money: utilities
> 	- converts integer value to a valid money format
> 	- takes in an integer
> 	- will be used by cashier and customer as well as pricing for menu items
> 	- make sure handles edge cases when  used by cashier and customer

**Claude's reply:**

Your plan is mostly right. Three of the points need a second look, and your edge-case point is the part I'd focus on.

## Questions about your plan

**1. Who actually calls `Money`?**
You listed the Cashier, Customer and menu pricing. Two of those may not need it:
- Menu items and the Cashier store and add **cents**, which are plain ints. Does anything there need dollars-as-text?
- The brief says the UI only displays and rules live elsewhere. Which layer turns cents into `$10.80` for the screen?

If the answer is "mostly the boundary", then `Money` is a display helper. That's a stronger design story than "everyone uses it". It also means a `Customer` or `Cashier` that formats money itself is a design smell.

**2. "Handles edge cases": which ones?**
Write the list yourself before coding. Here are cases to test your list against:
- `0` should give `$0.00`
- `5` should give `$0.05`. What goes wrong if you just print `cents / 100 + "." + cents % 100`?
- `1000` should give `$10.00`, not `$10.0`
- A **negative** amount. Can the game ever produce one? If not, should `format` throw an exception, or quietly format it anyway? Pick one and be ready to justify it, since the marking mentions exception handling.

**3. Why a utility class, and how?**
- It has no state, so what should the class look like to stop anyone from creating an instance? Look at how `java.lang.Math` is set up.
- Is it static methods only?
- Why is `double` the wrong tool here? The brief explicitly says to store integer cents, so be able to say why.

## Parked for later

Stage 3 needs percentage discounts (VIP, Combo, Happy Hour), including **rounding** and **order of application**. Don't build it now, because `stage-1` must stay free of Stage 3 features. Think about whether that logic belongs in `Money` or somewhere else, and bring it back in Stage 3.

## Next step

Answer questions 1 and 2 in your own words, then write `Money.format` and paste it. I'll check it against the edge cases and ask questions about anything that looks off, without writing the code.

---

## Request 7

**Prompt (Danial):**

> separate question, ive cloned the github to my vs code, so money is a utility class, i should create in under src/main/java/util, my question is should every utility  be in their separate.java file or all utilities in one java file just as separate class definition

**Claude's reply:**

One file per class, so `Money` goes in `src/main/java/util/Money.java`. Your path is right.

## Why

- **Java enforces it for public classes.** A `public` class must live in a file with exactly its name. If you put `public class Money` and `public class InputValidator` in one file, it won't compile.
- **Package-private classes can share a file**, but that's a bad habit. They'd be hidden from the rest of your packages, and `util` classes need to be reachable from `entity`, `control` and `boundary`.
- **Practical reasons agree.** Your team works in parallel, and one class per file avoids merge conflicts. It also matches how the brief grades Javadoc and the class diagram.

## Details

- **File and class names must match.** `Money.java` contains `public final class Money`.
- **Package declaration:** the first line is `package util;`, matching the folder.
- **Don't group by "utility".** Split by purpose. `Money` handles money formatting, and input validation (Kenz's area) would be its own class. Avoid one `Utils` class that does everything, since that's a cohesion problem you'd have to explain in the design discussion.

## Quick check

In VS Code, install the "Extension Pack for Java" if you haven't. Then compile from the repo root with the command in the README:

```bash
javac -d out $(find src/main/java -name "*.java")
```

Go ahead and write `Money.format` when you're ready.

---

## Request 8

**Prompt (Danial):**

> package util;
>
> public final class Money {
>
>     // Private constructor to prevent instantiating
>     private Money() {
>         throw new AssertionError("Utility class cannot be instantiated");
>     }
>
>     // Money methods
>     public static String format(int value) {
>         // change format of value
>     }
> }

**Claude's reply:**

The structure is good. Here's what I'd tell you in a code review.

## What's good
- `final` class with a private constructor is the correct utility-class pattern.
- Throwing `AssertionError` from the constructor is a nice touch. It also blocks anyone who tries to call the constructor through reflection.
- The package line and file location are right.

## Things to fix or think about

**1. It won't compile yet.**
`format` promises to return a `String` but never does. That's fine as a placeholder, but a bare `// comment` body gives a "missing return statement" error. Either write the real body or temporarily throw an exception so the rest of the project still builds. That's your call.

**2. The parameter name `value` hides the unit.**
Is it dollars, cents or something else? Anyone calling `Money.format(1080)` has to guess. Rename it so the unit is obvious, and say so in the Javadoc. The brief marks Javadoc, so use the `@param` and `@return` tags.

**3. The Javadoc is missing.**
The class has none, nor does the method. A short class comment should say what the class is for. The method comment should include an example, something like input 1080 and the output you expect.

**4. Your edge-case decisions aren't recorded yet.**
Before writing the body, decide and write down:
- What does `5` produce?
- What does `0` produce?
- What happens with a negative value? Does it throw, and if so, which exception? (`IllegalArgumentException` is the usual choice when a caller passes a bad argument.) Or does it format a negative amount, like `-$0.50`? If you throw, document it with `@throws`.

**5. A hint for the body, not the answer.**
You can split cents into two parts using integer division and the remainder operator. Then the question is how to make the remainder always show two digits. Look at `String.format` and what `%02d` does, and try it with 5 and 50.

## Next step
Fill in the body and the Javadoc, then paste it back. Then test it yourself with a tiny `main` or a JUnit test using these inputs: `0`, `5`, `50`, `100`, `1080`, `4500`, and a negative. Tell me what each one prints, and whether it matches what you expected. That also gives you the "functional evidence" the brief asks for in the report.

---

## Request 9

**Prompt (Danial):**

> what does @param and @return do?

**Claude's reply:**

They're Javadoc tags. Javadoc is the tool that turns your `/** ... */` comments into the HTML documentation under `docs/javadoc/`. The brief marks "completeness of Java Doc", so this matters for your grade.

## What each one does

- **`@param name description`** documents one parameter. Write one line per parameter, using the exact parameter name.
- **`@return description`** says what the method gives back. Skip it for `void` methods.
- **`@throws ExceptionType when ...`** documents an exception the method can throw. It's relevant to your negative-value decision.

## Example, on a different method so it isn't your answer

```java
/**
 * Adds two whole-dollar amounts.
 *
 * @param first  the first amount in dollars
 * @param second the second amount in dollars
 * @return the sum in dollars
 * @throws ArithmeticException if the sum overflows an int
 */
public static int addDollars(int first, int second) {
    return Math.addExact(first, second);
}
```

## Rules

- The summary sentence comes first, then a blank line, then the tags.
- Tag order is `@param`, `@return`, `@throws`.
- Describe the meaning and the unit, not just the type. "Amount in cents" is useful, while "an int" tells the reader nothing, since the signature already says that.

## Check it works

Your IDE shows these when you hover over a method call in VS Code. To generate the HTML, run the command from your README:

```bash
javadoc -d docs/javadoc -sourcepath src/main/java -subpackages entity:control:boundary:enums:util
```

Open `docs/javadoc/index.html` in a browser and find your class. Missing tags show up as warnings in the console, which is a quick way to check completeness before submitting.

---

## Request 10

**Prompt (Danial):**

> ```java
> package util;
>
> // utility class to format a value into dollars and cents for UI 
> public final class Money {
>
>     // Private constructor to prevent instantiating
>     private Money() {
>         throw new AssertionError("Utility class cannot be instantiated");
>     }
>
>     // format method: takes a integer value and formats it
>     // example use: String value = Money.format(1080)
>     // value is now a string that looks like $10.80
>     /**
>      * 
>      * @param value
>      * @return amount as a string
>      */
>     public static String format(int value) {
>
>         int cents = value % 100;
>         int dollars = value / 100;
>         return String.format("$%d.%02d", dollars, cents);
>     }
> }
> ```

**Claude's reply:**

This is a good attempt. The positive cases are correct, and you used integer maths and `%02d` as intended. Two things need work.

## 1. Negative input gives garbage

You never recorded your decision on negatives, and the code doesn't handle them. Run these through it by hand and see what comes out:

- `-50`
- `-1080`
- `-5`

Work out what `%` and `/` return for negative numbers in Java, then what `%02d` does with a negative. Ask yourself whether the output is a valid money string. Then decide:
- Can the game ever produce a negative amount? Think about the bill, revenue and discounts.
- If not, should `format` accept it quietly, or reject it, since a negative here means a bug elsewhere?
- If you reject it, which exception fits "the caller passed a bad argument"? Remember the `@throws` tag.

Whichever you choose, say so in the Javadoc.

## 2. The Javadoc is still thin

- **The class comment uses `//`, so Javadoc ignores it.** Only `/** ... */` comments are picked up. Turn the class comment into a real one.
- **The `//` comment block above the method is wasted.** Move that explanation and the example inside the `/** */` block.
- **`@param value` has no description.** Say what it means and what unit it's in.
- **`@return amount as a string` is vague.** Say what the string looks like, using your `$10.80` example.
- **The parameter name `value` still hides the unit.** A name that says "cents" makes the method self-documenting.

## Test it yourself

Don't trust a method until you've run it. Write a tiny `main` (or a JUnit test) that prints `Money.format(...)` for `0`, `5`, `50`, `100`, `1080`, `4500` and your negatives. For each one, write down the output you expected before you run it, and compare. This also feeds your "functional evidence" section.

Fix both points, then paste it again. If the negative case still confuses you, tell me what you think `-50 % 100` gives and I'll help you reason through it.

---

## Request 11

**Prompt (Danial):**

> ```java
> package util;
>
> /**
>   utility class to format a value into dollars and cents for UI 
> */
> public final class Money {
>
>     // Private constructor to prevent instantiating
>     private Money() {
>         throw new AssertionError("Utility class cannot be instantiated");
>     }
>
>     /**
>      * format method: takes a integer value and formats it
>        example use: String value = Money.format(1080)
>        value is now a string that looks like $10.80
>
>        for negative values in billing, convert value to positive, then return with "-" before $ to show negative value
>      * @param value the amount in cents
>      * @return amount as a string e.g. "$10.80"
>      */
>     public static String format(int value) {
>
>         if (value < 0) {
>             value = value * -1;
>             int cents = value % 100;
>             int dollars = value / 100;
>             return String.format("-$%d.%02d", dollars, cents);
>         }
>         int cents = value % 100;
>         int dollars = value / 100;
>         return String.format("$%d.%02d", dollars, cents);
>     }
> }
>
> ```

**Claude's reply:**

This works now, and it's a clear improvement. Negatives format sensibly, and you documented the decision. Here's what I'd still flag.

## Logic

**1. The two branches repeat the same lines.**
Both compute `cents` and `dollars` and call `String.format`. Only the sign differs. Ask yourself what the smallest piece is that actually depends on the sign, and whether you can compute the digits once. A reviewer would call this duplication, and it's easy to remove.

**2. You reassign the parameter (`value = value * -1`).**
It's legal, but many style guides discourage it, because the original input is lost. If you pull out the sign into its own variable, point 1 and this one are solved together.

**3. One edge case you may not know about.**
What is `-Integer.MIN_VALUE` in Java? Try `System.out.println(-Integer.MIN_VALUE);`. If your negation overflows, what does the rest of your method do? The game will never hit this, but think about whether you want to guard against it or document that you don't. `Math.abs` has the same quirk, so read its Javadoc.

**4. Check the decision against the brief.**
You now support negative amounts. Can the game ever produce one? If not, you've added behaviour that nothing uses. That's fine if you can justify it. If a negative would actually signal a bug elsewhere, throwing might be the better design. Be ready to explain your choice in the design discussion.

## Javadoc

- **Format:** continuation lines in a `/** */` block should start with ` * `. Yours work, but Javadoc tooling and reviewers expect the asterisks.
- **Layout:** put a one-sentence summary first, then a blank line, then the details, then the tags. Right now the summary, example and negative-value rule run together.
- **The usage example** will render poorly as plain text. Look at `{@code ...}` and `<pre>` for showing code in Javadoc.
- **Wording:** "format method:" repeats what the signature says. Describe the behaviour instead, such as what it returns and when the output has a minus sign.
- **Naming:** `value` still hides the unit. You documented it in `@param`, but the parameter name could say it too.

## Test it

You haven't shown me your test results yet. Run `0`, `5`, `50`, `1080`, `-5`, `-50`, `-1080` and `Integer.MIN_VALUE`. What does each print? For each one, write what you expected first.

Tidy up the duplication and the Javadoc, then paste it back. When the tests give you the results you expected, `Money` is done and you can start on `MenuItem`.

---

## Request 12

**Prompt (Danial):**

> how do i write a quick main to test it

**Claude's reply:**

A scratch `main` is fine for now. Put it where it won't be mistaken for game code.

## Where to put it

Make a file called `MoneyCheck.java` in a throwaway folder, for example `scratch/` at the repo root. Don't put it under `src/main/java`, because that folder is your submission. Add `scratch/` to `.gitignore` too, or just don't `git add` it. Later you can turn the checks into proper tests under `testing/`.

## What it looks like

```java
import util.Money;

public class MoneyCheck {
    public static void main(String[] args) {
        // each row: input in cents, then the output YOU expect (decide before running)
        int[] inputs = {0, 5, 50, 100, 1080, -5, -50, -1080};
        String[] expected = {"", "", "", "", "", "", "", ""};   // fill these in first

        for (int i = 0; i < inputs.length; i++) {
            String actual = Money.format(inputs[i]);
            String result = actual.equals(expected[i]) ? "OK  " : "FAIL";
            System.out.println(result + " format(" + inputs[i] + ") = " + actual
                    + "   (expected " + expected[i] + ")");
        }
    }
}
```

Fill in the `expected` array yourself before running. The point is to predict, then compare. Add `Integer.MIN_VALUE` as an extra input if you want to see the overflow case.

## How to run it

From the repo root, compile your source and the scratch file together, then run it:

```bash
javac -d out src/main/java/util/Money.java scratch/MoneyCheck.java
java -cp out MoneyCheck
```

`MoneyCheck` has no package line, so it's in the default package. That's why you run it as `MoneyCheck` and not `scratch.MoneyCheck`.

In VS Code you can also click the "Run" link that appears above `main` once the Java extension is installed. It does the same thing.

## Later

Plain printing is fine for now. When you want proper tests, JUnit is the standard tool. The brief's "testing" folder and functional evidence will want a few reproducible checks, so this loop is a good first draft of that.

Run it and tell me which rows say FAIL, if any.

---

## Request 13

**Prompt (Danial):**

> Exception in thread "main" java.lang.UnsupportedClassVersionError: MoneyCheck has been compiled by a more recent version of the Java Runtime (class file version 65.0), this version of the Java Runtime only recognizes class file versions up to 52.0

**Claude's reply:**

The error means `javac` and `java` on your machine come from different JDK versions. It's an environment problem, not a bug in your code.

## What the message says

- **Class file version 65.0** means your `javac` is JDK 21.
- **Runtime up to 52.0** means your `java` is Java 8.

Your PATH has a newer compiler and an older runtime. The README requires JDK 17 or later, so the Java 8 runtime is the one to fix.

## Confirm it

```bash
javac -version
java -version
```

You'll probably see 21 for the first and 1.8 for the second.

## Fix it

Either of these works. The first is the proper fix.

**Option A: point `java` at the same JDK as `javac`.** Install or select JDK 17 or newer, then make sure your PATH and `JAVA_HOME` use it for both commands.
- **Windows:** edit Environment Variables, set `JAVA_HOME` to the newer JDK folder, and put `%JAVA_HOME%\bin` ahead of other Java entries in PATH. Open a new terminal afterwards.
- **macOS:** `export JAVA_HOME=$(/usr/libexec/java_home -v 21)` and add it to your shell profile.
- **Linux:** `sudo update-alternatives --config java`.

**Option B: compile for the old runtime, as a quick workaround.**
```bash
javac --release 8 -d out src/main/java/util/Money.java scratch/MoneyCheck.java
java -cp out MoneyCheck
```
This works for `Money`, but I wouldn't rely on it. The project may use Java 17 features later, such as records or `var`. Use it just to check `Money` today.

## Check afterwards

Run `javac -version` and `java -version` again. They should show the same major version, 17 or higher. Teo and Kenz may hit the same problem on their machines, so tell them what you found.

Then rerun the check and tell me which rows say FAIL, if any.

---

## Request 14

**Prompt (Danial):**

> how do i do option a

**Claude's reply:**

It depends on your OS, so here are the steps for each. If you tell me which one you're on, I can narrow it down.

## Step 1: find out what you have

```bash
javac -version
java -version
where java        # Windows
which -a java     # macOS / Linux
```

The `where` or `which -a` output lists every Java the terminal can find, in priority order. The first one wins. It will probably be an old Java 8 path.

## Step 2: pick or install a JDK 17+

If `javac -version` already shows 21, you have a good JDK. Find its folder so you can point to it. If you don't have one, install Temurin 21 from adoptium.net, which has installers for every OS.

## Step 3: point your PATH at it

**Windows**
1. Press the Windows key, search "Edit the system environment variables", and open it.
2. Click **Environment Variables**.
3. Under **System variables**, click **New** (or **Edit** if it exists):
   - Name: `JAVA_HOME`
   - Value: the JDK folder, for example `C:\Program Files\Eclipse Adoptium\jdk-21.0.x-hotspot`
4. Select **Path**, click **Edit**, and add `%JAVA_HOME%\bin`.
5. Use **Move Up** to put it at the top, above any old Java entry such as `C:\Program Files\Common Files\Oracle\Java\javapath` or a Java 8 folder.
6. Click OK on everything, then **open a new terminal**. Old terminals keep the old PATH.

**macOS**
```bash
/usr/libexec/java_home -V          # lists installed JDKs
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
```
To make it permanent, add those two `export` lines to `~/.zshrc`, then open a new terminal.

**Linux**
```bash
sudo update-alternatives --config java
sudo update-alternatives --config javac
```
Choose the same JDK 17+ entry for both.

## Step 4: verify

In a **new** terminal:

```bash
java -version
javac -version
```

Both should print the same major version, 17 or higher.

## VS Code note

If you run the code with VS Code's Run button instead, it uses its own setting. Open Settings, search `java.jdt.ls.java.home`, and set it to the same JDK folder. Otherwise the editor and the terminal may use different Javas.

## Common mistake

If the version doesn't change, you didn't open a new terminal, or an old Java entry is still above yours in PATH. `where java` or `which -a java` will show which one is winning.

Tell me what the two `-version` commands print after this, and then rerun your check.

---

## Request 15

**Prompt (Danial):**

> when i type where java it doesnt output anything

**Claude's reply:**

That's almost certainly a shell quirk, not a sign that Java is missing. Java did run earlier, since it gave you the version error, so it's on your PATH.

## Why it printed nothing

In **PowerShell**, `where` is an alias for a different command (`Where-Object`), so it silently outputs nothing. Use one of these instead.

**PowerShell**
```powershell
where.exe java
Get-Command java -All | Select-Object Source
```

**Command Prompt (cmd)**
```bat
where java
```

**macOS / Linux / Git Bash**
```bash
which -a java
```

Each prints every `java` the terminal can find, in priority order. The first line is the one that runs.

## What to look for

- A path like `C:\Program Files\Common Files\Oracle\Java\javapath\java.exe` or any folder with `jre1.8` or `jdk1.8` in the name is the Java 8 that's winning.
- If the first line is a Java 8 path, move your JDK 17+ entry above it in PATH, as in the earlier steps.

Also run the same lookup for the compiler:

```powershell
where.exe javac
```

Paste what you get from both here, and tell me which shell you're using (PowerShell, cmd, Git Bash or the VS Code terminal). Then I can tell you exactly which entry to change.

---

## Request 16

**Prompt (Danial):**

> C:\Program Files (x86)\Common Files\Oracle\Java\java8path\java.exe
> C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\javac.exe

**Claude's reply:**

Your diagnosis is confirmed.

- `java` resolves to `C:\Program Files (x86)\Common Files\Oracle\Java\java8path\java.exe`, an old Java 8 launcher stub that sits early in PATH.
- `javac` resolves to `C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\javac.exe`, which is your good JDK 21.

Your JDK 21 is installed correctly, but its `bin` folder is behind the Java 8 entry in PATH, and it doesn't contain `java` ahead of it. You don't need to install anything, only reorder PATH.

## Fix

1. Press the Windows key, search **Edit the system environment variables**, and open it.
2. Click **Environment Variables**.
3. Under **System variables**, click **New**:
   - Name: `JAVA_HOME`
   - Value: `C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot`
4. Select **Path** (system variables), click **Edit**, and click **New**. Add `%JAVA_HOME%\bin`.
5. Select that new entry and click **Move Up** until it is **above** the `...\Oracle\Java\java8path` entry. Order is what matters here.
6. Click OK on all windows.
7. **Close every terminal and VS Code, then reopen.** Old windows keep the old PATH.

## Verify

```powershell
where.exe java
java -version
javac -version
```

- `where.exe java` should now list the `Eclipse Adoptium\...\bin\java.exe` line first.
- Both `-version` commands should say 21.

## If the Java 8 entry still wins

- Check **User variables** as well. A `Path` entry there is also read, and it comes first for your user. Put the Adoptium entry at the top there if needed.
- Don't delete the Java 8 entry unless you know nothing else needs it. Reordering is enough.

After that, run your check again and tell me which rows say FAIL, if any.

---

## Request 17

**Prompt (Danial):**

> i followed your fix but it still shows java version 1.8

**Claude's reply:**

Let's find out what's actually happening instead of guessing. Run these in a **freshly opened** PowerShell (not a terminal that was open while you edited PATH).

## 1. See what the terminal finds now

```powershell
where.exe java
echo $env:JAVA_HOME
$env:Path -split ';' | Select-String -Pattern 'java|jdk|Adoptium'
```

The last command prints every PATH entry that mentions Java, in order. Paste all three outputs here. The order of those lines is the whole story.

## 2. Likely causes

- **The terminal wasn't restarted.** A new tab inside VS Code's terminal may still be a child of the old VS Code process. Fully quit VS Code (not just the window), then reopen it. Better still, test in a standalone PowerShell window first.
- **The entry is in the wrong list.** Windows has System variables and User variables. If you edited one `Path` but the Java 8 entry lives in the other, the result may not be what you expect. System entries come before user entries. If the `java8path` entry is in **System** Path, your Adoptium entry must be above it in **System** Path, not in User Path.
- **Move Up wasn't saved.** Reopen the Environment Variables window and check the order is still as you set it.
- **The Java 8 stub is special.** `java8path` holds Oracle's launcher stub and Oracle's installer puts it at the front. If nothing else works, you can remove that one entry from Path. Nothing in this assignment needs Java 8.

## 3. Quick workaround while you sort it out

You don't need `java` on PATH to test. Call the JDK 21 one directly:

```powershell
& "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe" -version
& "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe" -cp out MoneyCheck
```

If the first prints 21, your install is fine and it's only a PATH ordering issue. The second runs your check right now.

Paste the output from step 1 and I'll tell you exactly which entry is in the way.

---

## Request 18

**Prompt (Danial):**

> fixed it, i exit vs code and reenter, then i javac and java to run Moneycheck, all testcases passed

**Claude's reply:**

Good, that was a PATH-ordering problem, and restarting VS Code fixed it. Your `Money` class passes its checks.

## Before you move on

- **Did you include `Integer.MIN_VALUE` and the duplication cleanup?** You said all test cases passed, but I don't know if that input was in the list or if you removed the repeated branches. If either is still open, finish it now. They're quick, and they're the kind of thing a reviewer notices.
- **Keep your check file.** Move `MoneyCheck` out of `scratch/` into `testing/` when you're ready. It's your first piece of functional evidence for the report.
- **Commit your own work.** `Money.java` is yours, written by you. Keep commit messages plain, such as "Add Money.format with negative handling".

## GAI paper trail

My feedback on your code was review and explanation, so it counts as GAI assistance. Add an entry in your own log in `gai-paper-trail/` covering this session: your prompts and my replies, including the Money reviews. Do it now while it's fresh, because it's easier than reconstructing it at the deadline. Also keep an eye out for Dr Li's reply, and adjust if it restricts what I can help with.

## Next: `MenuItem`

Answer these on your own before writing:

1. What does a `MenuItem` need to know? You already have the three fields from the brief.
2. Which of those should be changeable after construction? Think about whether a price should ever change mid-game.
3. Do Salad, Burger and Pasta need their own subclasses? What would each subclass add, compared with three objects built from the same class?
4. What should the constructor do if someone passes a negative price or zero prep units?
5. Stage 3's `ComboMeal` has to be built from two existing items. What does that suggest about how you design `MenuItem` now? Don't build it yet, just think about it.

Write your answers in plain bullets like you did for `Money`, then write the class and paste both. I'll review them the same way.

---

## Request 19

**Prompt (Danial):**

> how do i generate the paper trail? i think it is acceptable that you help me with it

**Claude's reply:**

I can help with that, since it's a record of what happened and not content for your submission. The one rule is that it has to be **verbatim and complete**. It must contain your exact prompts and my exact replies, with nothing summarized or tidied up, because the policy uses it to verify your declaration. So I'll extract it from the session transcript instead of retyping it. First I'll check what the transcript looks like.

The full transcript is available. I'll check its structure, then extract it with a script so nothing is retyped.

I've generated your paper trail from the session transcript with a script, so the text is exactly what was said and nothing was retyped. It's pushed to `gai-paper-trail/danial-claude-log.md` on your branch. Pull the branch to open it in VS Code.

## What's in it

- A header covering the tool, who used it, and what the format means.
- 19 requests, each with your exact prompt followed by my full reply, including the skeleton I generated and the revert.
- No automatic system messages, which you didn't type. The header says they were removed.

## What you need to do

1. **Write the "Summary of GAI use" section yourself, in your own words.** I left a placeholder, and the honest version is short. Cover what I generated (the starter skeleton, which was later reverted) and what was explanation and review of code you wrote. I've deliberately not written this for you, because the declaration has to be yours and it must match the log.
2. **Read through it once.** You're the one declaring it's accurate, so check that nothing looks wrong. One example: Request 2 shows your message with text about me reading the PDFs. That is exactly what was in your message, so I left it as is.
3. **Regenerate it at the end of each session.** The file is a snapshot. Anything you ask me later isn't in it yet, and the last few lines of this conversation aren't either. Ask me to rebuild it when you finish a stretch of work, and the same script will do it.
4. **Check the format.** The NTU guidelines recommend a Word document for the trail. I've given you Markdown. Whether the course accepts Markdown is a question for Dr Li, since you asked about format indirectly. If Word is required, converting is easy and I can do it with `pandoc` if you ask.

## Things I haven't done

- The footnote or in-text acknowledgement for anything from me that ends up in your submission. Right now that's nothing in your code, since you wrote `Money` yourself. If you adopt my design suggestions in your report, such as the cohesion points or the `-Integer.MIN_VALUE` edge case, cite them.
- The signed declaration, which is yours alone. Tick the boxes after Dr Li replies.

Meanwhile, `MenuItem` is next. Try the five questions from my last reply first.

---

## Request 20

**Prompt (Danial):**

> you pushed the md file to main? also, what is the command to create a new branch, add my MoneyCheck and Money.java, and push to dev?

**Claude's reply:**

No. I pushed the paper trail to `claude/nifty-curie-tn8ng0`, the branch I was told to work on. Nothing went to `main` or `dev`. You can confirm on GitHub by switching the branch dropdown. To get the file into your own branch, see the end.

## Commands

Start from an up-to-date `dev`, make a branch, add your two files, commit and push:

```bash
git fetch origin
git checkout -b danial/money-util origin/dev
```

Move your check file somewhere trackable first. `testing/` is the folder meant for this:

```bash
mkdir -p testing
mv scratch/MoneyCheck.java testing/MoneyCheck.java
```

Then add, commit and push:

```bash
git add src/main/java/util/Money.java testing/MoneyCheck.java
git commit -m "Add Money.format utility and its check"
git push -u origin danial/money-util
```

## Getting it into `dev`

That pushes your **branch**, not `dev` itself. The usual flow is to open a pull request from `danial/money-util` into `dev` on GitHub and have a teammate look at it. That's safer for a shared project, and it gives you a record of reviews, which helps the "GitHub evidence" marks.

If your team agrees to push straight to `dev` instead, run this after the commit:

```bash
git checkout dev
git pull origin dev
git merge danial/money-util
git push origin dev
```

## Things to check

- **Run `git status` first.** Make sure only your two files are staged. Don't stage `out/`, which `.gitignore` already covers, or the `scratch/` folder.
- **`MoneyCheck` has no `package` line.** If it sits in `testing/` and ever moves under `src/main/java`, the layout will need rethinking. For now, compile it with `javac -d out src/main/java/util/Money.java testing/MoneyCheck.java`.
- **Is `dev` the right base?** The older log says `dev` exists, so I assumed it's your integration branch. If your team treats `main` as that branch, swap the names.

## The paper trail file

It's on my branch, not yours. To bring it into your branch without merging my whole branch:

```bash
git fetch origin claude/nifty-curie-tn8ng0
git checkout origin/claude/nifty-curie-tn8ng0 -- gai-paper-trail/danial-claude-log.md
git add gai-paper-trail/danial-claude-log.md
git commit -m "Add Danial's GAI paper trail"
```

That ends up in your PR too. Fill in the summary section before you commit it, if you can.

---

## Request 21

**Prompt (Danial):**

> error: Your local changes to the following files would be overwritten by checkout:
>         src/main/java/Main.java
> Please commit your changes or stash them before you switch branches.
> Aborting

**Claude's reply:**

Git is protecting you. You have uncommitted edits to `src/main/java/Main.java`, and `origin/dev` has a different version of that file. Switching would overwrite your edits, so it stopped.

## 1. Look at what changed

```bash
git status
git diff src/main/java/Main.java
```

Did you edit `Main.java` on purpose? You may have changed the team table or the TODO, or your editor might have changed line endings. Decide which case it is.

## 2. Pick one option

**A. Keep the edits and carry them to the new branch:**
```bash
git stash
git checkout -b danial/money-util origin/dev
git stash pop
```
If `stash pop` reports a conflict in `Main.java`, tell me and we'll go through it. Then stage only your Money files, and leave `Main.java` out unless the edit belongs in this commit.

**B. Commit the edits first on the branch you're on:**
```bash
git add src/main/java/Main.java
git commit -m "Update Main.java"
```
Only do this if the change is deliberate.

**C. Throw the edits away.** Only if you're sure they're accidental. This one is destructive and can't be undone:
```bash
git restore src/main/java/Main.java
```

I'd start with A, since it's reversible.

## 3. Then carry on

Once you're on `danial/money-util`, run the add, commit and push steps from before. Run `git status` before committing, so you know exactly which files are staged.

Paste the output of `git diff src/main/java/Main.java` if you're unsure what the change is, and I'll help you decide.

---

## Request 22

**Prompt (Danial):**

> lets move on to menuItem

**Claude's reply:**

Good. Before writing any code, I need your answers to the five questions from last time. They lead to the design choices you'll have to defend in the report. Here they are again with prompts to think through.

## Answer these first, in bullets

1. **What does a `MenuItem` know?** The brief gives you three facts. What are the types, given your cents rule?
2. **What can change after construction?** Should a price ever change mid-game? What keyword makes a field unchangeable, and what does that mean for setters?
3. **Subclasses or not?** What would a `Salad` subclass do that a `MenuItem` named "Salad" can't? If the answer is "nothing", what does that tell you about the design? Compare with `Customer`, where Regular, VIP and Critic really do behave differently.
4. **Invalid input:** what should happen if someone builds an item with a negative price, zero prep units or a blank name? Which exception fits a bad argument, and where does the check go?
5. **Stage 3 readiness:** a `ComboMeal` has a name, a price and a unit count too. What does that suggest about how `MenuItem` should look now, and what should be `private` versus accessible to a subclass? Don't write the combo, just design `MenuItem` so it won't need rewriting.

## Extra points to think about

- **`toString()`:** the player will see menu items on screen. Should `MenuItem` format its own price, or should the display layer do it? Remember your answer about who calls `Money`.
- **Equality:** two `MenuItem` objects with the same name, are they "the same"? Will anything compare them, for example the Chef matching orders to dishes? If not, you don't need `equals()`, and you can say why.
- **Where do the three dishes live?** Something has to create Salad, Burger and Pasta and hold them. That's `Menu`, which comes next. For now, keep `MenuItem` free of any knowledge about the menu.

## Then write the class

Once your bullets are down, write `MenuItem` with Javadoc on the class, constructor, and each public method, using `@param`, `@return` and `@throws` where they apply. Then extend your check file with a few cases:
- a valid item (does `getPriceCents()` return what you passed?)
- each kind of invalid input (does it throw what you expect?)

Paste your bullets and the code together, and I'll review them the same way as `Money`.

---

## Request 23

**Prompt (Danial):**

> package entity;
>
> /**
>   entity class to create menu items
> */
> public class MenuItem {
>
>     private String name;
>     private int price;
>     private int prepUnits;
>
>     public MenuItem(String name, int price, int prepUnits) {
>         this.name = name;
>         this.price = price;
>         this.prepUnits = prepUnits;
>     }
>     
>     public String getName() {
>         return this.name;
>     }
>
>     public int getPrice() {
>         return this.price;
>     }
>
>     public int prepUnits() {
>         return this.prepUnits;
>     }
> }
>
>
> is this correct for a starter?

**Claude's reply:**

It's a good start, and it compiles. The structure is right: private fields, a constructor and getters is proper encapsulation. A reviewer would raise these points, most important first.

## 1. Inconsistent getter name (`prepUnits()`)
The other getters are `getName()` and `getPrice()`, but this one is `prepUnits()`. Java naming convention, which the brief marks, says getters are `getX`. Check your last method against that rule. Anything calling it later will also expect one pattern.

## 2. The fields can be changed
Your answer to question 2 isn't here yet. Should a menu item's price ever change once it's created? If not, which keyword makes a field unchangeable, and what does it do to your design? Think about what a `ComboMeal` needs to rely on when it reads these values.

## 3. No validation
`new MenuItem("", -5, 0)` is accepted today, and it would break the game later: a dish with zero prep units, for example, could never be served correctly. Which values are invalid, and which exception should the constructor throw? Answer question 4 from my earlier list.

## 4. `price` hides its unit
The same issue as `Money`: is it dollars or cents? The name should say, and so should the Javadoc.

## 5. Javadoc is missing
You have a class comment but nothing on the constructor or methods, and the class comment lacks the ` * ` line prefixes. Documentation is marked, so each public method needs a summary plus `@param`, `@return` and `@throws` where they apply.

## 6. Smaller things
- There's no `toString()`. Who prints a menu item for the player, and what should it look like? Remember your answer about who calls `Money`.
- `this.` in the getters is fine, but you used it inconsistently with the constructor style. Pick one habit.

## Still open from last time
You haven't answered the five questions yet, in particular:
- **Subclasses or not?** Why would Salad, Burger and Pasta be separate classes, or not?
- **Stage 3 readiness:** what makes `MenuItem` easy to extend with `ComboMeal` later?

Write a few bullets on those, update the class, and paste both. Then add checks to your check file for a valid item and for each bad input, and tell me what they print.

---

## Request 24

**Prompt (Danial):**

> 2. i think price should be changeable so that combomeal can add up the prices easily
> 3. i dont think salad burger and pasta need to be separate classes because there is no distinct methods that each can have, what do you think?
>
>
>
> ```java
> package entity;
>
> /**
>  * entity class to create menu items
>  */
> public class MenuItem {
>
>     private String name;
>     private int price;
>     private int prepUnits;
>
>     /**
>      * @param name the name of the item
>      * @param price the price of the item in cents
>      * @param prepUnits the number of each item available
>      */
>     public MenuItem(String name, int price, int prepUnits) {
>         if (name == "") {
>             System.out.println("name cannot be empty");
>             return;
>         }
>         if (price < 0) {
>             System.out.println("price cannot be negative");
>             return;
>         }
>         if (prepUnits < 1) {
>             System.out.println("must have at least 1 prep unit");
>             return;
>         }
>         this.name = name;
>         this.price = price;
>         this.prepUnits = prepUnits;
>     }
>     
>     /**
>      * @return the name of the dish
>      */
>     public String getName() {
>         return name;
>     }
>
>     /**
>      * @return the price of the dish
>      */
>     public int getPrice() {
>         return price;
>     }
>
>     /**
>      * @return the number of dishes available
>      */
>     public int getPrepUnits() {
>         return prepUnits;
>     }
> }
>
> ```

**Claude's reply:**

Your reasoning on question 3 is right, but question 2's reasoning has a gap, and the constructor has a real bug.

## Your answers

**Q3, no subclasses:** I agree, and your reasoning is the right one. Use inheritance when types behave differently, and these three differ only in data. Compare with the Stage 2 customers, where Regular, VIP and Critic really do behave differently. Use that contrast in your design discussion.

**Q2, mutable price:** I'd push back. You said the price should be changeable so `ComboMeal` can add prices. Does adding two prices require changing either one? A combo only needs to **read** the components through `getPrice()`. Think about what could go wrong if price were changeable:
- A shared `MenuItem` (the Pasta every customer orders) could have its price changed under orders already in progress. How does that fit the brief's "price is locked when the order is taken"?
- Happy Hour is a pricing policy applied when the order is taken. Does it need to modify the dish itself?

So what does `final` buy you here? You'd still have getters, and no setters. Decide again with that in mind.

## Constructor bugs

**1. Invalid input leaves a broken object.**
`return` inside a constructor doesn't cancel construction. `new MenuItem("", -5, 0)` still produces an object, with `name` as `null` and the numbers at `0`. The caller has no idea. Instead of printing and returning, what should the constructor do so that no object is created at all? You did the same reasoning for `Money` with negative values.

**2. Printing from an entity.**
The brief says the UI only displays and rules live in domain classes. A constructor that prints to the console breaks that separation. How else can it report the problem to the caller?

**3. `name == ""` doesn't test what you think.**
In Java, `==` on Strings compares references, not contents. Look up how to compare string contents, and also what happens with `null`, and with a name made only of spaces.

**4. `price < 0` allows a price of 0.**
Is a free dish valid in this game? Decide, and make the check match your decision.

## Javadoc

- `@param prepUnits` says "the number of each item available" and `getPrepUnits` says "number of dishes available". Both are wrong: prep units are the **units of Chef work** needed to prepare it. This matters, because a wrong comment misleads teammates.
- No summary sentences on the methods, and no `@throws` once you add exceptions.
- Say the unit for the price in `getPrice`'s `@return`.

## Still open

- **Naming:** `price` still hides its unit. You documented it, but the name could say it too.
- **`toString()`:** still missing. Decide who prints a dish and what it looks like.

## Next step

Fix the constructor with your decisions on the points above, update the Javadoc, and decide on `final`. Then extend your check file to build each kind of invalid item and see what happens. If your tests pass for the wrong reason, say, because nothing throws, that's the signal. Paste it back when ready.

---

## Request 25

**Prompt (Danial):**

> package entity;
>
> /**
>  * entity class to create menu items
>  */
> public class MenuItem {
>
>     private final String name;
>     private final int price;
>     private int prepUnits;
>
>     /**
>      * @param name the name of the item
>      * @param price the price of the item in cents
>      * @param prepUnits units of Chef work needed to prepare the dish
>      */
>     public MenuItem(String name, int price, int prepUnits) {
>         if (name == null || name.isBlank()) {
>             throw new IllegalArgumentException("name cannot be empty");
>         }
>         if (price <= 0) {
>             throw new IllegalArgumentException("price cannot be negative or free");
>         }
>         if (prepUnits < 1) {
>             throw new IllegalArgumentException("must have at least 1 prep unit");
>         }
>
>         this.name = name;
>         this.price = price;
>         this.prepUnits = prepUnits;
>     }
>     
>     /**
>      * @return the name of the dish
>      */
>     public String getName() {
>         return name;
>     }
>
>     /**
>      * @return the price of the dish
>      */
>     public int getPrice() {
>         return price;
>     }
>
>     /**
>      * @return the unit of chef work needed to prep the dish
>      */
>     public int getPrepUnits() {
>         return prepUnits;
>     }
> }

**Claude's reply:**

This is much better. The constructor now stops bad input from creating an object, and `isBlank()` plus the null check is the right way to test the name. Here's what remains.

## Still to fix

**1. `prepUnits` isn't `final`.**
You made `name` and `price` final but left this one open. Is there any reason a dish's prep units should change after it's created? If not, it should match the others. Think about what a reviewer asks when one field out of three is treated differently, and what `ComboMeal` relies on when it sums units.

**2. The Javadoc still doesn't document the exceptions.**
Your constructor now throws `IllegalArgumentException` in three cases. A caller reading the docs can't see that. Which tag documents it, and what should it say for each condition?

**3. Missing summary sentences.**
Each Javadoc comment jumps straight to tags. Add a one-line description first, then a blank line, then the tags. That's what shows up in the generated HTML index.

**4. Units and names.**
- The `price` field and `getPrice()` still don't show the unit in their names. You've documented cents on the constructor, but not on `getPrice()`'s `@return`. Someone calling `getPrice()` has to hunt for it.
- The message "price cannot be negative or free" reads oddly. Think about what the person debugging would want to see: ideally the invalid value as well.

**5. No `toString()` yet.**
Decide: who shows a dish to the player, and what does it look like? Remember your own answer about who calls `Money`. If the boundary layer does the formatting, you may not need `toString()` here at all. Say which, and why.

## Check it works

You haven't told me your test results. Add these to your check file, since an exception that never fires would pass silently:
- valid input: do the getters return what you passed in?
- `null`, `""` and `"   "` for the name
- price `0` and `-1`
- prep units `0`

For each invalid case, catch `IllegalArgumentException` and confirm the message. Also try the extreme: what happens with a huge price such as `Integer.MAX_VALUE`? Does it matter for your game?

## Where this leaves you

Once those are fixed, `MenuItem` is done. Next comes `Menu`, which creates Salad, Burger and Pasta and decides who owns the "which customer orders what" rule. Ask Rifqi that before you start. Paste the updated class and your test results when ready.

---

## Request 26

**Prompt (Danial):**

> package entity;
>
> /**
>  * entity class to create menu items
>  */
> public class MenuItem {
>
>     /**
>      *  fields declared when a MenuItem is created. fields are final so that they cannot be edited once created
>      * 
>      * @field name the name of the item
>      * @field price the price of the item in cents
>      * @field units of chef work needed to prepare the dish
>      */
>     private final String name;
>     private final int price;
>     private final int prepUnits;
>
>     /**
>      * a constructor for MenuItem. Takes in the name of the dish, the price as well as how much work is needed to prepare it
>      * 
>      * @param name the name of the item
>      * @param price the price of the item in cents
>      * @param prepUnits units of Chef work needed to prepare the dish
>      * @throws IllegalArgumentException if name is null, price is negative or 0, or prepUnits is less than 1
>      */
>     public MenuItem(String name, int price, int prepUnits) {
>         if (name == null || name.isBlank()) {
>             throw new IllegalArgumentException("name cannot be empty");
>         }
>         if (price <= 0) {
>             throw new IllegalArgumentException("price cannot be negative or 0");
>         }
>         if (prepUnits < 1) {
>             throw new IllegalArgumentException("must have at least 1 prep unit");
>         }
>
>         this.name = name;
>         this.price = price;
>         this.prepUnits = prepUnits;
>     }
>     
>     /**
>      * getName returns the name of the dish
>      * 
>      * @return the name of the dish
>      */
>     public String getName() {
>         return name;
>     }
>
>     /**
>      * getPrice returns the price of the dish
>      * 
>      * @return the price of the dish in cents
>      */
>     public int getPrice() {
>         return price;
>     }
>
>     /**
>      * getPrepUnits returns the unit of chef work needed to prep the dish
>      * 
>      * @return the unit of chef work needed to prep the dish
>      */
>     public int getPrepUnits() {
>         return prepUnits;
>     }
> }

**Claude's reply:**

I'll check one suspicious tag against the real Javadoc tool before telling you about it.

The logic and exception handling are now done. The Javadoc has one real problem, which I checked with the Javadoc tool.

## Problems

**1. `@field` isn't a Javadoc tag.**
I tested it on a copy of your field comment. With private members included, the Javadoc tool reports `error: unknown tag: field` for each `@field` line. This is a real error and would show up when you generate the docs for submission. The README's Javadoc command doesn't include private members, so you may not see it by default, but don't rely on that.

Also, a `/** */` block above `private final String name;` documents **only that one field**. Your block is describing three, so the other two get no comment. You have two clean options, and you should choose:
- Use a normal `//` comment above the group of fields, and skip Javadoc for private fields. Most projects do this, since private members don't appear in the generated docs by default.
- Give each field its own short `/** */` comment.

**2. The `@throws` text doesn't match the code.**
It says "name is null", but your check also rejects blank names. Make the sentence say what the code does. You can also say it as three conditions on separate lines.

## Style

- **The summaries repeat the method name** ("getName returns the name of the dish"). Convention is to start with a verb phrase, such as "Returns the name of the dish", and not to restate the method name, which the signature already shows. Then drop the duplicate sentence in `@return`.
- **The summaries and `@return` lines say the same thing twice.** One clear sentence plus the tag is enough. Keep the unit in `@return`, since the name still doesn't carry it.

## Open decisions

- **Naming:** the `price` field and `getPrice()` still don't show the unit. You documented cents, so it's a style call, but be able to justify it. Consistency with how you wrote `Money` is a good argument either way.
- **`toString()`:** you haven't said whether the dish shows itself or the boundary prints it. Say which, and why, because this goes straight into your design discussion.
- **Test results:** I still haven't seen your checks for invalid input. Did every bad value throw the exception with the message you expect? Tell me what happened, especially for `"   "`, price `0` and prep units `0`.

## Next step

Fix the `@field` lines and the `@throws` wording, then tell me your test results and your `toString()` decision. After that `MenuItem` is done, and `Menu` is next. Before starting it, ask Rifqi who owns the rule that decides which dish a customer orders.

---

## Request 27

**Prompt (Danial):**

> <!-- reply 1 -->
> > Naming: the price field and getPrice() still don’t show the unit. You documented cents, so it’s a style call, but be able to justify it. Consistency with how you wrote Money is a good argument either way.
>
> i dont understand what you mean by this

**Claude's reply:**

I mean that the name `price` doesn't tell a reader what unit the number is in, and you may want it to.

## The problem

Look at this call in someone else's code:

```java
int p = item.getPrice();
```

Is `p` 15 (dollars) or 1500 (cents)? The method name doesn't say. They have to find your Javadoc to know. If they guess wrong, a Burger costs $1500 or $0.15, and nothing crashes, so the bug is easy to miss.

## Two ways to handle it

1. **Put the unit in the name.** Call the field `priceCents` and the getter `getPriceCents()`. Now the call reads `item.getPriceCents()`, and nobody can mistake it. This is common practice when a number has a unit.
2. **Keep `price` and rely on documentation.** That works if the whole project agrees that every price is in cents, and everyone remembers it.

## Why it matters for you

The brief says all money is stored as integer cents. Your teammates will write `Customer`, `Cashier` and the display code, all of which touch prices. A name that carries the unit catches mix-ups before they happen. It also explains your decision about `Money.format(value)`, which has the same issue: `value` is cents, but the name doesn't say.

## What I'm asking you to decide

Pick option 1 or 2, and have a one-sentence reason ready, since someone might ask in the presentation. There's no wrong answer. It's a naming decision, and I'm only pointing out that the choice affects whether other people misuse your class.

If you choose option 1, remember to rename the constructor parameter, the field and the getter together, and update your check file.

---

## Request 28

**Prompt (Danial):**

> import entity.MenuItem;
>
> public class menuItemCheck {
>     public static void main(String[] args) {
>         // create MenuItem object
>         MenuItem burger = new MenuItem("burger", 500, 1);
>
>         // Test getName method
>         String actualName = burger.getName();
>         String nameResult = actualName.equals("burger") ? "OK " : "FAIL ";
>         System.out.println(nameResult + "burger.getName() = " + actualName + " expected: burger");
>
>         // Test getPriceCents method
>         int actualPriceCents = burger.getPriceCents();
>         int priceResult = actualPriceCents.equals(500) ? "OK " : "FAIL ";
>         System.out.println(priceResult + "burger.getPriceCents() = " + actualPriceCents + " expected: 500");
>
>         // Test getPrepUnits method
>         int actualPrepUnits = burger.getPrepUnits();
>         int PrepUnitResult = actualPrepUnits.equals(1) ? "OK " : "FAIL ";
>         System.out.println(PrepUnitResult + "burger.getPrepUnits() = " + actualPriceCents + " expected: 1");
>
>     }
> }

**Claude's reply:**

Good, you've gone with `getPriceCents()`, and a test per getter is the right idea. As written it won't compile, and one line would give a wrong result even if it did.

## Compile errors

**1. `actualPriceCents.equals(500)` on an `int`.**
`int` is a primitive, so it has no methods. `.equals` only exists on objects like `String`. How do you compare two ints for equality? You already know the answer from everyday code. The same mistake appears in the prep-units test.

**2. `int priceResult = ... ? "OK " : "FAIL "`.**
You're assigning a `String` to an `int` variable. What type should the result variable be? Look at how you declared `nameResult`, which is correct.

## Logic bug

**3. The last print shows the wrong variable.**
```java
"burger.getPrepUnits() = " + actualPriceCents
```
It prints `actualPriceCents`, not the prep units. This bug is nasty because the code would compile, and the line would print a misleading value, such as 500, next to a PASS or FAIL decided by a different variable. Find where the wrong variable is used.

## Style

- **Class name:** `menuItemCheck` starts with a lowercase letter. Java classes use `UpperCamelCase`, and the file name must match the class name exactly, so rename both the file and class together.
- **Variable names:** you wrote `PrepUnitResult` with a capital P while the others start lowercase. Be consistent.
- **Repeated code:** you've written the same compare-and-print three times. If you want, think about a small helper method that takes a label, the actual value and the expected value. That's optional for now.
- **Test data:** the brief says Burger costs $15.00, which is 1500 cents. Using the real value, even in a test, makes the result easier to compare against the spec.

## What's missing

You haven't tested the invalid inputs yet. Those are the interesting part, because the exceptions are new behaviour. Add checks for:
- `null`, `""` and `"   "` as the name
- price `0` and `-1`
- prep units `0`

For each, you want to see an `IllegalArgumentException` and the right message. How can you run code that is expected to throw and still keep the test going? Look up `try`/`catch`. Also decide what should be printed if **no** exception happens, since that's the failure case.

## Next step

Fix the compile errors, then compile and run it. Tell me what prints. Then add the invalid-input tests and tell me what those print. If any case surprises you, paste the output and we'll work out why together.

---

## Request 29

**Prompt (Danial):**

> My professor has replied and to all the points i listed, she says ok and that i only have to provide FULL communication log with AI. 
>
> this means you can generate starter code, give design suggestions, help with bugs, etc. but remember to log every convo we have in a md file !

**Claude's reply:**

Good news. I'll update your log now, so it covers everything up to this message and notes the instructor's decision.

---

