package kz.zzhalelov.server.request;

import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.EventRepository;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.exception.ConflictException;
import kz.zzhalelov.server.exception.ForbiddenException;
import kz.zzhalelov.server.exception.NotFoundException;
import kz.zzhalelov.server.request.dto.*;
import kz.zzhalelov.server.user.User;
import kz.zzhalelov.server.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                              EventRepository eventRepository,
                              RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestMapper = requestMapper;
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

        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Лимит участников для события достигнут");
        }

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
    @Transactional
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

        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        if (dto.getStatus() == RequestStatus.CONFIRMED) {
            for (Request request : requests) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new ConflictException("Лимит заявок для события уже достигнут");
                }
                request.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmed.add(request);
            }

            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
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
        eventRepository.save(event);

        RequestStatusResponseDto responseDto = new RequestStatusResponseDto();
        responseDto.setConfirmedRequests(requestMapper.toResponse(confirmed));
        responseDto.setRejectedRequests(requestMapper.toResponse(rejected));

        return responseDto;
    }

    @Override
    public RequestResponseDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запрос у пользователя не найден"));
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return requestMapper.toResponse(request);
    }

    @Override
    public List<RequestResponseDto> findUserRequests(Long userId) {
        List<Request> requests = requestRepository.findAllByRequester_Id(userId);
        return requestMapper.toResponse(requests);
    }

    @Override
    public List<EventRequestResponseDto> findEventRequests(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getInitiator() == null || !event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Нет доступа к заявкам этого события");
        }
        List<Request> requests = requestRepository.findAllByEvent_Id(eventId);
        return requestMapper.toEventResponse(requests);
    }
}