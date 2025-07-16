package kz.zzhalelov.server.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {
    Long id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
}