package psychologist.project.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PsychologistFilterDto {
    private String firstName;
    private String gender;
    private Long specialityId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
