package kz.zzhalelov.server.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByRequester_IdAndEvent_Id(Long requesterId, Long eventId);
}