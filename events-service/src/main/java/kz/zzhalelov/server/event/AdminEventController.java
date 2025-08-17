package kz.zzhalelov.server.event;

import jakarta.validation.Valid;
import kz.zzhalelov.server.event.dto.*;
import kz.zzhalelov.server.event.eventEnum.EventState;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> searchEvents(@RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<EventState> states,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                           LocalDateTime rangeStart,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                           LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {

        ParamAdminEventDto dto = new ParamAdminEventDto();
        dto.setUserIds(users != null ? users : new ArrayList<>());
        dto.setStates(states != null ? states : new ArrayList<>());
        dto.setCatIds(categories != null ? categories : new ArrayList<>());
        dto.setRangeStart(rangeStart);
        dto.setRangeEnd(rangeEnd);
        dto.setFrom(from);
        dto.setSize(size);

        List<Event> events = eventService.searchEvents(dto);

        return events.stream()
                .map(eventMapper::toFullResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateAdmin(@Valid @RequestBody AdminEventUpdateDto adminEventUpdateDto,
                                    @PathVariable long eventId) {
        Event event = eventMapper.fromAdminUpdate(adminEventUpdateDto);
        return eventMapper.toFullResponse(eventService.updateByAdmin(event, eventId));
    }
}