package kz.zzhalelov.server.event;

import jakarta.validation.Valid;
import kz.zzhalelov.server.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventController {
    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@Valid @RequestBody EventCreateDto eventCreateDto,
                               @PathVariable long userId) {
        Event event = eventMapper.fromCreate(eventCreateDto);
        Event createdEvent = eventService.create(event, userId, eventCreateDto.getCategory());
        return eventMapper.toFullResponse(createdEvent);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> findAllByInitiator(@PathVariable long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        List<Event> userEvents = eventService.findAllByInitiator(userId, from, size);
        return userEvents.stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEventByInitiator(@PathVariable long userId,
                                             @PathVariable long eventId) {
        return eventService.findByEventAndInitiator(eventId, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@Valid @RequestBody EventUpdateDto eventUpdateDto,
                               @PathVariable long eventId,
                               @PathVariable long userId) {
        Event event = eventMapper.fromUpdate(eventUpdateDto);
        return eventMapper.toFullResponse(eventService.update(event, userId, eventId));
    }
}