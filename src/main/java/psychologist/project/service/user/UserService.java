package psychologist.project.service.user;

import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.booking.UnauthorizedBookingDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.exception.RegistrationException;
import psychologist.project.model.User;

public interface UserService {
    UserRegisterResponseDto save(UserRegisterRequestDto userRegisterRequestDto)
            throws RegistrationException;

    User registerUnauthorized(UnauthorizedBookingDto createDto) throws RegistrationException;

    UserDto getCurrentUserData();

    void deleteUserById(Long id);
}
