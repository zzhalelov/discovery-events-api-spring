package kz.zzhalelov.server.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.category.CategoryRepository;
import kz.zzhalelov.server.event.dto.ParamEventDto;
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

    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            EntityManager em) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.jpaQueryFactory = new JPAQueryFactory(em);
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
    public List<Event> findAll(int from, int size, List<Long> categoryIds) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            return eventRepository.findByCategoryIdIn(categoryIds, pageable).getContent();
        }
        return eventRepository.findAll(pageable).getContent();
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
    public List<Event> searchEvents(ParamEventDto paramEventDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (paramEventDto.getRangeStart() != null) {
            builder.and(event.eventDate.goe(paramEventDto.getRangeStart()));
        }
        if (paramEventDto.getRangeEnd() != null) {
            builder.and(event.eventDate.loe(paramEventDto.getRangeEnd()));
        }

        List<Long> userIds = paramEventDto.getUserIds();
        if (userIds != null) {
            userIds = userIds.stream()
                    .filter(id -> id != null && id > 0)
                    .toList();
            if (userIds.isEmpty() && !paramEventDto.getUserIds().isEmpty()) {
                return Collections.emptyList();
            }
            if (!userIds.isEmpty()) {
                builder.and(event.initiator.id.in(userIds));
            }
        }

//        if (userIds != null && !userIds.isEmpty()) {
//            builder.and(event.initiator.id.in(userIds));
//        }

        if (paramEventDto.getStates() != null && !paramEventDto.getStates().isEmpty()) {
            builder.and(event.state.in(paramEventDto.getStates()));
        }

        List<Long> catIds = paramEventDto.getCatIds();
        if (catIds != null) {
            catIds = catIds.stream()
                    .filter(id -> id != null && id > 0) // убираем null и 0
                    .toList();
            if (catIds.isEmpty() && !paramEventDto.getCatIds().isEmpty()) {
                return Collections.emptyList();
            }
            if (!catIds.isEmpty()) {
                builder.and(event.category.id.in(catIds));
            }
        }

//        if (catIds != null && !catIds.isEmpty()) {
//            builder.and(event.category.id.in(catIds));
//        }
        return jpaQueryFactory
                .selectFrom(event)
                .where(builder)
                .offset(paramEventDto.getFrom())
                .limit(paramEventDto.getSize())
                .fetch();
    }

    @Override
    public Event findById(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не нпйдено"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Событие не нпйдено");
        }

        return event;
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