package psychologist.project.dto.psychologist;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PagedPsychologistDto {
    private List<PsychologistWithDetailsDto> psychologists;
    private Integer count;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
}
