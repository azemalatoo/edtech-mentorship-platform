package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.controller.base.BaseRestController;
import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageResponseDto;
import alatoo.edu.edtechmentorshipplatform.util.ResponseApi;
import alatoo.edu.edtechmentorshipplatform.util.ResponseCode;
import alatoo.edu.edtechmentorshipplatform.services.TutoringPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tutoring-packages")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "APIs for managing tutoring packages")
public class TutoringPackageController extends BaseRestController {

    private final TutoringPackageService tutoringPackageService;

    @Operation(summary = "Create a new tutoring package")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tutoring Package created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseApi<TutoringPackageResponseDto> create(
            @Valid @RequestBody TutoringPackageRequestDto dto) {
        TutoringPackageResponseDto result = tutoringPackageService.create(dto);
        return new ResponseApi<>(result, ResponseCode.CREATED);
    }

    @Operation(summary = "Update an existing tutoring package")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tutoring Package updated successfully"),
            @ApiResponse(responseCode = "404", description = "Tutoring Package not found")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<TutoringPackageResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TutoringPackageRequestDto dto) {
        TutoringPackageResponseDto result = tutoringPackageService.update(id, dto);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Delete a tutoring package")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tutoring Package deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tutoring Package not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<Void> delete(@PathVariable Long id) {
        tutoringPackageService.delete(id);
        return new ResponseApi<>(null, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get a tutoring package by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tutoring Package found"),
            @ApiResponse(responseCode = "404", description = "Tutoring Package not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<TutoringPackageResponseDto> getById(@PathVariable Long id) {
        TutoringPackageResponseDto result = tutoringPackageService.getById(id);
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get all tutoring packages")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of all Tutoring Packages")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseApi<List<TutoringPackageResponseDto>> getAll() {
        List<TutoringPackageResponseDto> result = tutoringPackageService.getAll();
        return new ResponseApi<>(result, ResponseCode.SUCCESS);
    }
}
