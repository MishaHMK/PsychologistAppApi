package psychologist.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePsychologistDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    private String fatherName;
    private String phoneNumber;
    private String email;
    private String introduction;
    @NotNull(message = "Speciality id is required")
    private Long specialityId;
    @NotNull(message = "Specialist gender is required")
    private String gender;
}
