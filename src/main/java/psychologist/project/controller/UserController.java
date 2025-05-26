package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import psychologist.project.dto.user.UpdateUserDataDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.service.user.UserService;

@Tag(name = "User controller", description = "User management endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("me")
    @Operation(summary = "Get user info",
            description = "Receive currently logged in user info")
    public UserDto receiveCurrentUserInfo() {
        return userService.getCurrentUserData();
    }

    @DeleteMapping("remove-user")
    @Operation(summary = "Remove user",
            description = "Remove currently logged user from db")
    public void deleteCurrentUser() {
        userService.deleteUser();
    }

    @PutMapping("update-user")
    @Operation(summary = "Update user",
            description = "Update currently logged in user data")
    public UserDto updateUserData(@RequestBody UpdateUserDataDto updateDto) {
        return userService.updateUser(updateDto);
    }

    @PatchMapping(value = "update-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update image",
            description = "Update currently logged in user image")
    public UserDto updateUserImage(@RequestParam MultipartFile profileImage)
            throws IOException {
        return userService.updateImage(profileImage.getBytes());
    }
}
