package psychologist.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;
import psychologist.project.annotations.UniqueValue;
import psychologist.project.model.Psychologist;

@Data
@Accessors(chain = true)
public class CreatePsychologistDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private String fatherName;
    @UniqueValue(entity = Psychologist.class, fieldName = "phoneNumber",
            message = "Phone number must be unique")
    private String phoneNumber;
    @UniqueValue(entity = Psychologist.class, fieldName = "email",
            message = "Email must be unique")
    @Email
    private String email;
    @NotNull(message = "Session price is required")
    private BigDecimal sessionPrice;
    private String introduction;
    @NotNull(message = "Speciality id is required")
    private Long specialityId;
    @NotNull(message = "Specialist gender is required")
    private String gender;
}
