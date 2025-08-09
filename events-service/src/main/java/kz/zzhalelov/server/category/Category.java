package kz.zzhalelov.server.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kz.zzhalelov.server.event.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    List<Event> events;
}