package kz.zzhalelov.server.event;

import kz.zzhalelov.server.event.dto.ParamEventDto;
import kz.zzhalelov.server.user.User;

import java.util.List;

public interface EventService {
    Event create(Event event, long userId, Long categoryId);

    List<Event> findAllByInitiator(long userId, int from, int size);

    List<Event> findAll(int from, int size, List<Long> categoryIds);

    Event update(Event event, long userId, long eventId);

    Event updateByAdmin(Event event, long eventId);

    List<Event> searchEvents(ParamEventDto paramEventDto);

    Event findById(long eventId);

}