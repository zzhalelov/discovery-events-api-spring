package kz.zzhalelov.server.event;

import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.category.CategoryRepository;
import kz.zzhalelov.server.exception.ConflictException;
import kz.zzhalelov.server.exception.ForbiddenException;
import kz.zzhalelov.server.exception.NotFoundException;
import kz.zzhalelov.server.user.User;
import kz.zzhalelov.server.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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
    public Event cancelEvent(Event event, long userId, long eventId) {
        if (event.getInitiator().getId() != userId) {
            throw new ForbiddenException("Вы не являетесь инициатором этого события");
        }

        if (event.getState() != EventState.PENDING) {
            throw new ConflictException("Событие уже опубликовано или отменено");
        }
        event.setState(EventState.CANCELED);
        return eventRepository.save(event);
    }

    private void merge(Event existingEvent, Event updatedEvent) {
        if (updatedEvent.getState() == EventState.PENDING) {
            existingEvent.setState(updatedEvent.getState());
        }
    }
}