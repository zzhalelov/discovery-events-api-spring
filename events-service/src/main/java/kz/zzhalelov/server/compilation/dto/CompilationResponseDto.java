package kz.zzhalelov.server.compilation.dto;

import kz.zzhalelov.server.event.Event;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationResponseDto {
    Long id;
    List<Event> events;
    boolean pinned;
    String title;
}