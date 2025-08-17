package kz.zzhalelov.server.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationUpdateDto {
    @Size(max = 50)
    String title;
    Boolean pinned;
    List<Long> events;
}