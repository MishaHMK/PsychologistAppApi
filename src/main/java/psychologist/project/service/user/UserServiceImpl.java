package psychologist.project.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.booking.UnauthorizedBookingDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.exception.RegistrationException;
import psychologist.project.mapper.UserMapper;
import psychologist.project.model.User;
import psychologist.project.repository.bookings.BookingRepository;
import psychologist.project.repository.user.UserRepository;
import psychologist.project.security.SecurityUtil;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserRegisterResponseDto save(UserRegisterRequestDto requestDto) {
        String email = requestDto.getEmail();
        checkUserRegistered(email);
        /* if (requestDto.getPassword() != null) {
            Optional<User> byEmail = userRepository.findByEmail(email);
            if (byEmail.isPresent() && ) {

            }
        }*/
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public User registerUnauthorized(UnauthorizedBookingDto createDto) {
        checkUserRegistered(createDto.getEmail());
        User user = new User()
                .setEmail(createDto.getEmail())
                .setFirstName(createDto.getFirstName())
                .setLastName(createDto.getLastName());
        return userRepository.save(user);
    }

    private void checkUserRegistered(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && !byEmail.get().getPassword().isEmpty()) {
            throw new RegistrationException("User with email "
                    + email+ " already registered");
        }
    }

    @Override
    public UserDto getCurrentUserData() {
        return userMapper.toUserDto(SecurityUtil.getLoggedInUser());
    }
}
