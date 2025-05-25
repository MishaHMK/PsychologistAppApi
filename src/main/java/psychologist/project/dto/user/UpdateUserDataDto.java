package psychologist.project.dto.user;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UpdateUserDataDto {
    private String email;
    private String firstName;
    private String lastName;
    private String fatherName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private byte[] profileImage;
    //private String imageUrl;
    private LocalDate birthDate;
}
