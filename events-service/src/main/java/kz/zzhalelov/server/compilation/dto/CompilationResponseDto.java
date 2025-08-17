package kz.zzhalelov.server.compilation.dto;

import kz.zzhalelov.server.event.dto.EventShortDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO for {@link kz.zzhalelov.server.compilation.Compilation}
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationResponseDto {
    Long id;
    String title;
    List<EventShortDto> events;
    Boolean pinned;
}