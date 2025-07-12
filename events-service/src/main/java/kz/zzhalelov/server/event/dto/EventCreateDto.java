package kz.zzhalelov.server.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreateDto {
    @NotBlank(message = "")
    @Size(min = 20, max = 2000)
    String annotation;
    Integer categoryId;
    @NotBlank(message = "Описание не заполнено")
    @Size(min = 20, max = 7000)
    String description;
    String eventDate;
    Double latitude;
    Double longitude;
    Boolean paid;
    @Size(min = 0, max = 10)
    Integer participantLimit;
    Boolean requestModeration;
    @NotBlank(message = "")
    @Size(min = 3, max = 120)
    String title;

}