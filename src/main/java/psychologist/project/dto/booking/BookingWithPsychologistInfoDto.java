package psychologist.project.dto.booking;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;

@Data
@Accessors(chain = true)
public class BookingWithPsychologistInfoDto implements Serializable {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String meetingUrl;
    private PsychologistWithDetailsDto psychologistDto;
    private Long userId;
    private String status;
}
