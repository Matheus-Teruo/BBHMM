package com.BBHMM.backend.BBHMM.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.BBHMM.backend.BBHMM.models.Event;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e WHERE e.uuid = :uuid")
    Optional<Event> findByUuid(UUID uuid);

    @Query("""
        SELECT eu.event FROM EventUser eu
        WHERE eu.user.uuid = :userUuid
        AND (:eventName IS NULL OR LOWER(eu.event.eventName) LIKE LOWER(CONCAT('%', :eventName, '%')))
        AND (:eventDate IS NULL OR eu.event.eventDate >= :eventDate)
        AND (:finished IS NULL OR eu.event.finished = :finished)
    """)
    Page<Event> findAllbyUser(String eventName, LocalDate eventDate, Boolean finished, Pageable pageable, UUID userUuid);

    @Query("""
        SELECT eu.event
        FROM EventUser eu
        WHERE eu.user.uuid = :userUuid
    """)
    List<Event> findAllbyUserList(UUID userUuid);

    @Query("""
        SELECT CASE WHEN EXISTS(
            SELECT 1 FROM EventUser eu
            WHERE eu.event.uuid = :eventUuid
            AND eu.user.uuid = :userUuid
        ) THEN TRUE ELSE FALSE END
    """)
    boolean userAlreadyInEvent(UUID userUuid, UUID eventUuid);

    boolean existsByEventName(String eventName);
}
