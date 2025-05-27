package psychologist.project.service.user;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.booking.UnauthorizedBookingDto;
import psychologist.project.dto.user.UpdateUserDataDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.exception.RegistrationException;
import psychologist.project.model.User;

public interface UserService {
    UserRegisterResponseDto save(UserRegisterRequestDto userRegisterRequestDto)
            throws RegistrationException;

    User registerUnauthorized(UnauthorizedBookingDto createDto) throws RegistrationException;

    UserDto getCurrentUserData();

    UserDto updateUser(UpdateUserDataDto updateDto);

    UserDto updateImage(byte[] imageData);

    UserDto updateImage(MultipartFile imageData) throws IOException;

    void deleteUser();
}
