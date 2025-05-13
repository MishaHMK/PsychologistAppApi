package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psychologist.project.dto.auth.UserLoginRequestDto;
import psychologist.project.dto.auth.UserLoginResponseDto;
import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.repository.user.UserRepository;
import psychologist.project.security.AuthenticationService;
import psychologist.project.service.user.UserService;

@Tag(name = "Auth controller", description = "User authorization endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authService;

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Create new user in system with provided data")
    public UserRegisterResponseDto register(
            @Valid @RequestBody UserRegisterRequestDto registerRequestDto) {
        return userService.save(registerRequestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login user to receive JWT Token")
    public UserLoginResponseDto login(
            @Valid @RequestBody UserLoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }
}
