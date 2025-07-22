package kz.zzhalelov.server.request.dto;

import kz.zzhalelov.server.request.Request;
import kz.zzhalelov.server.user.dto.UserShortDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestMapper {
    public Request fromCreate(RequestCreateDto requestCreateDto) {
        return new Request();
    }

    public RequestResponseDto toResponse(Request request) {
        RequestResponseDto requestResponseDto = new RequestResponseDto();
        requestResponseDto.setId(request.getId());
        requestResponseDto.setRequester(new UserShortDto(request.getRequester().getId(),
                request.getRequester().getName()));
        requestResponseDto.setEvent(request.getEvent());
        requestResponseDto.setCreated(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        requestResponseDto.setStatus(request.getStatus());
        return requestResponseDto;
    }
}