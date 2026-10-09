import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class LeavePlanner implements Reportable {
    private final Employee employee;
    private final int year;
    private final int annualAllowance;
    private final int initialUsedDays;
    private final HolidayCalendar calendar;
    private final List<LeavePlan> plans = new ArrayList<>();
    private int nextId = 1;

    public LeavePlanner(Employee employee, int year, int annualAllowance,
            int initialUsedDays, HolidayCalendar calendar) {
        if (annualAllowance < 0 || initialUsedDays < 0 || initialUsedDays > annualAllowance) {
            throw new IllegalArgumentException(
                    "Used days must be between zero and the annual allowance.");
        }
        LocalDate.of(year, 1, 1);
        this.employee = Objects.requireNonNull(employee, "Employee is required.");
        this.calendar = Objects.requireNonNull(calendar, "Holiday calendar is required.");
        this.year = year;
        this.annualAllowance = annualAllowance;
        this.initialUsedDays = initialUsedDays;
    }

    public int getYear() {
        return year;
    }

    public int getAnnualAllowance() {
        return annualAllowance;
    }

    public int getUsedDays() {
        int total = initialUsedDays;
        for (LeavePlan plan : plans) {
            if (plan.getStatus() == LeaveStatus.USED) {
                total += plan.getDays();
            }
        }
        return total;
    }

    public int getPlannedDays() {
        int total = 0;
        for (LeavePlan plan : plans) {
            if (plan.getStatus() == LeaveStatus.PLANNED) {
                total += plan.getDays();
            }
        }
        return total;
    }

    public int getRemainingDays() {
        return annualAllowance - getUsedDays();
    }

    public int getAvailableToPlan() {
        return getRemainingDays() - getPlannedDays();
    }

    public LeavePlan planLeave(LocalDate start, LocalDate end, String reason) {
        Objects.requireNonNull(start, "Start date is required.");
        Objects.requireNonNull(end, "End date is required.");
        if (start.getYear() != year || end.getYear() != year) {
            throw new IllegalArgumentException("Both leave dates must be within " + year + ".");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("A reason is required.");
        }
        if (!employee.isEligibleForLeave(start, end)) {
            throw new IllegalArgumentException(
                    "The leave dates are outside the employee's contract period.");
        }
        for (LeavePlan plan : plans) {
            boolean overlaps = !end.isBefore(plan.getStartDate())
                    && !start.isAfter(plan.getEndDate());
            if (plan.getStatus() != LeaveStatus.CANCELLED && overlaps) {
                throw new IllegalArgumentException("Dates overlap plan #" + plan.getPlanId() + ".");
            }
        }
        int days = calendar.countLeaveDays(start, end);
        if (days == 0) {
            throw new IllegalArgumentException(
                    "The selected period contains no chargeable working days.");
        }
        if (days > getAvailableToPlan()) {
            throw new IllegalArgumentException("Insufficient leave. Required: " + days
                    + "; available: " + getAvailableToPlan() + ".");
        }
        LeavePlan plan = new LeavePlan(nextId++, start, end, reason.trim(), days,
                LeaveStatus.PLANNED);
        plans.add(plan);
        return plan;
    }

    private int findPlanIndex(int planId) {
        for (int i = 0; i < plans.size(); i++) {
            if (plans.get(i).getPlanId() == planId) {
                return i;
            }
        }
        throw new IllegalArgumentException("Plan ID not found in " + year + ".");
    }

    public void cancelLeave(int planId) {
        int index = findPlanIndex(planId);
        LeavePlan plan = plans.get(index);
        if (plan.getStatus() != LeaveStatus.PLANNED) {
            throw new IllegalStateException("Only planned leave can be cancelled.");
        }
        plans.set(index, plan.withStatus(LeaveStatus.CANCELLED));
    }

    public void markLeaveUsed(int planId) {
        int index = findPlanIndex(planId);
        LeavePlan plan = plans.get(index);
        if (plan.getStatus() != LeaveStatus.PLANNED) {
            throw new IllegalStateException("Only planned leave can be marked used.");
        }
        if (!plan.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Mark leave used after its end date.");
        }
        plans.set(index, plan.withStatus(LeaveStatus.USED));
    }

    public List<LeavePlan> getPlans() {
        return Collections.unmodifiableList(new ArrayList<>(plans));
    }

    public void displayBalance() {
        System.out.println("\n--- LEAVE BALANCE ---");
        System.out.println("Employee: " + employee.getName());
        System.out.println("Year: " + year);
        System.out.println("Annual allowance: " + annualAllowance);
        System.out.println("Used leave: " + getUsedDays());
        System.out.println("Remaining leave: " + getRemainingDays());
        System.out.println("Planned leave: " + getPlannedDays());
        System.out.println("Available to plan: " + getAvailableToPlan());
    }

    @Override
    public void displaySummary() {
        displayBalance();
    }
}
