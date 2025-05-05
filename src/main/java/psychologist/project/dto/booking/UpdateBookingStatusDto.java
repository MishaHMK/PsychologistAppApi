package psychologist.project.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import psychologist.project.model.Booking;

@Data
@Accessors(chain = true)
public class UpdateBookingStatusDto {
    @NotNull(message = "New status is required")
    private Booking.BookingStatus status;
}
