# Claude Code Conversation Log

GAI paper trail for CT2003 Restaurant Rush, lab group COMP1, Group 3.

- **Tool:** Claude Code, using Claude Opus 5.5 (`claude-opus-5-5`)
- **Used by:** Teo Soon How
- **Format:** one entry per request. Each prompt is quoted exactly as typed. From entry 8 on, Claude's reply is copied in full. Entries 1 to 7 only summarise the reply; the full replies are in the Claude Code session transcript. Local file paths and other personal details are replaced with [brackets].

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

## Before the log started (Sat 31 Oct 2026)

These requests came before the log was started, so their responses are summarised after the fact.

### 1. Wrong project title on GitHub

> why is there a NTU placement for 2003 course? can you resolve and name it properly after 2003 assignment. Do you have my full content for the course from Blackboard in my local folder?

Claude found that the README described an internship placement system from an earlier semester's brief, not this semester's assignment, and checked which course files were available locally.

### 2. Course materials

> [local CT2003 course folder] use this folder to store everything from 2003 [NTULearn course link]

> can you read through what is available first

> i put it here already [local CT2003 course folder]

Claude could not open NTULearn because the browser extension was not connected, so the files were downloaded by hand. Claude then read the assignment brief, the deliverables sheet, the group list, the NTU GAI policy documents and the GradeWay guide.

### 3. README and repository clean-up

> for the assignment can you help me update Github to reflect the correct title first? now some NTU placement internship title

Claude rewrote the README for Restaurant Rush (game rules, stages, repository structure, build commands, submission checklist and marks), removed the old internship data files, added empty folders for the project layout and updated the repository description.

Files: `README.md`, `.gitignore`, `.gitkeep` files in each folder, `data/` removed.

> delete all history and record prior to this

After the user confirmed, Claude replaced the history with a single commit, "Set up Restaurant Rush project structure", and force-pushed it to `main` and `dev`.

> put the final date of submission here too

(Sent with a screenshot of the NTULearn due date.) Claude added the deadline to the README: Fri 13 Nov 2026, 11:59 PM (UTC+8).

> so is this live now?

Claude confirmed the changes were live on GitHub and noted that teammates need to clone the repository again.

### 4. Kickoff deck

> Can you also help me start to study the assignment, list down what needs to be done, propose an allocation to each team member, propose the timeline for us (with enough time to study and review) then propose the milestones for the team. 1st meeting will be today 31st October. make this ppt slide not the [work] template one for sharing later

Claude made a 14-slide kickoff deck covering the brief, the rules that are easy to get wrong, the three stages, the deliverables, a first sketch of the classes, the proposed split of work, a timeline from 31 Oct to 13 Nov and eight milestones.

Output: "Restaurant Rush Kickoff" deck (claude.ai artifact).

### 5. Stage-by-stage plan

> give me the exact details of what needs to be done at which stage, what files etc

Claude listed every file to create or change at each stage with its owner, the tests with their expected numbers, the hand-in folders, how to tag a stage, and five design decisions to settle by Mon 2 Nov.

> make this as a ppt to share with the tema

Claude turned the plan into a 17-slide deck.

Output: "Restaurant Rush Build Plan" deck (claude.ai artifact).

> is this ppt sharable

Claude explained that the decks stay private until they are shared from the Share menu, and that they can be exported as PowerPoint or PDF.

### 6. Team table and Main.java

> ok plan is confirm. can we put the plan consideration simply in readme? just the team member informationw will do.
> You should not help me write any impoant code. to get us started, can you help me write the main file template so that we can use that as a base for each of us to get started working?

Claude added a Group Members table to the README with names, lab group and responsibilities. Matric numbers were left out because the repository is public. Claude also wrote a placeholder `Main.java` that prints a title line and has a TODO for starting the game; it contains no game logic. It compiled and ran with the README's build commands.

Files: `README.md`, `src/main/java/Main.java`.

> ok now base on this base, teach me what I should do

The user interrupted this request before Claude replied.

## Log (from Sat 31 Oct 2026)

### 7. Conversation log

> from now on keep an md file on the conversation we have regarding the project

Claude created this log and will add an entry after each request about the project.

Files: `gai-paper-trail/claude-conversation-log.md`.

### 8. Checking the declarations

> can you understand what was the instruction and the declaration that I will underatke, ensure that I do not breach any of those clauses

Claude read the Declaration of Original Work form, the Declaration on Use of GAI Assistance, NTU's GAI guidelines, the brief and the deliverables sheet. It changed this log to record replies in full and added the "To acknowledge in the submission" list.

Files: `gai-paper-trail/claude-conversation-log.md`.

Claude's reply:

I read both forms you'll sign, NTU's GAI guidelines, the brief and the deliverables sheet. This is my reading; the instructor's word is final.

**What you're agreeing to**

- **Declaration of Original Work** (one form, signed by all four of you): the work is the group's own collective effort, and you upheld academic integrity and the Student Code of Academic Conduct, which includes NTU's GAI rules. Plagiarism means lower or no marks and possible disciplinary action. Names must match your matric cards exactly.
- **Declaration on Use of GAI** (each of you, separately): you tick which GAI use applies (i: key ideas, ii: first text, iii: grammar only, iv: no GAI at all). You also declare that (a) you submitted the full paper trail, (b) every word, paraphrase, significant idea or fact that came from GAI is footnoted, and (c) everything else is your own work.
- **NTU's GAI guidelines**, the rules behind that form:
  - The instructor must state which use is allowed.
  - GAI may never produce the whole submission.
  - The paper trail must hold both your prompts and the GAI's output, and be kept until the end of the next academic year.
  - A missing or late paper trail can be presumed improper, and can itself be a violation.
  - You may be questioned on the work orally.

**Where you could breach them**

Nothing is breached yet, because the declarations are signed at submission. These are the gaps to close before then:

1. **Nobody has said what GAI use is allowed.** Neither the brief nor the deliverables sheet states it. You can't tick option iv, because you've already used me. Ask the instructor in writing which option applies, and say what you've used me for.
2. **I wrote some of what you'll submit:** the README text, which is already public on GitHub, and the placeholder `Main.java`. If only ideas are allowed (option i), rewrite them in your own words. If first drafts are allowed (option ii), revise them yourselves. Either way, acknowledge them.
3. **My ideas will end up in the design.** The class structure, the food-choice rule, the Critic options, the test cases and the stage plan came from me. Whatever the team adopts needs a footnote in the report.
4. **The paper trail is incomplete.** The log only summarises my earlier replies. The full replies exist only in Claude Code's transcript on this computer, which Claude Code deletes after 30 days by default.
5. **Your teammates sign the originality form too.** Tell them what you've used me for, so they sign knowing it and each declare their own GAI use honestly.

**What I've changed**

- From now on the log records my replies in full, not as summaries. This reply is entry 8 of the log.
- The top of the log now lists everything that needs acknowledging in the submission.
- Until the instructor answers, I'll explain, review and answer questions, but I won't write code, README text or report text for the submission. If a request would cross that line, I'll say so before doing anything.

I can draft the email to the instructor, and copy the full session transcript to a private folder on your OneDrive, outside the public repo, so it's kept. Do you want either?
