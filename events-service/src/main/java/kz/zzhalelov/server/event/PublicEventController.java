package kz.zzhalelov.server.event;

import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.EventResponseDto;
import kz.zzhalelov.server.event.eventEnum.EventSortParams;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    public List<EventResponseDto> findAll(@RequestParam(required = false) String text,
                                          @RequestParam(required = false) List<Long> categoryIds,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                          @RequestParam(required = false) Boolean onlyAvailable,
                                          @RequestParam(required = false) EventSortParams sort,
                                          @RequestParam(required = false) Long from,
                                          @RequestParam(required = false) Long size) {
        return eventService.findAll()
                .stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }
}