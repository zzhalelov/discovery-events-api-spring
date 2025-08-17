package kz.zzhalelov.server.category;

import jakarta.validation.Valid;
import kz.zzhalelov.server.category.dto.CategoryCreateDto;
import kz.zzhalelov.server.category.dto.CategoryMapper;
import kz.zzhalelov.server.category.dto.CategoryResponseDto;
import kz.zzhalelov.server.category.dto.CategoryUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        Category category = categoryMapper.fromCreate(categoryCreateDto);
        return categoryMapper.toResponse(categoryService.create(category));
    }

    //PATCH /admin/categories/{catId}
    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto update(@PathVariable long catId,
                                      @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        Category category = categoryMapper.fromUpdate(categoryUpdateDto);
        return categoryMapper.toResponse(categoryService.update(catId, category));
    }

    //DELETE /admin/categories/{catId}
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long catId) {
        categoryService.delete(catId);
    }
}