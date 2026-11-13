# CLAUDE.md — Restaurant Rush (CT2003 Group 3)

NTU CT2003 group assignment: a turn-based restaurant game in plain Java, played on the command line. Game rules, stages, team split, build commands and the submission checklist are in [README.md](README.md). Read it before changing anything.

## This repo overrides the global CLAUDE.md
The global file is written for WorkSAFE at SMRT. Most of it does not apply here:
- This is a **university assignment on GitHub** (`armaldro/2003-Group3`), not a WorkSAFE repo. Ignore the GitLab, `glab`, runner, Azure, SMRT template, Promi and Standards Baseline rules.
- The repo lives in **OneDrive on purpose**. The "never use OneDrive" rule does not apply to this repo.
- Use `git` from the **Bash** tool. It is not on the PowerShell PATH. `gh` is installed and logged in as `armaldro`, so PRs can be read and opened with it; opening or merging a PR still needs the user's go-ahead.
- **JDK 25 is installed** (`javac` and `java` 25.0.5, run from the Bash tool), so compile and run the code rather than reasoning about it. Compile with the README command; for the checks, compile `src/main/java` and `testing/` into one folder outside the repo and run each check class. Checked on 12 Nov 2026: the project compiles and every check passes (155 OK, 0 FAIL on `feature/stage-2-arrivals`).

## Academic integrity (NTU GAI policy)
- Every use of Claude must be recorded in `gai-paper-trail/claude-conversation-log.md`: the prompt quoted exactly, Claude's reply in full, and personal details replaced with [brackets]. Add an entry after each request and keep the "To acknowledge in the submission" list up to date.
- The user is **Teo Soon How**: turn sequence, scoring, Happy Hour timing, stage tags, README and report. Other members' areas belong to them: customers (Rifqi), menu/order/chef/cashier/money (Danial), tables/waiter/host/console (Kenz). Explain or review their code, but don't write it unless asked, and say who owns a file before editing it.
- Prefer explaining and guiding over writing large amounts of code. The marks reward the group's own design.

## Code conventions
- **ECB packages** under `src/main/java/`: `entity` (data and state), `control` (game rules and turn sequence), `boundary` (all console I/O), `enums`, `util`. No `System.out` or `Scanner` outside `boundary` (Main's banner is the only exception).
- Money is held as **int cents** and formatted with `util.Money`.
- Javadoc on every public class and method. The Javadoc is a graded deliverable, generated into `docs/javadoc/`.
- No test framework. Checks are plain `main` classes in `testing/` (e.g. `MoneyCheck`) that print `OK`/`FAIL` lines.
- Each stage (`stage-1`, `stage-2`, `stage-3`) must be a runnable tagged commit. Later stages should extend the design, not rewrite it, because the README must list the files changed between stages.

## Git workflow
- Branches: `main` (stable), `dev` (integration), plus feature branches like `feature/customers` and `danial/money-util`.
- Never commit to `main`. Commit or push only when asked, in small commits with clear messages, matching the team's style ("Add markPaid and leave").
- Deadline: **Fri 13 Nov 2026, 23:59 SGT**.
