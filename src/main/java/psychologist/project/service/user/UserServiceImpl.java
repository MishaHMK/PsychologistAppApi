package psychologist.project.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.exception.RegistrationException;
import psychologist.project.mapper.UserMapper;
import psychologist.project.model.User;
import psychologist.project.repository.user.UserRepository;
import psychologist.project.security.SecurityUtil;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserRegisterResponseDto save(UserRegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with email "
                    + requestDto.getEmail() + " already exists");
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public UserDto getCurrentUserData() {
        return userMapper.toUserDto(SecurityUtil.getLoggedInUser());
    }
}
