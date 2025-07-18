package kz.zzhalelov.server.event.dto;

import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.event.Event;
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
        LocalDateTime dateTime = LocalDateTime.parse(eventCreateDto.getEventDate(), formatter);
        event.setEventDate(dateTime);

        event.setLat(eventCreateDto.getLatitude());
        event.setLon(eventCreateDto.getLongitude());
        event.setPaid(eventCreateDto.getPaid());
        event.setParticipantLimit(eventCreateDto.getParticipantLimit());
        event.setRequestModeration(eventCreateDto.getRequestModeration());
        event.setTitle(eventCreateDto.getTitle());
        return event;
    }

    public EventResponseDto toResponse(Event event) {
        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(event.getId());
        eventResponseDto.setAnnotation(event.getAnnotation());
        eventResponseDto.setCategoryId(event.getCategory().getId());
        eventResponseDto.setDescription(event.getDescription());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        eventResponseDto.setEventDate(event.getEventDate().format(dateTimeFormatter));

        eventResponseDto.setLatitude(event.getLat());
        eventResponseDto.setLongitude(event.getLon());
        eventResponseDto.setPaid(event.getPaid());
        eventResponseDto.setParticipantLimit(event.getParticipantLimit());
        eventResponseDto.setRequestModeration(event.getRequestModeration());
        eventResponseDto.setTitle(event.getTitle());
        return eventResponseDto;
    }
}