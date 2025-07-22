package kz.zzhalelov.server.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.request.RequestStatus;
import kz.zzhalelov.server.user.dto.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestResponseDto {
    Long id;
    @NotNull
    UserShortDto requester;
    @NotNull
    Event event;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String created;
    @NotNull
    RequestStatus status;
}