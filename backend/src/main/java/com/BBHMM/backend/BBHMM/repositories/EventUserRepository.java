package com.BBHMM.backend.BBHMM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.BBHMM.backend.BBHMM.models.EventUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventUserRepository extends JpaRepository<EventUser, UUID> {

    @Query("SELECT eu FROM EventUser eu WHERE eu.user.uuid = :userUuid AND eu.event.uuid = :eventUuid")
    Optional<EventUser> findByUserAndEventId(UUID userUuid, UUID eventUuid);

    @Query("SELECT eu FROM EventUser eu JOIN eu.event e WHERE e.uuid = :eventUuid")
    List<EventUser> listUsersByEvent(UUID eventUuid);
}
