package kz.zzhalelov.server.compilation;

import jakarta.validation.Valid;
import kz.zzhalelov.server.compilation.dto.CompilationCreateDto;
import kz.zzhalelov.server.compilation.dto.CompilationMapper;
import kz.zzhalelov.server.compilation.dto.CompilationResponseDto;
import kz.zzhalelov.server.compilation.dto.CompilationUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto create(@Valid @RequestBody CompilationCreateDto dto) {
        return compilationService.create(dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationService.deleteById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationResponseDto update(@PathVariable Long compId,
                                         @Valid @RequestBody CompilationUpdateDto dto) {
        Compilation updatedCompilation = compilationService.update(compId, dto);
        return compilationMapper.toResponse(updatedCompilation);
    }
}