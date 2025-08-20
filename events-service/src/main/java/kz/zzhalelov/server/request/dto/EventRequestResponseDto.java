package kz.zzhalelov.server.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.zzhalelov.server.request.RequestStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestResponseDto {
    Long id;
    Long requester;
    Long event;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String created;
    RequestStatus status;
}