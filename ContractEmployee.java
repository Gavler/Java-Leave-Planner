import java.time.LocalDate;
import java.util.Objects;

public final class ContractEmployee extends Employee {
    private final LocalDate contractStartDate;
    private final LocalDate contractEndDate;

    public ContractEmployee(String employeeId, String name, String department,
            LocalDate contractStartDate, LocalDate contractEndDate) {
        super(employeeId, name, department);
        this.contractStartDate = Objects.requireNonNull(
                contractStartDate, "Contract start date is required.");
        this.contractEndDate = Objects.requireNonNull(
                contractEndDate, "Contract end date is required.");
        if (contractEndDate.isBefore(contractStartDate)) {
            throw new IllegalArgumentException(
                    "Contract end date cannot be before its start date.");
        }
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    @Override
    public String getEmployeeType() {
        return "Contract Employee";
    }

    @Override
    public boolean isEligibleForLeave(LocalDate start, LocalDate end) {
        return !start.isBefore(contractStartDate) && !end.isAfter(contractEndDate);
    }

    @Override
    public void displaySummary() {
        super.displaySummary();
        System.out.println("Contract starts: " + contractStartDate);
        System.out.println("Contract ends: " + contractEndDate);
    }
}
