package psychologist.project.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import psychologist.project.model.Speciality;

@Data
@Accessors(chain = true)
public class UpdatePsychologistDto {
    private String firstName;
    private String lastName;
    private String fatherName;
    private String phoneNumber;
    private String email;
    private String introduction;
    private Long specialityId;
    private String gender;
}
