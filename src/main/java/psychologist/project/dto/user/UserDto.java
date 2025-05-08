package psychologist.project.dto.user;

import java.time.LocalDate;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String fatherName,
        LocalDate birthDate,
        String role) {
}
