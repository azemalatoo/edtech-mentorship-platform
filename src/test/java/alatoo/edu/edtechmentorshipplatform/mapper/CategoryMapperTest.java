package alatoo.edu.edtechmentorshipplatform.mapper;

import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {

    @Test
    void toEntity_shouldMapAllFields() {
        CategoryDto dto = new CategoryDto();
        dto.setId(42L);
        dto.setName("Data Science");
        dto.setDescription("All things data");

        Category entity = CategoryMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(42L);
        assertThat(entity.getName()).isEqualTo("Data Science");
        assertThat(entity.getDescription()).isEqualTo("All things data");
    }

    @Test
    void toEntity_shouldHandleNullDto() {
        org.junit.jupiter.api.Assertions.assertThrows(
            NullPointerException.class,
            () -> CategoryMapper.toEntity(null)
        );
    }

    @Test
    void toDto_shouldMapAllFields() {
        Category entity = Category.builder()
            .id(7L)
            .name("Java")
            .description("JVM language")
            .build();

        CategoryDto dto = CategoryMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(7L);
        assertThat(dto.getName()).isEqualTo("Java");
        assertThat(dto.getDescription()).isEqualTo("JVM language");
    }

    @Test
    void toDto_shouldHandleNullEntity() {
        org.junit.jupiter.api.Assertions.assertThrows(
            NullPointerException.class,
            () -> CategoryMapper.toDto(null)
        );
    }
}
