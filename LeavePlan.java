import java.time.LocalDate;

public final class LeavePlan {
    private final int planId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String reason;
    private final int days;
    private final LeaveStatus status;

    LeavePlan(int planId, LocalDate startDate, LocalDate endDate,
            String reason, int days, LeaveStatus status) {
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.days = days;
        this.status = status;
    }

    public int getPlanId() {
        return planId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getDays() {
        return days;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    LeavePlan withStatus(LeaveStatus newStatus) {
        return new LeavePlan(planId, startDate, endDate, reason, days, newStatus);
    }

    @Override
    public String toString() {
        return "#" + planId + " | " + startDate + " to " + endDate
                + " | " + days + " day(s) | " + status + " | " + reason;
    }
}
