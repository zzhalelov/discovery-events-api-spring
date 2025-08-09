package kz.zzhalelov.server.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.zzhalelov.server.category.dto.CategoryResponseDto;
import kz.zzhalelov.server.event.eventEnum.EventState;
import kz.zzhalelov.server.user.dto.UserShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    CategoryResponseDto category;
    Long confirmedRequests;
    String createdOn;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @NotBlank
    String eventDate;
    @NotNull
    UserShortDto initiator;
    @NotNull
    Double lat;
    @NotNull
    Double lon;
    @NotNull
    Boolean paid;
    Long participantLimit;
    String publishedOn;
    Boolean requestModeration;
    EventState state;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
    Long views;
}