package psychologist.project.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import psychologist.project.model.User;

@Component("securityUtil")
public class SecurityUtil {
    public static User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getLoggedInUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getId();
    }
}
