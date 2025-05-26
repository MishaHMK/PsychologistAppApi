package psychologist.project.dto.user;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fatherName;
    private byte[] profileImage;
    private LocalDate birthDate;
    private String role;
}
