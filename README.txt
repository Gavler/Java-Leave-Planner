JAVA LEAVE PLANNER - STUDENT PROJECT

Requirements: JDK 8 or later. Verified with Java 17.

HOW TO RUN
Extract this ZIP. Open a terminal in LeavePlannerProject, then run:
    javac *.java
    java Main
For an IDE, create a Java project, add all seven .java files to the same source
folder without a package declaration, and run Main.java.

FILES AND RESPONSIBILITIES
Employee.java: Stores an employee ID and name.
Holiday.java: Stores a named, observed holiday date and its printable form.
HolidayCalendar.java: Sorts and filters holidays, identifies Monday-Friday
working dates, and counts the leave days in an inclusive date range.
LeaveStatus.java: Restricts status values to PLANNED, USED, and CANCELLED.
LeavePlan.java: Holds an immutable record of one leave period. A status change
creates a replacement record; it never edits a record the user already holds.
LeavePlanner.java: Manages one employee's yearly allowance, opening used days,
plans, validation rules, cancellations, completed leave, and balance calculations.
Main.java: Reads keyboard input, builds objects, displays the menu, and calls
the matching planner methods.

LINE-BY-LINE READING GUIDE
The Java source files contain comments beside meaningful statements.
An opening brace begins a class, method, loop, condition, or other block.
A closing brace ends that block. Blank lines separate related pieces of code.
A semicolon ends a statement; a comment beginning with // is not executed.
public: The class or method can be accessed by other classes.
private: Only code inside the declaring class can directly access this member.
No access modifier: Package-private; other classes in the same package may call it.
final field: The field cannot be reassigned after initialization. It does not,
by itself, prevent changing the contents of a collection stored in that field.
final class: Other classes cannot subclass it.
static: The member belongs to the class, so no object is needed to call it.
void: The method returns no value.
this: The current object; this.name distinguishes its field from a parameter.
new: Constructs an object.
return: Ends a method and optionally sends a value to its caller.
throw: Stops the operation by raising an exception.
try/catch: Attempts an operation and handles a specified kind of failure.
@Override: Declares that a method replaces an inherited method.
List<Holiday>: A list whose elements are Holiday objects.
for (Holiday holiday : holidays): Process each holiday in the list.
&&: Both conditions must be true. ||: At least one must be true. !: Logical NOT.
++ increases an integer by one. += adds a value to an existing total.
condition ? a : b chooses a when the condition is true, otherwise b.
Holiday::getDate supplies a method reference that extracts dates for sorting.
== compares primitive values, enum constants, or object references. Use equals()
to compare LocalDate values. Main intentionally uses == to select a planner object.

STARTUP PROCESS
1. Read the computer's current year, employee ID, and name.
2. Read workplace-observed holiday names and dates for this year and next year.
3. Copy and sort the holiday list into a calendar that cannot change this session.
4. Read this year's allowance and days already used.
5. Read next year's allowance separately; next year's used days start at zero.
6. Create two LeavePlanner objects. Select this year's planner initially.
7. Repeat the menu until Exit is selected.

ADDING LEAVE
Main reads start date, end date, and reason, then calls active.planLeave(...).
The planner checks that dates are ordered and belong to its year, the reason is
not blank, and no non-cancelled plan overlaps the requested inclusive date range.
The calendar counts each date, charging one day for Monday-Friday unless that
date is in the entered holiday list. It does not automatically shift weekend
holidays; enter the actual observed date.
The planner rejects a zero-working-day period or insufficient available days.
It assigns an ID, stores a PLANNED record, and returns the record for display.

BALANCE CALCULATIONS
Used = initial used days + days in records whose status is USED.
Remaining = annual allowance - used.
Planned = days in records whose status is PLANNED.
Available to plan = remaining - planned.
Example: allowance 15, initial used 4, new plan 3 -> remaining 11, available 8.
Cancelling that plan -> remaining 11, available 11.
Completing it instead -> used 7, remaining 8, planned 0, available 8.

STATUS CHANGES
cancelLeave(id): Find the plan, require PLANNED, replace with CANCELLED.
markLeaveUsed(id): Find the plan, require PLANNED and an end date before today,
then replace with USED. Repeating either action is rejected.
The totals are recalculated from statuses, so completion does not charge twice.
Call getPlans() again to see changed statuses; previously returned objects are
immutable snapshots, not automatically updated live records.

HOLIDAY SEARCH
Menu 1 lists entered holidays on or after today in the current year.
Menu 2 lists all entered holidays next year.
These menu choices always refer to the computer's current year, regardless of
the selected planner. Balance, add, view, cancel, and complete use the selected
year. Menu 8 switches the selected year.

SCOPE AND ASSUMPTIONS
- Holidays are manually entered, not fetched from an official calendar.
- Work is Monday-Friday; leave is measured in whole days.
- Calendars are fixed after startup so existing charges remain consistent.
- All data is in memory and disappears when the program exits.
- There is no approval workflow, carryover, database, login, or automatic completion.
- Cross-year leave must be split into two plans, one for each year.
- Plan IDs are unique within a year. Select the correct year before using an ID.
- Past dates are accepted to let the user record completed leave, then mark it
  used. Do not enter leave already included in the opening used-day count.
- Opening used days contain no dates, so overlap checking applies only to plans
  entered during the session.
- The dates and year come from the computer's local clock.

VALIDATION PERFORMED
All seven application files compiled with Java 17 and -Xlint:all.
26 focused domain checks passed, including holiday/weekend counting, inclusive
dates, sorted-year filtering, read-only access, overlap rejection, date validation,
insufficient balance, cancellation, rebooking, repeated completion, future-date
completion rejection, and independent yearly balances.
A console scenario passed for malformed number/date retries, year switching,
holiday exclusion, plan display, cancellation, balance display, and exit.
