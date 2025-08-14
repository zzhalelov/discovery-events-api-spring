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
import java.util.ArrayList;
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

        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии ");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        long totalRequests = requestRepository.countByEvent_Id(eventId);

        if (event.getParticipantLimit() > 0 && totalRequests >= event.getParticipantLimit()) {
            throw new ConflictException("Лимит участников для события достигнут");
        }

//        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
//            throw new ConflictException("Если у события достигнут лимит запросов на участие");
//        }

        Request request = new Request();
        request.setStatus(RequestStatus.PENDING);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return requestRepository.save(request);
    }

    @Override
    public RequestStatusResponseDto update(RequestStatusUpdateDto dto,
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

        long confirmedRequests = requestRepository.countByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED);

        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        if (dto.getStatus() == RequestStatus.CONFIRMED) {
            for (Request request : requests) {
                if (confirmedRequests >= event.getParticipantLimit()) {
                    throw new ConflictException("Лимит заявок для события уже достигнут");
                }
                request.setStatus(RequestStatus.CONFIRMED);
                confirmed.add(request);
                confirmedRequests++;
            }

            if (confirmedRequests >= event.getParticipantLimit()) {
                List<Request> pendingRequests = requestRepository
                        .findAllByEvent_IdAndStatus(eventId, RequestStatus.PENDING);
                pendingRequests.forEach(pendingRequest -> pendingRequest.setStatus(RequestStatus.REJECTED));
                rejected.addAll(pendingRequests);
                requestRepository.saveAll(pendingRequests);
            }
        } else if (dto.getStatus() == RequestStatus.REJECTED) {
            requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            rejected.addAll(requests);
        }

        requestRepository.saveAll(confirmed);
        requestRepository.saveAll(rejected);

        RequestStatusResponseDto responseDto = new RequestStatusResponseDto();
        responseDto.setConfirmedRequests(requestMapper.toResponse(confirmed));
        responseDto.setRejectedRequests(requestMapper.toResponse(rejected));

        return responseDto;
    }
}