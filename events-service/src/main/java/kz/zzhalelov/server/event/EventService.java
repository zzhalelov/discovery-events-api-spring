package kz.zzhalelov.server.event;

import kz.zzhalelov.server.event.dto.EventFullDto;
import kz.zzhalelov.server.event.dto.ParamAdminEventDto;
import kz.zzhalelov.server.event.dto.ParamPublicEventDto;

import java.util.List;

public interface EventService {
    Event create(Event event, long userId, Long categoryId);

    List<Event> findAllByInitiator(long userId, int from, int size);

    List<Event> findAll(ParamPublicEventDto params);

    Event update(Event event, long userId, long eventId);

    Event updateByAdmin(Event event, long eventId);

    List<Event> searchEvents(ParamAdminEventDto dto);

    EventFullDto findById(long eventId);

    EventFullDto findByEventAndInitiator(long eventId, long userId);
}