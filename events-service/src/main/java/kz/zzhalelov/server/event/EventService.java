package kz.zzhalelov.server.event;

public interface EventService {
    Event create(Event event, long userId, Long categoryId);
}