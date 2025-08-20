package kz.zzhalelov.server.request.dto;

import kz.zzhalelov.server.request.Request;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {
    public Request fromCreate(RequestCreateDto requestCreateDto) {
        return new Request();
    }

    public RequestResponseDto toResponse(Request request) {
        RequestResponseDto dto = new RequestResponseDto();
        dto.setId(request.getId());

        if (request.getRequester() != null) {
            dto.setRequester(request.getRequester().getId());
        }

        if (request.getEvent() != null) {
            dto.setEvent(request.getEvent().getId());
        }

        dto.setCreated(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dto.setStatus(request.getStatus());
        return dto;
    }

    public List<RequestResponseDto> toResponse(List<Request> requests) {
        return requests.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EventRequestResponseDto toEventResponse(Request request) {
        EventRequestResponseDto dto = new EventRequestResponseDto();
        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setEvent(request.getEvent().getId());
        dto.setCreated(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        dto.setStatus(request.getStatus());
        return dto;
    }

    public List<EventRequestResponseDto> toEventResponse(List<Request> requests) {
        return requests.stream()
                .map(this::toEventResponse)
                .collect(Collectors.toList());
    }
}