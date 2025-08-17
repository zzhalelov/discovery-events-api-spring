package kz.zzhalelov.server.category;

import kz.zzhalelov.server.category.dto.CategoryMapper;
import kz.zzhalelov.server.category.dto.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    //GET /categories/{catId}
    @GetMapping("/{catId}")
    public CategoryResponseDto findById(@PathVariable long catId) {
        Category category = categoryService.findById(catId);
        return categoryMapper.toResponse(category);
    }

    //GET /categories
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponseDto> findAll(@RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryService.findAll(pageable)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}