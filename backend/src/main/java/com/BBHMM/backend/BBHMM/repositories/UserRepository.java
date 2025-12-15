package com.BBHMM.backend.BBHMM.repositories;

import com.BBHMM.backend.BBHMM.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.uuid = :uuid")
    Optional<User> findByUuid(UUID uuid);

    @Query("SELECT u FROM User u JOIN u.events e WHERE e.uuid = :eventUuid")
    List<User> listUsersByEvent(UUID eventUuid);

    Optional<User> findByEmail(String email);

    Optional<User> findByFullname(String fullname);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    UserDetails findByUsernameReturnDetails(String username);

    @Query("""
    SELECT CASE WHEN EXISTS (
        SELECT 1 FROM User u
        JOIN u.events e
        WHERE u.uuid = :userUuid
        AND e.uuid = :eventUuid
        ) THEN TRUE ELSE FALSE END
    """)
    boolean isUserParticipantInEvent(UUID userUuid, UUID eventUuid);

    boolean existsByUsername(String username);

    boolean existsByFullname(String fullname);

    boolean existsByEmail(String email);

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
        FROM User u
        WHERE u.uuid = :uuid AND u.role = 'ROLE_USER'
    """)
    boolean existsByIdAndRoleUser(UUID uuid);

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END
        FROM User u
        WHERE u.uuid = :uuid AND u.role = 'ROLE_GUEST'
    """)
    boolean existsByIdAndRoleGuest(UUID uuid);
}
