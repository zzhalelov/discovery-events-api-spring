package kz.zzhalelov.server.event;

import jakarta.validation.Valid;
import kz.zzhalelov.server.category.Category;
import kz.zzhalelov.server.event.dto.AdminEventUpdateDto;
import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.EventResponseDto;
import kz.zzhalelov.server.event.dto.ParamEventDto;
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
    public List<EventResponseDto> searchEvents(@RequestParam(required = false) List<Long> users,
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

        ParamEventDto paramEventDto = new ParamEventDto();
        paramEventDto.setUserIds(users != null ? users : new ArrayList<>());
        paramEventDto.setStates(states != null ? states : new ArrayList<>());
        paramEventDto.setCatIds(categories != null ? categories : new ArrayList<>());
        paramEventDto.setRangeStart(rangeStart);
        paramEventDto.setRangeEnd(rangeEnd);
        paramEventDto.setFrom(from);
        paramEventDto.setSize(size);

        List<Event> events = eventService.searchEvents(paramEventDto);
//        if (events == null) {
//            return
//        }

        return events.stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponseDto updateAdmin(@Valid @RequestBody AdminEventUpdateDto adminEventUpdateDto,
                                        @PathVariable long eventId) {
        Event event = eventMapper.fromAdminUpdate(adminEventUpdateDto);
        return eventMapper.toResponse(eventService.updateByAdmin(event, eventId));
    }
}