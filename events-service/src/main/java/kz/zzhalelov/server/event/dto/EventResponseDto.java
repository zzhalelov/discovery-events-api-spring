package kz.zzhalelov.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kz.zzhalelov.server.category.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponseDto {
    Long id;
    @NotBlank(message = "")
    @Size(min = 20, max = 2000)
    String annotation;
    Long categoryId;
    @NotBlank(message = "Описание не заполнено")
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String eventDate;
    Double latitude;
    Double longitude;
    Boolean paid;
    @Min(0)
    @Max(10)
    Integer participantLimit;
    Boolean requestModeration;
    @NotBlank(message = "")
    @Size(min = 3, max = 120)
    String title;
}
