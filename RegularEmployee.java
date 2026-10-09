import java.time.LocalDate;

public final class RegularEmployee extends Employee {
    public RegularEmployee(String employeeId, String name, String department) {
        super(employeeId, name, department);
    }

    @Override
    public String getEmployeeType() {
        return "Regular Employee";
    }

    @Override
    public boolean isEligibleForLeave(LocalDate start, LocalDate end) {
        return true;
    }
}
