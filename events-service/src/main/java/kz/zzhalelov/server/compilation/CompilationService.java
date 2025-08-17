package kz.zzhalelov.server.compilation;

import kz.zzhalelov.server.compilation.dto.CompilationCreateDto;
import kz.zzhalelov.server.compilation.dto.CompilationResponseDto;
import kz.zzhalelov.server.compilation.dto.CompilationUpdateDto;

import java.util.List;


public interface CompilationService {
    CompilationResponseDto create(CompilationCreateDto dto);

    Compilation findById(Long id);

    List<Compilation> findAll(Boolean pinned, int from, int size);

    void deleteById(Long compId);

    Compilation update(Long compId, CompilationUpdateDto compilationUpdateDto);
}