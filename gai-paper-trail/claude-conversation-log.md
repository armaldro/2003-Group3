# Claude Code Conversation Log

GAI paper trail for CT2003 Restaurant Rush, lab group COMP1, Group 3.

- **Tool:** Claude Code, using Claude Opus 5.5 (`claude-opus-5-5`)
- **Used by:** Teo Soon How
- **Format:** one entry per request, starting from entry 9. Each prompt is quoted exactly as typed and Claude's reply is copied in full. Local file paths and other personal details are replaced with [brackets].
- **Earlier entries:** entries 1 to 8, from earlier on Sat 31 Oct 2026, are kept privately outside this repository. The "To acknowledge" list below still covers them.

## To acknowledge in the submission

Anything below that ends up in the submission needs a footnote or in-text reference saying it came from Claude.

- `README.md`: all of the current text was written by Claude (entries 3 and 6).
- `src/main/java/Main.java`: the placeholder was written by Claude (entry 6).
- Design ideas from Claude, if the team adopts them (entries 4 and 5):
  - the class structure: Customer, MenuItem and Staff base classes; the applyDiscount() and chooseItem() hooks; Restaurant holding the waiting queue; GameConfig, Money in cents, InvalidActionException, WaiterTask, Scoreboard and ArrivalSchedule;
  - the food-choice rule (customer n orders menu item (n − 1) mod 3);
  - the payment tie-break, the Critic twist options and the Host acting before the Waiter;
  - the Stage 1 to 3 test cases.
- The work split, timeline and milestones (entry 4), if the report describes them.
- How `Main.java` is structured (entry 9), if the team follows it: Main creates the console boundary, the game setting and the controller, passes the first two into the controller and starts it; one shared `Scanner`; Main written last.

## Log (from Sat 31 Oct 2026)

### 9. How to write Main.java

> how should i write for Main.Java?

Claude read `Main.java`, the empty `util/GameConfig.java` and the brief, then explained what `Main.java` should contain without writing any code.

Files: `gai-paper-trail/claude-conversation-log.md`.

Claude's reply:

I'll explain what goes in it rather than write it, since we agreed I won't write submission code until the instructor confirms the GAI level.

**Main's one job**

Main creates the few objects the game needs, connects them, and hands over to the controller. Nothing else. Its own Javadoc already says this, and the brief backs it up: rules belong in control and entity classes, and input and output belong in boundary classes (section 2.2). Part of the coupling and cohesion marks comes from keeping it that way.

When it's finished, `main` does four things, in this order:

1. Create the console boundary object: the only class that reads the keyboard and prints to the screen.
2. Pick the game setting: the base setting from `GameConfig` (18 turns, 2 tables, the six arrivals, the menu). If you add other difficulty settings later, the boundary asks the player which one, and Main just passes the answer on.
3. Create the `GameController`, passing it the boundary object and the setting through its constructor.
4. Tell the controller to start.

That's three or four lines. If `main` grows past about ten lines, something that belongs elsewhere has crept in.

**Why pass things in, instead of letting the controller create them**

If `GameController` creates its own console and config, it's tied to those exact classes. If Main creates them and passes them in, you can start a game with a different setting (a shorter game for testing, or a Stage 2 arrival list with a VIP and a Critic) without touching the controller. This is called dependency injection. It only counts as dependency inversion (the D in SOLID) if the controller depends on an interface or abstract class rather than the concrete console class, and the brief warns against interfaces created just to tick a box (section 4.4). Mention it in the design discussion only if your code really does it (section 5).

**What to keep out of Main**

- The turn loop, satisfaction, payment or any other rule. These go in `control`.
- `Scanner` and menu prompts. These go in `boundary`. Create one `Scanner` for the whole game and don't close it: closing it also closes `System.in`, and every later read fails.
- `static` fields holding game state, such as a static list of customers. State lives in objects the controller owns.
- The current welcome `println`. It's fine as a placeholder, but once the boundary class exists, move the banner there so Main prints nothing itself.

**Exceptions**

Invalid input isn't Main's job. The brief says an invalid choice must explain why it failed and let the manager choose again without using up the staff task (section 3.4), so it's handled in the boundary and controller, where the choice is made. Main may catch an unexpected exception at the top and print a short message instead of a stack trace, but don't use that to hide errors you should fix.

