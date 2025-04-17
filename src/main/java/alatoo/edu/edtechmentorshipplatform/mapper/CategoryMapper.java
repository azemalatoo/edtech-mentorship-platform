package alatoo.edu.edtechmentorshipplatform.mapper;


import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.entity.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public static CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
