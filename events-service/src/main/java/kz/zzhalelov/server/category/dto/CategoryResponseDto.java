package kz.zzhalelov.server.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponseDto {
    Long id;
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 1, max = 50)
    String name;
}