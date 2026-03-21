package com.BBHMM.backend.BBHMM.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventUserId implements Serializable {

    @Column(name = "uuid_user", columnDefinition = "BINARY(16)")
    private UUID uuidUser;

    @Column(name = "uuid_event", columnDefinition = "BINARY(16)")
    private UUID uuidEvent;
}
