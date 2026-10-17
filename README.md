# CT2003 Group 3: Internship Placement Management System

CT2003 Object-Oriented Design & Programming, AY2025/2026 Semester 1, CCDS, NTU.

This is a Java command-line app that acts as one central hub for **Students**, **Company Representatives** and **Career Center Staff**. The system manages internship opportunities, applications and placements.

## Group Members

| Name (as on matric card) | Matric No. | Lab Group | Role |
| ------------------------ | ---------- | --------- | ---- |
| _TBC_                    |            |           |      |
| _TBC_                    |            |           |      |
| _TBC_                    |            |           |      |
| _TBC_                    |            |           |      |
| _TBC_                    |            |           |      |

## Repository Structure

```
.
├── src/main/java/        Java source code (package layout follows the ECB pattern)
│   ├── entity/           Entity classes (User, Student, CompanyRepresentative, CareerCenterStaff,
│   │                     Internship, Application, ...)
│   ├── control/          Control/manager classes (business logic)
│   ├── boundary/         Boundary classes (CLI menus / UI)
│   ├── enums/            Enumerations (InternshipLevel, InternshipStatus, ApplicationStatus, ...)
│   └── util/             Helpers (CSV file I/O, input validation, ...)
├── data/                 CSV data files loaded at start-up (no database, JSON or XML allowed)
├── docs/javadoc/         Generated Javadoc HTML
├── diagrams/             UML class diagram (Visual Paradigm project + exported images)
├── report/               Main report (max 12 pages, Times New Roman 12pt, 1.5 spacing)
├── forms/                Signed Declaration of Original Work and Declaration on Use of GAI
├── testing/              Test cases and results (see Appendix A of the brief)
└── gai-paper-trail/      Prompts and outputs from any GAI use (required by the NTU GAI policy)
```

## Build & Run

Requires JDK 17 or later.

```bash
# compile
javac -d out $(find src/main/java -name "*.java")

# run (from the repo root, so that data/ resolves)
java -cp out Main

# generate Javadoc
javadoc -d docs/javadoc -sourcepath src/main/java -subpackages entity:control:boundary:enums:util
```

## Default Login

- **Students:** ID such as `U2310001A` (U + 7 digits + a letter)
- **Career Center Staff:** NTU account, such as `sng001`
- **Company Representatives:** company email (they must register and be approved by staff before they can log in)
- **Default password:** `password`

## Key Dates

| Item | Date |
| ---- | ---- |
| Submission (zip on NTULearn) | One day before the presentation (**Fri, 14 Nov**) |
| Demo & presentation (max 15 min, every member presents, face on camera) | **Week 13, Sat 15 Nov** |

Late submissions lose 3 marks per calendar day.

## Submission Checklist

Zip everything as `CT2003_<AssignmentGroupNumber>.zip`. The brief also uses the format `<lab_grp>-grp3.<ext>`, so confirm with the TA which one applies. Only one submission is needed per group.

- [ ] Implementation code and Javadoc
- [ ] Main report, with a separate diagram file if the diagram is unclear in the report
  - [ ] Cover page: Declaration of Original Work (Appendix B)
  - [ ] Design considerations (OO principles, design patterns, trade-offs, assumptions)
  - [ ] Detailed UML class diagram, annotated with where the OO principles apply
  - [ ] Additional features, clearly highlighted
  - [ ] Testing: test cases and results
  - [ ] Reflection
  - [ ] Link to this GitHub repository
- [ ] Data files
- [ ] Declaration of Original Work form (signed)
- [ ] Declaration on Use of GAI form, plus the paper trail
- [ ] Setup instructions and other relevant files
- [ ] (Optional) WBS.xls if contributions differ, emailed to Dr. Li Fang and the TA

## Marking Weightage

| Component | Marks |
| --------- | ----- |
| UML Class Diagram | 30 |
| Design Consideration | 20 |
| Implementation Code | 25 |
| Demonstration & Report | 25 |
