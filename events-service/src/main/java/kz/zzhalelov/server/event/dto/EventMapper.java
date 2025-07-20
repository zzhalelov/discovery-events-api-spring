package kz.zzhalelov.server.event.dto;

import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.EventState;
import kz.zzhalelov.server.user.dto.UserShortDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {
    public Event fromCreate(EventCreateDto eventCreateDto) {
        Event event = new Event();
        event.setAnnotation(eventCreateDto.getAnnotation());
        event.setDescription(eventCreateDto.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime eventDate = LocalDateTime.parse(eventCreateDto.getEventDate(), formatter);
        event.setEventDate(eventDate);

        LocalDateTime createdDate = LocalDateTime.parse(eventCreateDto.getEventDate(), formatter);
        event.setCreatedOn(createdDate);

        event.setLat(eventCreateDto.getLocation().getLat());
        event.setLon(eventCreateDto.getLocation().getLon());
        event.setPaid(eventCreateDto.getPaid());
        event.setParticipantLimit(eventCreateDto.getParticipantLimit());
        event.setRequestModeration(eventCreateDto.getRequestModeration());
        event.setTitle(eventCreateDto.getTitle());
        event.setState(EventState.PENDING);
        return event;
    }

    public EventResponseDto toResponse(Event event) {
        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(event.getId());
        eventResponseDto.setAnnotation(event.getAnnotation());

        Category category = event.getCategory();
        eventResponseDto.setCategory(category);
        eventResponseDto.setDescription(event.getDescription());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventResponseDto.setEventDate(event.getEventDate().format(dateTimeFormatter));
        eventResponseDto.setCreatedOn(event.getCreatedOn().format(dateTimeFormatter));

        eventResponseDto.setLocation(new LocationDto(event.getLat(), event.getLon()));
        eventResponseDto.setPaid(event.getPaid());
        eventResponseDto.setParticipantLimit(event.getParticipantLimit());
        eventResponseDto.setRequestModeration(event.getRequestModeration());
        eventResponseDto.setTitle(event.getTitle());
        eventResponseDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        eventResponseDto.setState(event.getState());
        return eventResponseDto;
    }
}