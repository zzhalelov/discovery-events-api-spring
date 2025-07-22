package kz.zzhalelov.server.request;

import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.EventRepository;
import kz.zzhalelov.server.exception.NotFoundException;
import kz.zzhalelov.server.user.User;
import kz.zzhalelov.server.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Request create(Request request, long userId, long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользовватель не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setStatus(RequestStatus.PENDING);
        return requestRepository.save(request);
    }

//    @Override
//    public List<Request> findRequestsByUser(Request request, Long userID) {
//        return List.of();
//    }
//
//    @Override
//    public Request update(Request request, Long userId) {
//        return null;
//    }
}