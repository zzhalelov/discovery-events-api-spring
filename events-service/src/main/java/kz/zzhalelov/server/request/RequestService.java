package kz.zzhalelov.server.request;

import kz.zzhalelov.server.request.dto.RequestResponseDto;
import kz.zzhalelov.server.request.dto.RequestStatusResponseDto;
import kz.zzhalelov.server.request.dto.RequestStatusUpdateDto;

import java.util.List;

public interface RequestService {
    Request create(long userId, long eventId);

    RequestStatusResponseDto update(RequestStatusUpdateDto dto,
                                    long userId,
                                    long eventId);

    RequestResponseDto cancelRequest(Long userId, Long requestId);

    List<RequestResponseDto> findUserRequests(Long userId);
}
