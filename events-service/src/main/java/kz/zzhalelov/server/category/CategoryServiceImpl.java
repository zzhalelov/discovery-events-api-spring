package kz.zzhalelov.server.category;

import kz.zzhalelov.server.event.EventRepository;
import kz.zzhalelov.server.exception.ConflictException;
import kz.zzhalelov.server.exception.NotFoundException;
import org.springframework.data.domain.Pageable;
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
        categoryRepository.findCategoryByName(category.getName())
                .filter(cat -> !cat.getId().equals(category.getId()))
                .ifPresent(cat -> {
                    throw new ConflictException("Категория с таким именем уже существует");
                });
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long catId, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        categoryRepository.findCategoryByName(updatedCategory.getName())
                .filter(cat -> !cat.getId().equals(catId))
                .ifPresent(cat -> {
                    throw new ConflictException("Категория с таким именем уже существует");
                });
        merge(existingCategory, updatedCategory);
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        boolean hasEvents = eventRepository.existsByCategoryId(catId);
        if (hasEvents) {
            throw new ConflictException("Нельзя удалить категорию с привязанными событиями");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).getContent();
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