package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.category.CategoryDto;
import alatoo.edu.edtechmentorshipplatform.services.CategoryService;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "CategoryController", description = "APIs for managing categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @Operation(summary = "Create a new category")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<CategoryDto> create(
            @Valid @RequestBody CategoryDto request) {
        CategoryDto dto = service.createCategory(request);
        return new ResponseApi<>(dto, ResponseCode.CREATED);
    }

    @Operation(summary = "Get a category by ID")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @GetMapping("/{id}")
    public ResponseApi<CategoryDto> getById(@PathVariable Long id) {
        return new ResponseApi<>(service.getCategoryById(id), ResponseCode.SUCCESS);
    }

    @Operation(summary = "List all categories")
    @ApiResponse(responseCode = "200", description = "Categories listed successfully")
    @GetMapping
    public ResponseApi<List<CategoryDto>> listAll() {
        return new ResponseApi<>(service.getAllCategories(), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Update an existing category")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @PutMapping("/{id}")
    public ResponseApi<CategoryDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDto request) {
        return new ResponseApi<>(service.updateCategory(id, request), ResponseCode.SUCCESS);
    }

    @Operation(summary = "Delete a category")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseApi<Void> delete(@PathVariable Long id) {
        service.deleteCategory(id);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }
}
