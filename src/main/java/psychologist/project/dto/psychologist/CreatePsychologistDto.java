package psychologist.project.dto.psychologist;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import psychologist.project.annotations.EnumValue;
import psychologist.project.annotations.UniqueValue;
import psychologist.project.model.Gender;
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
    private String introduction;
    @NotNull(message = "Speciality id is required")
    private Long specialityId;
    @EnumValue(enumClass = Gender.class,
            message = "Gender must be one of: MALE, FEMALE, OTHER")
    @NotNull(message = "Specialist gender is required")
    private String gender;
    @NotNull(message = "Experience (years) is required")
    private Integer experience;
    @NotNull(message = "Languages info is required")
    private String languages;
    @NotNull(message = "Education info is required")
    private String education;
    private String imageUrl;
    @NotNull(message = "Meeting url is required")
    private String meetingUrl;
    @NotEmpty
    private Set<Long> concernIds;
    @NotEmpty
    private Set<Long> approachIds;
}
