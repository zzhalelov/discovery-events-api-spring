package kz.zzhalelov.server.event;

import kz.zzhalelov.server.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(User user);
}