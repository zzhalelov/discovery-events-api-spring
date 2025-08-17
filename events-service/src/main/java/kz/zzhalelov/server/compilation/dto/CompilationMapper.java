package kz.zzhalelov.server.compilation.dto;

import kz.zzhalelov.server.compilation.Compilation;
import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.dto.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation fromCreate(CompilationCreateDto dto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.getPinned() != null ? dto.getPinned() : false);
        compilation.setEvents(events);
        return compilation;
    }

    public CompilationResponseDto toResponse(Compilation compilation) {
        CompilationResponseDto dto = new CompilationResponseDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());
        dto.setEvents(
                compilation.getEvents().stream()
                        .map(eventMapper::toShortDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}