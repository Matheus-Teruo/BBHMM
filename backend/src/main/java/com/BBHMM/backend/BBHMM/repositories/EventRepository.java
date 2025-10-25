package com.BBHMM.backend.BBHMM.repositories;

import com.BBHMM.backend.BBHMM.models.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("SELECT e FROM Event e WHERE e.uuid = :uuid")
    Optional<Event> findByUuid(UUID uuid);

    @Query("""
    SELECT e 
    FROM Event e 
    JOIN e.users u 
    WHERE u.uuid = :userId
    """)
    List<Event> findAllbyUser(UUID userUuid);

    boolean existsByEventName(String eventName);
}
