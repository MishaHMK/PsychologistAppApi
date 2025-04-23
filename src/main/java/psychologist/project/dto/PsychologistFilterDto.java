package psychologist.project.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PsychologistFilterDto {
    private String firstName;
    private String gender;
    private Long specialityId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}