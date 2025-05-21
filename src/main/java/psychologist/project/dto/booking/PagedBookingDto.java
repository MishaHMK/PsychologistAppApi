package psychologist.project.dto.booking;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;

@Data
@Accessors(chain = true)
public class PagedBookingDto {
    private List<PsychologistWithDetailsDto> psychologists;
    private Integer count;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
}
