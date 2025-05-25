package psychologist.project.service.user;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.booking.UnauthorizedBookingDto;
import psychologist.project.dto.user.UpdateUserDataDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.exception.AccessException;
import psychologist.project.exception.RegistrationException;
import psychologist.project.mapper.UserMapper;
import psychologist.project.model.User;
import psychologist.project.repository.user.UserRepository;
import psychologist.project.security.SecurityUtil;
import psychologist.project.service.email.MessageSenderService;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MessageSenderService messageSender;

    @Override
    public UserRegisterResponseDto save(UserRegisterRequestDto requestDto) {
        String email = requestDto.getEmail();
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            checkUserRegistered(byEmail.get());
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (byEmail.isPresent() && byEmail.get().getPassword() == null) {
            userRepository.save(user.setId(byEmail.get().getId()));
        } else {
            userRepository.save(user);
        }
        UserRegisterResponseDto response = userMapper.toResponse(user);
        messageSender.onRegistered(response);
        return response;
    }

    @Override
    public User registerUnauthorized(UnauthorizedBookingDto createDto) {
        checkUserRegisteredByEmail(createDto.getEmail());
        User user = new User()
                .setEmail(createDto.getEmail())
                .setFirstName(createDto.getFirstName())
                .setLastName(createDto.getLastName());
        return userRepository.save(user);
    }

    private void checkUserRegisteredByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && !byEmail.get().getPassword().isEmpty()) {
            throw new RegistrationException("User with email "
                    + email + " is already registered");
        }
    }

    private void checkUserRegistered(User user) {
        if (user.getPassword() != null) {
            throw new RegistrationException("User with email "
                    + user.getEmail() + " is already registered");
        }
    }

    @Override
    public UserDto getCurrentUserData() {
        return userMapper.toUserDto(SecurityUtil.getLoggedInUser());
    }

    @Override
    public void deleteUser() {
        User user = SecurityUtil.getLoggedInUser();
        userRepository.deleteById(user.getId());
    }

    @Override
    public UserDto updateUser(UpdateUserDataDto updateDto) {
        User user = userRepository.findById(SecurityUtil.getLoggedInUserId())
                .orElseThrow(
                        () -> new AccessException("You're not logged in")
                );
        updateIfPresent(updateDto.getEmail(), user::setEmail);
        updateIfPresent(updateDto.getFirstName(), user::setFirstName);
        updateIfPresent(updateDto.getLastName(), user::setLastName);
        updateIfPresent(updateDto.getFatherName(), user::setFatherName);
        updateIfPresent(updateDto.getImageUrl(), user::setImageUrl);
        if (updateDto.getBirthDate() != null) {
            user.setBirthDate(updateDto.getBirthDate());
        }
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    private void updateIfPresent(String value, Consumer<String> setter) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }
}