**When to write it**

Write Main last, not first. It can't call `GameController` until that class exists. An order that works:

1. You own the turn sequence, so agree with the team on what the controller's constructor takes and what its start method is called.
2. Kenz builds the console boundary class; you build `GameController` and `GameConfig`.
3. Then replace the TODO with the four steps, and update the Javadoc on `main` to say what it does.

**Across the stages**

Main should hardly change between stage-1, stage-2 and stage-3. VIP and Critic arrivals belong in the arrival schedule or config; the Host, ComboMeal and Happy Hour belong in the controller and entities. If Main does change, it should be a small setup change, which the brief says is expected (section 4.4), and you list it in the README's stage comparison.

**Two checks before tagging each stage**

- From a fresh clone, run the two commands in the README's Build & Run section exactly as written. The first segment of the oral presentation is the game launching from the submitted project (section 6.2).
- Main has no `package` line, so it's in the default package. That's fine for the entry point, and `java -cp out Main` relies on it. If you ever move it into a package, update the README's run command too.

Write it yourself and I'll review it, or ask me to go deeper on any of these points. This is entry 9 of the paper-trail log.

### 10. Commit message for the starter files

> based on the current work I have done I understand that I have done a basic template and also created the base file for the team to start coding, can you help me write a simple commit message to send to the team so I can push the files up for the team to start?

Claude checked the uncommitted changes and compiled the project; it failed because the three new classes are still empty. Claude then pointed out three things to fix and wrote a commit message. It did not change any source files.

Files: `gai-paper-trail/claude-conversation-log.md`.

Claude's reply:

Here's a commit message. Fix three things first, though, because the project doesn't compile right now.

**Fix before you push**

1. `control/GameController.Java` has a capital J. Rename it to `GameController.java`, or the README's build command won't find it.
2. There are two `GameConfig.java` files, one in `control/` and one in `util/`. Main imports `control.GameConfig`, so delete `util/GameConfig.java`, or change the import if you'd rather keep it in `util`.
3. The class Javadoc now sits above the `import` lines, so Javadoc won't attach it to `Main`. Move the comment to just above `public class Main`.

Even after those fixes, Main won't compile until ConsoleUI, GameConfig and GameController are written, because all three files are empty. That's fine as a starting point, but I'd push it to `dev`, not `main`. `main` (9b165cc) still has the placeholder that runs, and the team plan has everyone branching from `dev` anyway. On GitHub, `dev` is behind `main`, so this push just moves it forward:

```
git switch -c dev
git add -A
git commit
git push -u origin dev
```

Paste this as the commit message:

```
Wire up Main and add empty starter classes

Main now creates the console UI and the base game setting, passes
them to GameController and starts the game. It won't compile until
these classes are written:

- boundary/ConsoleUI.java (Kenz)
- control/GameConfig.java (Soon How)
- control/GameController.java (Soon How)

The files are empty for now so everyone knows where they go.
The GAI paper-trail log now starts from entry 9; entries 1 to 8
are kept privately.
```

This is entry 10 of the paper-trail log.

### 11. Starting the log from entry 9

> the conversation recorded should start only from
> How to write main java awrds

Claude moved entries 1 to 8 out of this log into a private file outside the repository and kept the original entry numbers.

Files: `gai-paper-trail/claude-conversation-log.md`, and a private file outside the repository.

Claude's reply:

The log now starts from entry 9 ("How to write Main.java"). I kept the original numbers so the log still lines up with the earlier entries.

I didn't delete entries 1 to 8. I moved them to a private file in your course folder, outside the public repo: `New folder\GAI paper trail - entries 1-8 (31 Oct 2026).md`. Keep that file. Those entries record where the README text, the first `Main.java` and the design ideas came from, and the paper trail you declare has to cover every GAI use that shaped the submission. For the same reason, the "To acknowledge" list at the top of the log still includes those items.

Entries 1 to 8 are also still on GitHub in commit 9b165cc, because taking them out of the file doesn't remove them from the history. Removing them from the history would need a force-push, so tell me if you want that.

This is entry 11 of the paper-trail log.
