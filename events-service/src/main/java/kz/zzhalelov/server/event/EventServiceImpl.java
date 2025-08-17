package kz.zzhalelov.server.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.category.CategoryRepository;
import kz.zzhalelov.server.event.dto.EventFullDto;
import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.ParamAdminEventDto;
import kz.zzhalelov.server.event.dto.ParamPublicEventDto;
import kz.zzhalelov.server.event.eventEnum.EventSortParams;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.exception.BadRequestException;
import kz.zzhalelov.server.exception.ConflictException;
import kz.zzhalelov.server.exception.NotFoundException;
import kz.zzhalelov.server.user.User;
import kz.zzhalelov.server.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private static final QEvent event = QEvent.event;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            EntityManager em, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.eventMapper = eventMapper;
    }

    @Override
    public Event create(Event event, long userId, Long categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (categoryId == null) {
            throw new IllegalArgumentException("Категория обязательна");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        if (event.getEventDate() == null) {
            throw new BadRequestException("Дата и время события обязательны");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException(
                    "Дата и время события должны быть не ранее, чем через два часа от текущего момента");
        }

        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findAllByInitiator(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiator_Id(userId, pageable);
    }

    @Override
    public List<Event> findAll(ParamPublicEventDto params) {
        if (params.getRangeEnd() != null && params.getRangeStart() != null && params.getRangeEnd().isBefore(params.getRangeStart())) {
            throw new BadRequestException("Дата начала не может быть позже жаты окончания");
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (params.getRangeStart() != null) {
            builder.and(event.eventDate.goe(params.getRangeStart()));
        }
        if (params.getRangeEnd() != null) {
            builder.and(event.eventDate.loe(params.getRangeEnd()));
        }

        List<Long> catIds = params.getCategories();
        if (catIds != null) {
            catIds = catIds.stream()
                    .filter(id -> id != null && id > 0) // убираем null и 0
                    .toList();
            if (catIds.isEmpty() && !params.getCategories().isEmpty()) {
                return Collections.emptyList();
            }
            if (!catIds.isEmpty()) {
                builder.and(event.category.id.in(catIds));
            }
        }

        if (params.getText() != null && !params.getText().isBlank()) {
            builder.and(event.annotation.containsIgnoreCase(params.getText())
                    .or(event.description.containsIgnoreCase(params.getText())));
        }

        if (params.getPaid() != null && params.getPaid()) {
            builder.and(event.paid.isTrue());
        }

        if (params.getOnlyAvailable()) {
            builder.and(event.participantLimit.eq(0)
                    .or(event.confirmedRequests.lt(event.participantLimit)));
        }

        return jpaQueryFactory
                .selectFrom(event)
                .where(builder)
                .orderBy(params.getSort() == EventSortParams.EVENT_DATE
                        ? event.eventDate.desc()
                        : event.views.desc())
                .offset(params.getFrom())
                .limit(params.getSize())
                .fetch();
    }

    @Override
    public Event update(Event updatedEvent, long userId, long eventId) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (!(existingEvent.getState() == EventState.CANCELED || existingEvent.getState() == EventState.PENDING)) {
            throw new ConflictException("Изменять можно только отмененные события или события в ожидании модерации");
        }

        if (updatedEvent.getEventDate() != null &&
                updatedEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Дата события должна быть не раньше, чем через два часа от текущего момента");
        }

        merge(existingEvent, updatedEvent);
        return eventRepository.save(existingEvent);
    }

    @Override
    public Event updateByAdmin(Event event, long eventId) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getEventDate() != null) {
            if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException(
                        "дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
        }

        if (event.getState() != null && event.getState() == EventState.PUBLISHED) {
            if (!existingEvent.getState().equals(EventState.PENDING)) {
                throw new ConflictException("событие можно публиковать, только если оно в состоянии ожидания публикации");
            }
            existingEvent.setPublishedOn(LocalDateTime.now());
        }

        if (event.getState() != null && event.getState() == EventState.CANCELED) {
            if (existingEvent.getState().equals(EventState.PUBLISHED)) {
                throw new ConflictException("событие можно отклонить, только если оно еще не опубликовано");
            }
        }
        merge(existingEvent, event);
        return eventRepository.save(existingEvent);
    }

    @Override
    public List<Event> searchEvents(ParamAdminEventDto dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (dto.getRangeStart() != null) {
            builder.and(event.eventDate.goe(dto.getRangeStart()));
        }
        if (dto.getRangeEnd() != null) {
            builder.and(event.eventDate.loe(dto.getRangeEnd()));
        }

        List<Long> userIds = dto.getUserIds();
        if (userIds != null) {
            userIds = userIds.stream()
                    .filter(id -> id != null && id > 0)
                    .toList();
            if (userIds.isEmpty() && !dto.getUserIds().isEmpty()) {
                return Collections.emptyList();
            }
            if (!userIds.isEmpty()) {
                builder.and(event.initiator.id.in(userIds));
            }
        }

        if (dto.getStates() != null && !dto.getStates().isEmpty()) {
            builder.and(event.state.in(dto.getStates()));
        }

        List<Long> catIds = dto.getCatIds();
        if (catIds != null) {
            catIds = catIds.stream()
                    .filter(id -> id != null && id > 0) // убираем null и 0
                    .toList();
            if (catIds.isEmpty() && !dto.getCatIds().isEmpty()) {
                return Collections.emptyList();
            }
            if (!catIds.isEmpty()) {
                builder.and(event.category.id.in(catIds));
            }
        }

        return jpaQueryFactory
                .selectFrom(event)
                .where(builder)
                .offset(dto.getFrom())
                .limit(dto.getSize())
                .fetch();
    }

    @Override
    public EventFullDto findById(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не нпйдено"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Событие не нпйдено");
        }

        return eventMapper.toFullResponse(event);
    }

    @Override
    public EventFullDto findByEventAndInitiator(long eventId, long userId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие на надено"));
        return eventMapper.toFullResponse(event);
    }

    private void merge(Event existingEvent, Event updatedEvent) {
        if (updatedEvent.getAnnotation() != null && !updatedEvent.getAnnotation().isBlank()) {
            existingEvent.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getCategory() != null) {
            existingEvent.setCategory(updatedEvent.getCategory());
        }
        if (updatedEvent.getDescription() != null && !updatedEvent.getDescription().isBlank()) {
            existingEvent.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getEventDate() != null) {
            existingEvent.setEventDate(updatedEvent.getEventDate());
        }
        if (updatedEvent.getLon() != null) {
            existingEvent.setLon(updatedEvent.getLon());
        }
        if (updatedEvent.getLat() != null) {
            existingEvent.setLat(updatedEvent.getLat());
        }
        if (updatedEvent.getPaid() != null) {
            existingEvent.setPaid(updatedEvent.getPaid());
        }
        if (updatedEvent.getParticipantLimit() != null) {
            existingEvent.setParticipantLimit(updatedEvent.getParticipantLimit());
        }
        if (updatedEvent.getRequestModeration() != null) {
            existingEvent.setRequestModeration(updatedEvent.getRequestModeration());
        }
        if (updatedEvent.getState() != null) {
            existingEvent.setState(updatedEvent.getState());
        }
        if (updatedEvent.getTitle() != null && !updatedEvent.getTitle().isBlank()) {
            existingEvent.setTitle(updatedEvent.getTitle());
        }
    }
}