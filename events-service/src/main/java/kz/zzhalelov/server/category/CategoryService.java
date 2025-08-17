package kz.zzhalelov.server.category;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Category create(Category category);

    Category update(Long catId, Category updatedCategory);

    void delete(Long catId);

    List<Category> findAll(Pageable pageable);

    Category findById(Long catId);
}