package kz.zzhalelov.server.hit;

import kz.zzhalelov.server.hit.dto.HitRequestDto;
import kz.zzhalelov.server.hit.dto.StatsResponseDto;

import java.util.List;

public interface StatsService {
    void saveHit(HitRequestDto dto);

    List<StatsResponseDto> getStats(String start, String end, List<String> uris, Boolean unique);
}