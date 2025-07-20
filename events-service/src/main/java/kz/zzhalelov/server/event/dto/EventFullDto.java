package kz.zzhalelov.server.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.zzhalelov.server.category.dto.CategoryDto;
import kz.zzhalelov.server.event.EventState;
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
    CategoryDto category;
    Integer confirmedRequests;
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
    Integer participantLimit;
    String publishedOn;
    Boolean requestModeration;
    EventState state;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
    Integer views;
}