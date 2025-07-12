package kz.zzhalelov.server.category;

import java.util.List;

public interface CategoryService {
    Category create(Category category);

    Category update(Long catId, Category updatedCategory);

    void delete(Long catId);

    List<Category> findAll();

    Category findById(Long catId);
}