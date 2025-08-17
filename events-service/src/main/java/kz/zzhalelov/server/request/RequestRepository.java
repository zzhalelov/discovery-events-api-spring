package kz.zzhalelov.server.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    List<Request> findAllByEvent_IdAndStatus(Long id, RequestStatus status);

    Optional<Request> findByIdAndRequester_Id(Long requestId, Long requesterId);

    List<Request> findAllByRequester_Id(Long requesterId);
}