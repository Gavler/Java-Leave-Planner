import java.time.LocalDate;

public abstract class Employee implements Reportable {
    private final String employeeId;
    private String name;
    private String department;

    protected Employee(String employeeId, String name, String department) {
        this.employeeId = requireText(employeeId, "Employee ID");
        this.name = requireText(name, "Name");
        this.department = requireText(department, "Department");
    }

    private static String requireText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be blank.");
        }
        return value.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireText(name, "Name");
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = requireText(department, "Department");
    }

    public abstract String getEmployeeType();

    public abstract boolean isEligibleForLeave(LocalDate start, LocalDate end);

    @Override
    public void displaySummary() {
        System.out.println("\n--- EMPLOYEE INFORMATION ---");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + name);
        System.out.println("Department: " + department);
        System.out.println("Employee type: " + getEmployeeType());
    }
}
