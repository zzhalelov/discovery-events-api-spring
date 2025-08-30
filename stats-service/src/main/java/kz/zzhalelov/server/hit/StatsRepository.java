package kz.zzhalelov.server.hit;

import kz.zzhalelov.server.hit.dto.StatsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    List<StatsResponseDto> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<Hit> findByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);
}