package kz.zzhalelov.server.request.dto;

import kz.zzhalelov.server.event.dto.EventShortDto;
import kz.zzhalelov.server.request.Request;
import kz.zzhalelov.server.user.dto.UserShortDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
            dto.setRequester(new UserShortDto(
                    request.getRequester().getId(),
                    request.getRequester().getName()
            ));
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
}