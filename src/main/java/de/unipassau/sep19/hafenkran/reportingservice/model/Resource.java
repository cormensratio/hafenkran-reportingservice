package de.unipassau.sep19.hafenkran.reportingservice.model;

import de.unipassau.sep19.hafenkran.reportingservice.dto.UserDTO;
import de.unipassau.sep19.hafenkran.reportingservice.exception.ResourceNotFoundException;
import de.unipassau.sep19.hafenkran.reportingservice.util.SecurityContextUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContext;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Provides a superclass for all resources.
 */
@Slf4j
@MappedSuperclass
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Resource {

    @Id
    @NonNull
    @Column(nullable = false)
    private UUID id;

    @Basic
    @NonNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @NonNull
    @Column(nullable = false)
    private UUID ownerId;

    Resource(@Nullable UUID ownerId) {
        if (ownerId == null) {
            ownerId = SecurityContextUtil.getCurrentUserDTO().getId();
        }
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.ownerId = ownerId;
    }

    /**
     * Validates whether the user in the current {@link SecurityContext} is allowed to access the resource.
     * Throws a {@link ResourceNotFoundException} when the user does not have sufficient permissions.
     */
    public void validatePermissions() {
        UserDTO user = SecurityContextUtil.getCurrentUserDTO();
        if (!(user.isAdmin() || user.getId().equals(ownerId))) {
            log.info(String.format("User %s is not allowed to access %s with id %s", user.getId(),
                    this.getClass().getName(), this.getId()));
            throw new ResourceNotFoundException(this.getClass());
        }
    }
}
