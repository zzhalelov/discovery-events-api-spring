package kz.zzhalelov.server.hit.dto;

import kz.zzhalelov.server.hit.Hit;
import org.springframework.stereotype.Component;

@Component
public class HitMapper {
    public Hit fromCreate(HitRequestDto dto) {
        Hit hit = new Hit();
        hit.setApp(dto.getApp());
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());
        hit.setTimestamp(dto.getTimestamp());
        return hit;
    }

    public StatsResponseDto toResponse(String app, String uri, long hits) {
        StatsResponseDto dto = new StatsResponseDto();
        dto.setApp(app);
        dto.setUri(uri);
        dto.setHits(hits);
        return dto;
    }
}