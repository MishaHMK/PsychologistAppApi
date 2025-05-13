package psychologist.project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import psychologist.project.dto.auth.UserLoginRequestDto;
import psychologist.project.dto.auth.UserLoginResponseDto;
import psychologist.project.model.User;
import psychologist.project.repository.user.UserRepository;
import psychologist.project.security.jwt.JwtUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        Optional<User> byEmail = userRepository.findByEmail(requestDto.email());
        if (byEmail.isPresent() && byEmail.get().getPassword().isEmpty()) {
            throw new BadCredentialsException("You have to register this user first");
        }
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
