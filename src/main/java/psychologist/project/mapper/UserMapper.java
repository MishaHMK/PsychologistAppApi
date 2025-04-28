package psychologist.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import psychologist.project.config.MapperConfig;
import psychologist.project.dto.auth.UserRegisterRequestDto;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.user.UserDto;
import psychologist.project.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegisterRequestDto dto);

    UserDto toUserDto(User user);

    UserRegisterResponseDto toResponse(User user);

    @Named("userFromId")
    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
