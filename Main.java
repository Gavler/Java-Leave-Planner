import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        int thisYear = LocalDate.now().getYear();
        System.out.println("===== EMPLOYEE LEAVE PLANNER =====");
        Employee employee = createEmployee();
        HolidayCalendar calendar = createHolidayCalendar(thisYear);
        int allowance = readInt("This year's annual leave allowance: ", 0, 366);
        int usedDays = readInt("Days already used this year: ", 0, allowance);
        int nextAllowance = readInt("Next year's annual leave allowance: ", 0, 366);
        LeavePlanner currentPlanner = new LeavePlanner(
                employee, thisYear, allowance, usedDays, calendar);
        LeavePlanner nextPlanner = new LeavePlanner(
                employee, thisYear + 1, nextAllowance, 0, calendar);
        LeavePlanner activePlanner = currentPlanner;
        boolean running = true;

        while (running) {
            displayMenu(activePlanner.getYear());
            int choice = readInt("Enter your choice: ", 0, 9);
            try {
                switch (choice) {
                    case 1:
                        printHolidays(calendar.getUpcomingHolidays(LocalDate.now(), thisYear));
                        break;
                    case 2:
                        printHolidays(calendar.getHolidaysForYear(thisYear + 1));
                        break;
                    case 3:
                        Reportable[] reports = {employee, activePlanner};
                        for (Reportable report : reports) {
                            report.displaySummary();
                        }
                        break;
                    case 4:
                        LocalDate start = readDate("Leave start date (YYYY-MM-DD): ");
                        LocalDate end = readDate("Leave end date (YYYY-MM-DD): ");
                        String reason = readText("Reason: ");
                        LeavePlan newPlan = activePlanner.planLeave(start, end, reason);
                        System.out.println("Leave plan created:");
                        System.out.println(newPlan);
                        break;
                    case 5:
                        List<LeavePlan> plans = activePlanner.getPlans();
                        if (plans.isEmpty()) {
                            System.out.println("No leave plans for the selected year.");
                        } else {
                            for (LeavePlan plan : plans) {
                                System.out.println(plan);
                            }
                        }
                        break;
                    case 6:
                        int cancelId = readInt("Enter the plan ID to cancel: ", 1, Integer.MAX_VALUE);
                        activePlanner.cancelLeave(cancelId);
                        System.out.println("Leave plan cancelled.");
                        break;
                    case 7:
                        int usedId = readInt("Enter the completed plan ID: ", 1, Integer.MAX_VALUE);
                        activePlanner.markLeaveUsed(usedId);
                        System.out.println("Leave marked as used.");
                        break;
                    case 8:
                        if (activePlanner == currentPlanner) {
                            activePlanner = nextPlanner;
                        } else {
                            activePlanner = currentPlanner;
                        }
                        System.out.println("Selected year: " + activePlanner.getYear());
                        break;
                    case 9:
                        employee.setName(readText("Enter the updated name: "));
                        employee.setDepartment(readText("Enter the updated department: "));
                        System.out.println("Employee profile updated.");
                        break;
                    case 0:
                        running = false;
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException error) {
                System.out.println("Cannot complete: " + error.getMessage());
            }
        }
        input.close();
        System.out.println("Goodbye! Session data was not saved.");
    }

    private static Employee createEmployee() {
        String employeeId = readText("Employee ID: ");
        String name = readText("Employee name: ");
        String department = readText("Department: ");
        System.out.println("\nChoose employee type:");
        System.out.println("1. Regular Employee");
        System.out.println("2. Contract Employee");
        int type = readInt("Employee type: ", 1, 2);
        if (type == 1) {
            return new RegularEmployee(employeeId, name, department);
        } else {
            while (true) {
                LocalDate contractStart = readDate("Contract start date (YYYY-MM-DD): ");
                LocalDate contractEnd = readDate("Contract end date (YYYY-MM-DD): ");
                if (contractEnd.isBefore(contractStart)) {
                    System.out.println("Contract end date cannot be before its start date.");
                } else {
                    return new ContractEmployee(
                            employeeId, name, department, contractStart, contractEnd);
                }
            }
        }
    }

    private static HolidayCalendar createHolidayCalendar(int thisYear) {
        List<Holiday> holidays = new ArrayList<>();
        System.out.println("\nEnter workplace holidays for " + thisYear + " and " + (thisYear + 1) + ".");
        System.out.println("Holidays are entered manually; no official list is downloaded.");
        int count = readInt("Number of holiday entries: ", 0, 1000);
        for (int i = 0; i < count; i++) {
            String name = readText("Holiday name: ");
            LocalDate date = readDate("Observed holiday date (YYYY-MM-DD): ");
            if (date.getYear() != thisYear && date.getYear() != thisYear + 1) {
                System.out.println("Use a date in this year or next year.");
                i--;
                continue;
            }
            holidays.add(new Holiday(name, date));
        }
        return new HolidayCalendar(holidays);
    }

    private static void displayMenu(int year) {
        System.out.println("\n===== LEAVE PLANNER | SELECTED YEAR: " + year + " =====");
        System.out.println("1. View upcoming holidays this year");
        System.out.println("2. View holidays next year");
        System.out.println("3. View employee summary and leave balance");
        System.out.println("4. Add a leave plan");
        System.out.println("5. View leave plans");
        System.out.println("6. Cancel a leave plan");
        System.out.println("7. Mark leave as used");
        System.out.println("8. Switch year");
        System.out.println("9. Update employee name and department");
        System.out.println("0. Exit");
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = input.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be blank.");
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(readText(prompt));
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException error) {
            }
            System.out.println("Enter a whole number from " + min + " to " + max + ".");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(readText(prompt));
            } catch (DateTimeParseException error) {
                System.out.println("Enter a valid date in YYYY-MM-DD format.");
            }
        }
    }

    private static void printHolidays(List<Holiday> holidays) {
        if (holidays.isEmpty()) {
            System.out.println("No matching holidays entered.");
        } else {
            for (Holiday holiday : holidays) {
                System.out.println(holiday);
            }
        }
    }
}
