package kz.zzhalelov.server.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationCreateDto {
    Boolean pinned = false;
    @NotBlank(message = "Название обязательно")
    @Size(max = 50, message = "Размер подборки не должен превыщать 50")
    String title;
    List<Long> events;
}