package kz.zzhalelov.server.hit.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsResponseDto {
    String app;
    String uri;
    Long hits;
}