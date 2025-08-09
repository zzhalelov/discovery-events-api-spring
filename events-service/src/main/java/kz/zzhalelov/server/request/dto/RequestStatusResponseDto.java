package kz.zzhalelov.server.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatusResponseDto {
    List<RequestResponseDto> confirmedRequests;
    List<RequestResponseDto> rejectedRequests;
}