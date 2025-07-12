package kz.zzhalelov.server.category;

import jakarta.validation.Valid;
import kz.zzhalelov.server.category.dto.CategoryCreateDto;
import kz.zzhalelov.server.category.dto.CategoryMapper;
import kz.zzhalelov.server.category.dto.CategoryResponseDto;
import kz.zzhalelov.server.category.dto.CategoryUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CategoryController {
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    //POST
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        Category category = categoryMapper.fromCreate(categoryCreateDto);
        return categoryMapper.toResponse(categoryService.create(category));
    }

    //PATCH /admin/categories/{catId}
    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto update(@PathVariable long catId,
                                      @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        Category category = categoryMapper.fromUpdate(categoryUpdateDto);
        return categoryMapper.toResponse(categoryService.update(catId, category));
    }

    //DELETE /admin/categories/{catId}
    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByid(@PathVariable long catId) {
        categoryService.delete(catId);
    }

    //GET /categories
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    //GET /categories/{catId}
    @GetMapping("/categories/{catId}")
    public Category findById(@PathVariable long catId) {
        return categoryService.findById(catId);
    }
}