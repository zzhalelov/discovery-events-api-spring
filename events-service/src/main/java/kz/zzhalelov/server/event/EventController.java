package kz.zzhalelov.server.event;

import jakarta.validation.Valid;
import kz.zzhalelov.server.event.dto.EventCreateDto;
import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}