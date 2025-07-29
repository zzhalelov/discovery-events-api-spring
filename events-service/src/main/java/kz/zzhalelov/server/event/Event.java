package kz.zzhalelov.server.event;

import jakarta.persistence.*;
import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.request.Request;
import kz.zzhalelov.server.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Table(name = "events")
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation;
    @ManyToOne
    private User initiator;
    @Column(length = 7000)
    private String description;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(length = 120)
    private String title;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    private Boolean paid;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    private Double lon;
    private Double lat;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    private Long views;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @OneToMany(mappedBy = "event")
    private List<Request> requests;
}