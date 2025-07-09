package kz.zzhalelov.server.user;

import jakarta.validation.Valid;
import kz.zzhalelov.server.user.dto.UserCreateDto;
import kz.zzhalelov.server.user.dto.UserMapper;
import kz.zzhalelov.server.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
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
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto create(@RequestBody @Valid UserCreateDto userCreateDto) {
        User user = userMapper.fromCreate(userCreateDto);
        return userMapper.toResponse(userService.create(user));
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return userService.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable int userId) {
        userService.delete(userId);
    }
}