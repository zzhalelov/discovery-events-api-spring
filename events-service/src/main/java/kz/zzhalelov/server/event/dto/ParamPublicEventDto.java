package kz.zzhalelov.server.event.dto;

import kz.zzhalelov.server.event.eventEnum.EventSortParams;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParamPublicEventDto {
    String text;
    List<Long> categories;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeEnd;
    Boolean onlyAvailable = false;
    Boolean paid;
    EventSortParams sort;
    Integer from = 0;
    Integer size = 10;
}