package psychologist.project.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import psychologist.project.model.User;

@Component("securityUtil")
public class SecurityUtil {
    public static User getLoggedInUser() {
        return getValidUserFromPrincipal();
    }

    public static Long getLoggedInUserId() {
        return getValidUserFromPrincipal().getId();
    }

    private static User getValidUserFromPrincipal() {
        Object principal = SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            throw new SecurityException("You are not logged in");
        }
        return (User) principal;
    }

    public static User getValidUserIfAuthenticated() {
        Object principal = SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            return null;
        }
        return (User) principal;
    }
}
