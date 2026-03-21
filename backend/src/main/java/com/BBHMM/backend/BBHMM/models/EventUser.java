package com.BBHMM.backend.BBHMM.models;

import java.util.UUID;

import com.BBHMM.backend.BBHMM.models.request.UpdateEventUserRequest;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_user")
@Getter
@NoArgsConstructor
public class EventUser {

    @EmbeddedId
    private EventUserId id;

    @ManyToOne
    @MapsId("uuidUser")
    @JoinColumn(name = "uuid_user")
    private User user;

    @ManyToOne
    @MapsId("uuidEvent")
    @JoinColumn(name = "uuid_event")
    private Event event;

    @Column(name = "color", length = 7)
    private String color;

    public EventUser(User user, Event event) {
        this.user = user;
        this.event = event;
        this.id = new EventUserId(user.getUuid(), event.getUuid());
    }

    public UUID getUserUuid() {
        return user.getUuid();
    }

    public void update(UpdateEventUserRequest request) {
        if (this.color != null) this.color = request.color();
    }
}
