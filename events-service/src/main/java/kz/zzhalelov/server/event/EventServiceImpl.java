package kz.zzhalelov.server.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.category.CategoryRepository;
import kz.zzhalelov.server.event.dto.ParamEventDto;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.exception.ConflictException;
import kz.zzhalelov.server.exception.ForbiddenException;
import kz.zzhalelov.server.exception.NotFoundException;
import kz.zzhalelov.server.user.User;
import kz.zzhalelov.server.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findAllByUserId(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return eventRepository.findAllByInitiator(user);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event update(Event updatedEvent, long userId, long eventId) {
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        merge(existingEvent, updatedEvent);
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
        if (paramEventDto.getUserIds() != null && !paramEventDto.getUserIds().isEmpty()) {
            builder.and(QEvent.event.initiator.id.in(paramEventDto.getUserIds()));
        }
        if (paramEventDto.getStates() != null && !paramEventDto.getStates().isEmpty()) {
            builder.and(QEvent.event.state.stringValue().in(paramEventDto.getStates()));
        }
        if (paramEventDto.getCatIds() != null && !paramEventDto.getCatIds().isEmpty()) {
            builder.and(QEvent.event.category.id.in(paramEventDto.getCatIds()));
        }
        return jpaQueryFactory
                .selectFrom(event)
                .where(builder)
                .offset(paramEventDto.getFrom())
                .limit(paramEventDto.getSize())
                .fetch();
    }

    private void merge(Event existingEvent, Event updatedEvent) {
        if (updatedEvent.getState() == EventState.PENDING) {
            existingEvent.setState(updatedEvent.getState());
        }
    }
}