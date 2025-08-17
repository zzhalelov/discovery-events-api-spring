package kz.zzhalelov.server.compilation;

import kz.zzhalelov.server.compilation.dto.CompilationCreateDto;
import kz.zzhalelov.server.compilation.dto.CompilationMapper;
import kz.zzhalelov.server.compilation.dto.CompilationResponseDto;
import kz.zzhalelov.server.compilation.dto.CompilationUpdateDto;
import kz.zzhalelov.server.event.Event;
import kz.zzhalelov.server.event.EventRepository;
import kz.zzhalelov.server.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    public CompilationServiceImpl(CompilationRepository compilationRepository,
                                  EventRepository eventRepository,
                                  CompilationMapper compilationMapper) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public CompilationResponseDto create(CompilationCreateDto dto) {
        List<Event> events = dto.getEvents() == null || dto.getEvents().isEmpty()
                ? Collections.emptyList()
                : eventRepository.findAllById(dto.getEvents());
        Compilation compilation = compilationMapper.fromCreate(dto, events);
        return compilationMapper.toResponse(compilationRepository.save(compilation));
    }

    @Override
    public Compilation findById(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пожборка не найдена"));
    }

    @Override
    public List<Compilation> findAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageable).getContent();
        }
        return compilationRepository.findAll(pageable).getContent();
    }

    @Override
    public void deleteById(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Подюорка не найдена");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public Compilation update(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation existingCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подюорка не найдена"));
        merge(existingCompilation, compilationUpdateDto);
        return compilationRepository.save(existingCompilation);
    }

    private void merge(Compilation existing, CompilationUpdateDto updateDto) {
        if (updateDto.getTitle() != null && !updateDto.getTitle().isBlank()) {
            existing.setTitle(updateDto.getTitle());
        }
        if (updateDto.getPinned() != null) {
            existing.setPinned(updateDto.getPinned());
        }
        if (updateDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateDto.getEvents());
            existing.setEvents(events);
        }
    }
}