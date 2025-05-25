package alatoo.edu.edtechmentorshipplatform.services.impl;

import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;
import alatoo.edu.edtechmentorshipplatform.exception.EntityInUseException;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.mapper.CategoryMapper;
import alatoo.edu.edtechmentorshipplatform.repo.CategoryRepo;
import alatoo.edu.edtechmentorshipplatform.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo repository;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category saved = repository.save(CategoryMapper.toEntity(dto));
        return CategoryMapper.toDto(saved);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category cat = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category not found with id %d", id)
                ));
        return CategoryMapper.toDto(cat);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category not found with id %d", id)
                ));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        Category updated = repository.save(existing);
        return CategoryMapper.toDto(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(
                    String.format("Category not found with id %d", id)
            );
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityInUseException(
                    String.format("Cannot delete category with id %d because it is referenced by other entities.", id)
            );
        }
    }
}
