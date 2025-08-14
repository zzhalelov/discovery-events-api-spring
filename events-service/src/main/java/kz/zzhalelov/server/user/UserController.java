package kz.zzhalelov.server.user;

import jakarta.validation.Valid;
import kz.zzhalelov.server.user.dto.UserCreateDto;
import kz.zzhalelov.server.user.dto.UserMapper;
import kz.zzhalelov.server.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody @Valid UserCreateDto userCreateDto) {
        User user = userMapper.fromCreate(userCreateDto);
        return userMapper.toResponse(userService.create(user));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> findAll(@RequestParam(required = false) List<Long> ids,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        if (ids != null && !ids.isEmpty()) {
            return userService.findAll(ids).stream()
                    .map(userMapper::toResponse)
                    .collect(Collectors.toList());
        } else {
            Pageable pageable = PageRequest.of(from / size, size);
            return userService.findAll(pageable)
                    .stream()
                    .map(userMapper::toResponse)
                    .collect(Collectors.toList());
        }
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long userId) {
        userService.delete(userId);
    }
}