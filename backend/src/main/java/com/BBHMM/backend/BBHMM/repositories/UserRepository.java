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

    UserDetails findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByFullname(String fullname);
}
