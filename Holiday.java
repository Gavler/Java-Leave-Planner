import java.time.LocalDate;
import java.util.Objects;

public final class Holiday {
    private final String name;
    private final LocalDate date;

    public Holiday(String name, LocalDate date) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Holiday name is required.");
        }
        this.name = name.trim();
        this.date = Objects.requireNonNull(date, "Holiday date is required.");
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + " - " + name;
    }
}
