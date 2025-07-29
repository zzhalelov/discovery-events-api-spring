package kz.zzhalelov.server.event.dto;

import kz.zzhalelov.server.category.dto.CategoryResponseDto;
import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.user.dto.UserShortDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    public Event fromCreate(EventCreateDto eventCreateDto) {
        Event event = new Event();
        event.setAnnotation(eventCreateDto.getAnnotation());
        event.setDescription(eventCreateDto.getDescription());

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime eventDate = LocalDateTime.parse(eventCreateDto.getEventDate(), formatter);
        event.setEventDate(eventCreateDto.getEventDate());

//        LocalDateTime createdDate = LocalDateTime.parse(eventCreateDto.getEventDate(), formatter);
//        event.setCreatedOn(createdDate);

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
        eventResponseDto.setTitle(event.getTitle());
        eventResponseDto.setAnnotation(event.getAnnotation());

        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(event.getCategory().getId());
        categoryDto.setName(event.getCategory().getName());
        eventResponseDto.setCategory(categoryDto);

        eventResponseDto.setPaid(event.getPaid());
        eventResponseDto.setDescription(event.getDescription());

        eventResponseDto.setEventDate(event.getEventDate());
        eventResponseDto.setCreatedOn(event.getCreatedOn());

        eventResponseDto.setInitiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()));
        eventResponseDto.setViews(event.getViews());
        eventResponseDto.setConfirmedRequests(event.getConfirmedRequests());
        eventResponseDto.setParticipantLimit(event.getParticipantLimit());
        eventResponseDto.setState(event.getState());
        eventResponseDto.setLocation(new LocationDto(event.getLat(), event.getLon()));
        eventResponseDto.setRequestModeration(event.getRequestModeration());
        eventResponseDto.setPublishedOn(event.getPublishedOn());
        return eventResponseDto;
    }

    public List<EventResponseDto> toResponse(List<Event> events) {
        return events.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Event fromUpdate(EventUpdateDto eventUpdateDto) {
        Event event = new Event();
        event.setAnnotation(eventUpdateDto.getAnnotation());
        event.setDescription(eventUpdateDto.getDescription());

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime eventDate = LocalDateTime.parse(eventUpdateDto.getEventDate(), formatter);
        event.setEventDate(eventUpdateDto.getEventDate());
//        LocalDateTime updatedDate = LocalDateTime.parse(eventUpdateDto.getEventDate(), formatter);
//        event.setEventDate(updatedDate);
        return event;
    }
}