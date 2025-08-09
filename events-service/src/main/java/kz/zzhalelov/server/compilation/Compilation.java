package kz.zzhalelov.server.compilation;

import jakarta.persistence.*;
import kz.zzhalelov.server.event.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToMany
    private List<Event> events;
    private Boolean pinned;
}