package com.BBHMM.backend.BBHMM.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BBHMM.backend.BBHMM.models.EventInvitation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventInvitationRepository extends JpaRepository<EventInvitation, UUID> {

    @Query("""
    SELECT ei
    FROM EventInvitation ei
    JOIN FETCH ei.event
    JOIN FETCH ei.userInvited
    JOIN FETCH ei.userOwner
    WHERE ei.userInvited.uuid = :userUuid
    AND (
        (:accepted IS NULL AND ei.accepted IS NULL)
        OR (ei.accepted = :accepted)
    )
    """)
    Page<EventInvitation> findAllByUserInvitedUuid(UUID userUuid, Boolean accepted, Pageable pageable);

    boolean existsByUserInvitedUuidAndEventUuidAndAcceptedIsNull(UUID userUuid, UUID eventUuid);

    Optional<EventInvitation> findByUuid(UUID uuid);
}
