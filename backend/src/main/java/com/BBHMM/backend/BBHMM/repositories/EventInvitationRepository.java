package com.BBHMM.backend.BBHMM.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.BBHMM.backend.BBHMM.models.EventInvitation;

public interface EventInvitationRepository extends JpaRepository<EventInvitation, UUID> {

    @Query("""
    SELECT ei
    FROM EventInvitation ei
    JOIN FETCH ei.event
    JOIN FETCH ei.userInvited
    JOIN FETCH ei.userOwner
    WHERE ei.userInvited.uuid = :userUuid
    """)
    Page<EventInvitation> findAllByUserInvitedUuid(UUID userUuid, Pageable pageable);

    boolean existsByUserRequestUuidAndEventUuidAndAcceptedIsNull(UUID userUuid, UUID eventUuid);
}
