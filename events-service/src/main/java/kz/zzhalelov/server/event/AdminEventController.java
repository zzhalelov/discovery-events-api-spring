package kz.zzhalelov.server.event;

import kz.zzhalelov.server.event.dto.EventMapper;
import kz.zzhalelov.server.event.dto.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class AdminEventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponseDto> getAllEvents() {
        return eventService.findAll()
                .stream()
                .map(eventMapper::toResponse)
                .collect(Collectors.toList());
    }
}