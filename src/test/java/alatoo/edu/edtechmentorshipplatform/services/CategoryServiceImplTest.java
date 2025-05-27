package alatoo.edu.edtechmentorshipplatform.services;

import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;
import alatoo.edu.edtechmentorshipplatform.exception.EntityInUseException;
import alatoo.edu.edtechmentorshipplatform.exception.NotFoundException;
import alatoo.edu.edtechmentorshipplatform.repo.CategoryRepo;
import alatoo.edu.edtechmentorshipplatform.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepo repository;

    @InjectMocks
    private CategoryServiceImpl service;

    private CategoryDto dto;
    private Category entity;

    @BeforeEach
    void setUp() {
        dto = CategoryDto.builder()
                .id(null)
                .name("TestCat")
                .description("A test category")
                .build();
        entity = Category.builder()
                .id(42L)
                .name("TestCat")
                .description("A test category")
                .build();
    }

    @Test
    void createCategory_shouldSaveAndReturnDto() {
        when(repository.save(any(Category.class))).thenReturn(entity);

        CategoryDto result = service.createCategory(dto);

        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getName()).isEqualTo("TestCat");
        assertThat(result.getDescription()).isEqualTo("A test category");
        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(repository).save(captor.capture());
        Category saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo(dto.getName());
        assertThat(saved.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    void getCategoryById_whenFound_shouldReturnDto() {
        when(repository.findById(42L)).thenReturn(Optional.of(entity));

        CategoryDto result = service.getCategoryById(42L);

        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getName()).isEqualTo("TestCat");
    }

    @Test
    void getCategoryById_whenNotFound_shouldThrowNotFound() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCategoryById(42L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Category not found with id 42");
    }

    @Test
    void getAllCategories_shouldMapAll() {
        Category other = Category.builder().id(7L).name("Other").description("Desc").build();
        when(repository.findAll()).thenReturn(List.of(entity, other));

        List<CategoryDto> list = service.getAllCategories();

        assertThat(list).hasSize(2);
        assertThat(list).extracting(CategoryDto::getId).containsExactly(42L, 7L);
        assertThat(list).extracting(CategoryDto::getName).containsExactly("TestCat", "Other");
    }

    @Test
    void updateCategory_whenFound_shouldUpdateAndReturnDto() {
        Category existing = Category.builder().id(42L).name("Old").description("OldDesc").build();
        when(repository.findById(42L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setName("NewName");
        dto.setDescription("NewDesc");
        CategoryDto result = service.updateCategory(42L, dto);

        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getName()).isEqualTo("NewName");
        assertThat(result.getDescription()).isEqualTo("NewDesc");
        verify(repository).save(existing);
    }

    @Test
    void updateCategory_whenNotFound_shouldThrowNotFound() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCategory(42L, dto))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Category not found with id 42");
        verify(repository, never()).save(any());
    }

    @Test
    void deleteCategory_whenNotExists_shouldThrowNotFound() {
        when(repository.existsById(100L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteCategory(100L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage("Category not found with id 100");
    }

    @Test
    void deleteCategory_whenInUse_shouldThrowEntityInUse() {
        when(repository.existsById(42L)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("FK violation"))
            .when(repository).deleteById(42L);

        assertThatThrownBy(() -> service.deleteCategory(42L))
            .isInstanceOf(EntityInUseException.class)
            .hasMessage("Cannot delete category with id 42 because it is referenced by other entities.");
    }

    @Test
    void deleteCategory_whenExists_shouldCallDelete() {
        when(repository.existsById(42L)).thenReturn(true);

        service.deleteCategory(42L);

        verify(repository).deleteById(42L);
    }
}
