package psychologist.project.dto.psychologist;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;
import psychologist.project.model.Speciality;

@Data
@Accessors(chain = true)
public class PsychologistDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String phoneNumber;
    private String email;
    private String introduction;
    private Speciality speciality;
    private BigDecimal sessionPrice;
    private String gender;
}
