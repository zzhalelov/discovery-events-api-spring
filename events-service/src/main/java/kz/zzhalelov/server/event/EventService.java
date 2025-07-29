package kz.zzhalelov.server.event;

import kz.zzhalelov.server.event.dto.ParamEventDto;
import kz.zzhalelov.server.user.User;

import java.util.List;

public interface EventService {
    Event create(Event event, long userId, Long categoryId);

    List<Event> findAllByUserId(long userId);

    List<Event> findAll();

    Event update(Event event, long userId, long eventId);

    List<Event> searchEvents(ParamEventDto paramEventDto);

}