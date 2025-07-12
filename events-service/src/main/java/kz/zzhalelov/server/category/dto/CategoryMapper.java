package kz.zzhalelov.server.category.dto;

import kz.zzhalelov.server.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category fromCreate(CategoryCreateDto categoryCreateDto) {
        Category category = new Category();
        category.setName(categoryCreateDto.getName());
        return category;
    }

    public CategoryResponseDto toResponse(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        return categoryResponseDto;
    }

    public Category fromUpdate(CategoryUpdateDto categoryUpdateDto) {
        Category category = new Category();
        category.setName(categoryUpdateDto.getName());
        return category;
    }
}