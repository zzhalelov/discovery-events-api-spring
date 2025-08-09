package kz.zzhalelov.server.request;

import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.EventRepository;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.exception.ConflictException;
import kz.zzhalelov.server.exception.ForbiddenException;
import kz.zzhalelov.server.exception.NotFoundException;
import kz.zzhalelov.server.request.dto.RequestMapper;
import kz.zzhalelov.server.request.dto.RequestStatusResponseDto;
import kz.zzhalelov.server.request.dto.RequestStatusUpdateDto;
import kz.zzhalelov.server.user.User;
import kz.zzhalelov.server.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;


    public RequestServiceImpl(RequestRepository requestRepository,
                              UserRepository userRepository,
                              EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestMapper = new RequestMapper();
    }

    @Override
    public Request create(
            long userId,
            long eventId) {
        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ConflictException("Уже существует заявка от данного пользователя");
        }

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользовватель не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        Request request = new Request();

        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии ");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("Если у события достигнут лимит запросов на участие - необходимо вернуть ошибку");
        }

        request.setStatus(RequestStatus.PENDING);

        if (!event.getRequestModeration() && (event.getParticipantLimit() == 0)) {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        return requestRepository.save(request);
    }

    @Override
    public RequestStatusResponseDto updateStatus(RequestStatusUpdateDto dto,
                                                 long userId,
                                                 long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getInitiator() == null || event.getInitiator().getId() != userId) {
            throw new ForbiddenException("Нет правов на управление событием");
        }

        List<Request> requests = requestRepository.findAllById(dto.getRequestIds())
                .stream()
                .filter(request -> request.getEvent() != null && request.getEvent().getId().equals(eventId))
                .toList();

        requests.forEach(request -> {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Статус подлежит изменению только у заявок со статусом PENDING");
            }
        });

        List<Request> confirmed = List.of();
        List<Request> rejected = List.of();

        if (dto.getStatus() == RequestStatus.CONFIRMED) {
            confirmed = requests;
            requests.forEach(request -> request.setStatus(RequestStatus.CONFIRMED));
        } else if (dto.getStatus() == RequestStatus.REJECTED) {
            rejected = requests;
            requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
        }

        requestRepository.saveAll(requests);

        RequestStatusResponseDto responseDto = new RequestStatusResponseDto();
        responseDto.setConfirmedRequests(requestMapper.toResponse(confirmed));
        responseDto.setRejectedRequests(requestMapper.toResponse(rejected));

        return responseDto;
    }
}