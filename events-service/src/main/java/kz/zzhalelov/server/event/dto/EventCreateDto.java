package kz.zzhalelov.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreateDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull(message = "Категория обязательна")
    Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    LocationDto location;
    @NotNull
    Boolean paid;
    @Min(0)
    Long participantLimit;
    Boolean requestModeration;
    @NotBlank(message = "Заголовок не заполнен")
    @Size(min = 3, max = 120)
    String title;
}