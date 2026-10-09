import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class HolidayCalendar {
    private final List<Holiday> holidays;

    public HolidayCalendar(List<Holiday> holidays) {
        List<Holiday> copy = new ArrayList<>(holidays);
        copy.sort(Comparator.comparing(Holiday::getDate));
        this.holidays = Collections.unmodifiableList(copy);
    }

    public List<Holiday> getHolidaysForYear(int year) {
        List<Holiday> result = new ArrayList<>();
        for (Holiday holiday : holidays) {
            if (holiday.getDate().getYear() == year) {
                result.add(holiday);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public List<Holiday> getUpcomingHolidays(LocalDate from, int year) {
        List<Holiday> result = new ArrayList<>();
        for (Holiday holiday : getHolidaysForYear(year)) {
            if (!holiday.getDate().isBefore(from)) {
                result.add(holiday);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public boolean isWorkingDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return false;
        }
        for (Holiday holiday : holidays) {
            if (holiday.getDate().equals(date)) {
                return false;
            }
        }
        return true;
    }

    public int countLeaveDays(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        int total = 0;
        LocalDate date = start;
        while (true) {
            if (isWorkingDay(date)) {
                total++;
            }
            if (date.equals(end)) {
                break;
            }
            date = date.plusDays(1);
        }
        return total;
    }
}
