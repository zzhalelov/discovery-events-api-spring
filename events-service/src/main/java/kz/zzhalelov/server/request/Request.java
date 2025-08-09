package kz.zzhalelov.server.request;

import jakarta.persistence.*;
import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User requester;
    private LocalDateTime created;
    @ManyToOne
    private Event event;
    private RequestStatus status;
}