package psychologist.project.dto.user;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateUserDataDto {
    private String email;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String imageUrl;
    private LocalDate birthDate;
}
