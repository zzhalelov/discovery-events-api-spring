package kz.zzhalelov.server.event;

import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.EventResponseDto;
import kz.zzhalelov.server.event.dto.ParamEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
                                               @RequestParam(required = false) List<String> states,
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
        paramEventDto.setUserIds(users);
        paramEventDto.setStates(states);
        paramEventDto.getCatIds().addAll(categories);
        paramEventDto.setRangeStart(rangeStart);
        paramEventDto.setRangeEnd(rangeEnd);
        paramEventDto.setFrom(from);
        paramEventDto.setSize(size);

        return eventService.searchEvents(paramEventDto)
                .stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }
}