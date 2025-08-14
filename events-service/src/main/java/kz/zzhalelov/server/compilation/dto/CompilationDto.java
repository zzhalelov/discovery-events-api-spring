package kz.zzhalelov.server.compilation.dto;

import kz.zzhalelov.server.event.Event;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for {@link kz.zzhalelov.server.compilation.Compilation}
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    Long id;
    String title;
    List<Event> events;
    Boolean pinned;
}