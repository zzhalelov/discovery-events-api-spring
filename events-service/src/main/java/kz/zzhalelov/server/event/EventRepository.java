package kz.zzhalelov.server.event;

import kz.zzhalelov.server.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Page<Event> findByCategoryIdIn(List<Long> categoryIds, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);
}