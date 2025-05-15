package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psychologist.project.dto.user.UserDto;
import psychologist.project.service.user.UserService;

@Tag(name = "User controller", description = "User management endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("me")
    @Operation(summary = "Get user info",
            description = "Receive currently logged in user info")
    public UserDto receiveCurrentUserInfo() {
        return userService.getCurrentUserData();
    }

    @PreAuthorize("#userId == @securityUtil.loggedInUserId")
    @DeleteMapping("remove-user/{userId}")
    @Operation(summary = "Remove user by id",
            description = "Remove currently logged user from db")
    public void deleteCurrentUser(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}
