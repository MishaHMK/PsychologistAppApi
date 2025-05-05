package psychologist.project.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CreateBookingDto {
    @NotNull(message = "Meeting date is required")
    @Future(message = "Booking date must be in the future")
    private LocalDateTime startTime;
    @NotNull(message = "Psychologist id is required")
    private Long psychologistId;
    private Long userId;
}
