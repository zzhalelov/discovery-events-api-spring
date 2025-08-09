package kz.zzhalelov.server.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.zzhalelov.server.event.eventEnum.EventState;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParamEventDto {
    List<Long> userIds = new ArrayList<>();
    List<EventState> states = new ArrayList<>();
    List<Long> catIds = new ArrayList<>();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime rangeEnd;
    private int from = 0;
    private int size = 10;
}