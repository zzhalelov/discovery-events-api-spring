package kz.zzhalelov.server.event.dto;

import kz.zzhalelov.server.category.dto.CategoryResponseDto;
import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.eventEnum.AdminStateAction;
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
        event.setEventDate(eventCreateDto.getEventDate());
        event.setLat(eventCreateDto.getLocation().getLat());
        event.setLon(eventCreateDto.getLocation().getLon());
        event.setPaid(eventCreateDto.getPaid());
        event.setParticipantLimit(eventCreateDto.getParticipantLimit());
        event.setRequestModeration(eventCreateDto.getRequestModeration());
        event.setTitle(eventCreateDto.getTitle());
        event.setState(EventState.PENDING);
        return event;
    }

    public Event fromUpdate(EventUpdateDto eventUpdateDto) {
        Event event = new Event();
        event.setAnnotation(eventUpdateDto.getAnnotation());
        event.setDescription(eventUpdateDto.getDescription());
        event.setEventDate(eventUpdateDto.getEventDate());
        event.setLat(eventUpdateDto.getLocation() != null ? eventUpdateDto.getLocation().getLat() : null);
        event.setLon(eventUpdateDto.getLocation() != null ? eventUpdateDto.getLocation().getLon() : null);
        event.setPaid(eventUpdateDto.getPaid());
        event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        event.setRequestModeration(eventUpdateDto.getRequestModeration());
        event.setTitle(eventUpdateDto.getTitle());

        if (eventUpdateDto.getStateAction() != null) {
            switch (eventUpdateDto.getStateAction()) {
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
                case SEND_TO_REVIEW -> event.setState((EventState.PENDING));
            }
        }
        return event;
    }

    public Event fromAdminUpdate(AdminEventUpdateDto adminEventUpdateDto) {
        Event event = new Event();
        event.setAnnotation(adminEventUpdateDto.getAnnotation());
        event.setDescription(adminEventUpdateDto.getDescription());
        event.setEventDate(adminEventUpdateDto.getEventDate());
        event.setLat(adminEventUpdateDto.getLocation() != null ? adminEventUpdateDto.getLocation().getLat() : null);
        event.setLon(adminEventUpdateDto.getLocation() != null ? adminEventUpdateDto.getLocation().getLat() : null);
        event.setPaid(adminEventUpdateDto.getPaid());
        event.setParticipantLimit(adminEventUpdateDto.getParticipantLimit());
        event.setRequestModeration(adminEventUpdateDto.getRequestModeration());
        event.setTitle(adminEventUpdateDto.getTitle());

        if (adminEventUpdateDto.getStateAction() != null) {
            if (adminEventUpdateDto.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
                event.setState(EventState.PUBLISHED);
            } else {
                event.setState(EventState.CANCELED);
            }
        }
        return event;
    }

    public EventFullDto toFullResponse(Event event) {
        EventFullDto dto = new EventFullDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setDescription(event.getDescription());
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setState(event.getState());
        dto.setRequestModeration(event.getRequestModeration());

        if (event.getEventDate() != null) {
            dto.setEventDate(event.getEventDate());
        }

        if (event.getCreatedOn() != null) {
            dto.setCreatedOn(event.getCreatedOn());
        }

        if (event.getPublishedOn() != null) {
            dto.setPublishedOn(event.getPublishedOn());
        }

        if (event.getCategory() != null) {
            dto.setCategory(new CategoryResponseDto(
                    event.getCategory().getId(),
                    event.getCategory().getName()
            ));
        }

        if (event.getInitiator() != null) {
            dto.setInitiator(new UserShortDto(
                    event.getInitiator().getId(),
                    event.getInitiator().getName()
            ));
        }

        if (event.getLat() != null && event.getLon() != null) {
            dto.setLocation(new LocationDto(event.getLat(), event.getLon()));
        } else {
            dto.setLocation(null);
        }

        dto.setViews(event.getViews() != null ? event.getViews() : 0);
        dto.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0);

        return dto;
    }

    public EventResponseDto toResponse(Event event) {
        EventResponseDto dto = new EventResponseDto();

        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());

        if (event.getCategory() != null) {
            dto.setCategory(new CategoryResponseDto(
                    event.getCategory().getId(),
                    event.getCategory().getName()
            ));
        }

        if (event.getEventDate() != null) {
            dto.setEventDate(event.getEventDate());
        }

        if (event.getInitiator() != null) {
            dto.setInitiator(new UserShortDto(
                    event.getInitiator().getId(),
                    event.getInitiator().getName()
            ));
        }

        dto.setPaid(event.getPaid());
        dto.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0);
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews() != null ? event.getViews() : 0);

        return dto;
    }

    public List<EventResponseDto> toResponse(List<Event> events) {
        return events.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EventShortDto toShortDto(Event event) {
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        return dto;
    }
}