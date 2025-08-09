package kz.zzhalelov.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import kz.zzhalelov.server.event.eventEnum.PublicStateAction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateDto {
    @Size(min = 20, max = 2000)
    String annotation;
    Long category;
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    @Min(0)
    Long participantLimit;
    Boolean requestModeration;
    @Size(min = 3, max = 120)
    String title;
    PublicStateAction stateAction;
}