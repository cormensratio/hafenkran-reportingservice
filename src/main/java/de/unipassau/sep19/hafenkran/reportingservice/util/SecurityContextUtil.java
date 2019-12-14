package de.unipassau.sep19.hafenkran.reportingservice.util;

import de.unipassau.sep19.hafenkran.reportingservice.config.JwtAuthentication;
import de.unipassau.sep19.hafenkran.reportingservice.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A utility method for extracting information about the current user stored in the {@link org.springframework.security.core.context.SecurityContext}.
 */
public class SecurityContextUtil {

    /**
     * Retrieves the {@link UserDTO} for the current user.
     *
     * @return {@link UserDTO} with information about the current user.
     */
    public static UserDTO getCurrentUserDTO() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthentication)) {
            throw new RuntimeException("Invalid user session");
        }

        JwtAuthentication authToken = (JwtAuthentication) auth;
        return (UserDTO) authToken.getDetails();
    }

    /**
     * Retrieves the jwt for the current session.
     *
     * @return a string with the token from the user request.
     */
    public static String getJWT() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthentication)) {
            throw new RuntimeException("Invalid user session");
        }

        JwtAuthentication authToken = (JwtAuthentication) auth;
        return authToken.getToken();
    }
}
