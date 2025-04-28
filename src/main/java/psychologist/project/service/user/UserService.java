package psychologist.project.service.user;

import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.exception.RegistrationException;

public interface UserService {
    UserRegisterResponseDto save(UserRegisterRequestDto userRegisterRequestDto)
            throws RegistrationException;

    UserDto getCurrentUserData();
}
