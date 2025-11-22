package com.BBHMM.backend.BBHMM.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_invitation")
@Getter
@NoArgsConstructor
public class EventInvitation {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "uuid_invited_user", nullable = false)
    private User userInvited;

    @ManyToOne
    @JoinColumn(name = "uuid_owner_user", nullable = false)
    private User userOwner;

    @ManyToOne
    @JoinColumn(name = "uuid_event", nullable = false)
    private Event event;

    private Boolean accepted;

    public EventInvitation(User userInvited, User userOwner, Event event){
        this.userInvited = userInvited;
        this.userOwner = userOwner;
        this.event = event;
    }

    public void acceptInvitation(boolean accept) {
        this.accepted = accept;
    }
}
