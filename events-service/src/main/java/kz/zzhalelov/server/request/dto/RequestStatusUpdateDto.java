package kz.zzhalelov.server.request.dto;

import kz.zzhalelov.server.request.RequestStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusUpdateDto {
    List<Long> requestIds;
    RequestStatus status;
}