package de.unipassau.sep19.hafenkran.reportingservice.config;

import de.unipassau.sep19.hafenkran.reportingservice.dto.UserDTO;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

/**
 * A class for persisting the {@link UserDTO} of the current user in a thread local {@link SecurityContextHolder}
 */
public class JwtAuthentication extends AbstractAuthenticationToken {

    @Getter
    private String token;

    JwtAuthentication(@NonNull @Valid UserDTO userDTO, @NonNull String token) {
        super(null);
        super.setAuthenticated(true);
        super.setDetails(userDTO);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
