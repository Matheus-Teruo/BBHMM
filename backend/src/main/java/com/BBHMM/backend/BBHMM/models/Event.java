package com.BBHMM.backend.BBHMM.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.BBHMM.backend.BBHMM.models.request.CreateEventRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateEventRequest;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor
public class Event {

    @Id @GeneratedValue(generator = "UUID")
    private UUID uuid;

    @Column(nullable = false, length = 100)
    private String eventName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "event_user",
            joinColumns = @JoinColumn(name = "uuid_event"),
            inverseJoinColumns = @JoinColumn(name = "uuid_user")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private List<Bill> bills = new ArrayList<>();

    public Event(CreateEventRequest request, User user) {
        this.eventName=request.eventName();
        this.description=request.description();
        this.users.add(user);
    }

    public void update(UpdateEventRequest request) {
        if (request.eventName() != null) {
            this.eventName=request.eventName();
        }
        if (request.description() != null) {
            this.description=request.description();
        }
    }
}
