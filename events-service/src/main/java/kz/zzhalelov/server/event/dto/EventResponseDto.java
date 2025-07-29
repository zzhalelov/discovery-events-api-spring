package kz.zzhalelov.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import kz.zzhalelov.server.category.dto.CategoryResponseDto;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.user.dto.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponseDto {
    Long id;
    @NotBlank(message = "")
    @Size(min = 20, max = 2000)
    String annotation;
    UserShortDto initiator;
    CategoryResponseDto category;
    @NotBlank(message = "Описание не заполнено")
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    LocationDto location;
    Boolean paid;
    @Min(0)
    @Max(10)
    Integer participantLimit;
    Boolean requestModeration;
    @NotBlank(message = "")
    @Size(min = 3, max = 120)
    String title;
    @NotNull
    EventState state;
    Long views;
    Long confirmedRequests;
    @Min(0)
    @Max(10)
    Long participants;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
}