package kz.zzhalelov.server.request;

import kz.zzhalelov.server.request.dto.RequestCreateDto;
import kz.zzhalelov.server.request.dto.RequestMapper;
import kz.zzhalelov.server.request.dto.RequestResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestController {
    private final RequestMapper requestMapper;
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponseDto create(@RequestBody(required = false) RequestCreateDto requestCreateDto,
                                     @PathVariable long userId,
                                     @RequestParam long eventId) {
        Request request = requestMapper.fromCreate(requestCreateDto);
        Request createdRequest = requestService.create(request, userId, eventId);
        return requestMapper.toResponse(createdRequest);
    }
}