package kz.zzhalelov.server.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    //GET /categories/{catId}
    @GetMapping("/{catId}")
    public Category findById(@PathVariable long catId) {
        return categoryService.findById(catId);
    }

    //GET /categories
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> findAll() {
        return categoryService.findAll();
    }
}