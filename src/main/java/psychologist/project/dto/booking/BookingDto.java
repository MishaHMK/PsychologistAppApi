package psychologist.project.dto.booking;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookingDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String meetingUrl;
    private Long psychologistId;
    private Long userId;
    private String status;
}
