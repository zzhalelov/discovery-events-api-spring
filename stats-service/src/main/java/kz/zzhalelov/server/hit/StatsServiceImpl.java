package kz.zzhalelov.server.hit;

import kz.zzhalelov.server.hit.dto.HitMapper;
import kz.zzhalelov.server.hit.dto.HitRequestDto;
import kz.zzhalelov.server.hit.dto.StatsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @Override
    public void saveHit(HitRequestDto dto) {
        statsRepository.save(hitMapper.fromCreate(dto));
    }

    @Override
    public List<StatsResponseDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

//        if (unique) {
//            return statsRepository.findByTimestampBetweenAndUriIn(startDate, endDate, uris);
//        } else {
//            return statsRepository.findByTimestampBetween(startDate, endDate);
//        }
//        return ;
    }
}