package kz.zzhalelov.server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 250)
    String name;
    @NotBlank(message = "email обязателен")
    @Size(min = 6, max = 254, message = "Email должен содержать от 6 до 254 символов")
    @Email(message = "Некорректный email")
    String email;
}