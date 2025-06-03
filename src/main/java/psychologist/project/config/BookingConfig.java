package psychologist.project.config;

import jakarta.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class BookingConfig {
    @Value("${booking.start-time}")
    private String startTimeString;

    @Value("${booking.end-time}")
    private String endTimeString;

    private LocalTime startTime;

    private LocalTime endTime;

    @Value("${booking.slot-duration-minutes}")
    private long slotLength;

    @Value("${booking.session-duration-minutes}")
    private long sessionLength;

    @Value("${booking.working-days}")
    private List<String> workingDaysRaw;

    private List<DayOfWeek> workingDays;

    @Value("${payment.redirect.authorized}")
    private String authorizedRedirect;

    @Value("${payment.redirect.unauthorized}")
    private String unauthorizedRedirect;

    @PostConstruct
    public void init() {
        this.startTime = LocalTime.parse(startTimeString);
        this.endTime = LocalTime.parse(endTimeString);

        this.workingDays = workingDaysRaw
                .stream()
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .toList();
    }
}
