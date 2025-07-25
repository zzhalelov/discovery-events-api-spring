package kz.zzhalelov.server.event;

import jakarta.validation.Valid;
import kz.zzhalelov.server.event.dto.EventCreateDto;
import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.EventResponseDto;
import kz.zzhalelov.server.event.dto.EventUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventController {
    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto create(@Valid @RequestBody EventCreateDto eventCreateDto,
                                   @PathVariable long userId) {
        Event event = eventMapper.fromCreate(eventCreateDto);
        Event createdEvent = eventService.create(event, userId, eventCreateDto.getCategory());
        return eventMapper.toResponse(createdEvent);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> findAllByInitiator(@PathVariable long userId) {
        return eventMapper.toResponse(eventService.findAllByUserId(userId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponseDto update(@Valid @RequestBody EventUpdateDto eventUpdateDto,
                                   @PathVariable long eventId,
                                   @PathVariable long userId) {
        Event event = eventMapper.fromUpdate(eventUpdateDto);
        return eventMapper.toResponse(eventService.update(event, userId, eventId));
    }

//    @PatchMapping("/{userId}/events/{eventId}")
//    @ResponseStatus(HttpStatus.OK)
//    public EventResponseDto cancelEvent(@Valid @RequestBody EventUpdateDto eventUpdateDto,
//                                        @PathVariable long userId,
//                                        @PathVariable long eventId) {
//        Event event = eventMapper.fromUpdate(eventUpdateDto);
//        return eventMapper.toResponse(eventService.cancelEvent(event, userId, eventId));
//    }
}