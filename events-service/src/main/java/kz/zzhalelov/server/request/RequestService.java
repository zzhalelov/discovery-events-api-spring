package kz.zzhalelov.server.request;

import kz.zzhalelov.server.request.dto.RequestStatusResponseDto;
import kz.zzhalelov.server.request.dto.RequestStatusUpdateDto;

import java.util.List;

public interface RequestService {
    Request create(long userId, long eventId);

//    RequestStatusUpdateDto updateStatus(RequestStatusResponseDto dto, long userId, long eventId);

    RequestStatusResponseDto updateStatus(RequestStatusUpdateDto dto,
                                          long userId,
                                          long eventId);

//    List<Request> findRequestsByUser(Request request, Long userID);
//
//    Request update(Request request, Long userId);
}
