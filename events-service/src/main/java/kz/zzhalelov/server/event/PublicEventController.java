package kz.zzhalelov.server.event;

import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> findAll(@RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) List<Long> categories
    ) {
        return eventService.findAll(from, size, categories)
                .stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public Event findById(@PathVariable long eventId) {
        return eventService.findById(eventId);
    }
}