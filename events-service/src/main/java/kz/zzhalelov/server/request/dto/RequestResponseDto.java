package kz.zzhalelov.server.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.zzhalelov.server.request.RequestStatus;
import kz.zzhalelov.server.user.dto.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestResponseDto {
    Long id;
    UserShortDto requester;
    Long event;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String created;
    RequestStatus status;
}