package kz.zzhalelov.server.compilation;

import kz.zzhalelov.server.compilation.dto.CompilationMapper;
import kz.zzhalelov.server.compilation.dto.CompilationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationMapper compilationMapper;
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationResponseDto findById(@PathVariable Long compId) {
        Compilation compilation = compilationService.findById(compId);
        return compilationMapper.toResponse(compilation);
    }

    @GetMapping
    public List<CompilationResponseDto> findAll(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return compilationService.findAll(pinned, from, size)
                .stream()
                .map(compilationMapper::toResponse)
                .collect(Collectors.toList());
    }
}