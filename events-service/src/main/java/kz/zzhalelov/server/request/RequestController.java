package kz.zzhalelov.server.request;

import kz.zzhalelov.server.request.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestController {
    private final RequestMapper requestMapper;
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponseDto create(@PathVariable long userId,
                                     @RequestParam long eventId) {
        Request createdRequest = requestService.create(userId, eventId);
        return requestMapper.toResponse(createdRequest);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestStatusResponseDto updateRequest(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @RequestBody RequestStatusUpdateDto dto) {
        return requestService.update(dto, userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponseDto cancelRequest(@PathVariable long userId,
                                            @PathVariable long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestResponseDto> findUserRequests(@PathVariable long userId) {
        return requestService.findUserRequests(userId);
    }
}