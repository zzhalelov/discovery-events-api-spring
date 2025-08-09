package kz.zzhalelov.server.category;

import kz.zzhalelov.server.event.EventRepository;
import kz.zzhalelov.server.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long catId, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        merge(existingCategory, updatedCategory);
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }

    private void merge(Category existingCategory, Category updatedCategory) {
        if (updatedCategory.getName() != null && !updatedCategory.getName().isBlank()) {
            existingCategory.setName(updatedCategory.getName());
        }
    }
}