package alatoo.edu.edtechmentorshipplatform.controller;

import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageRequestDto;
import alatoo.edu.edtechmentorshipplatform.dto.tutoringPackage.TutoringPackageResponseDto;
import alatoo.edu.edtechmentorshipplatform.services.TutoringPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tutoring-packages")
@RequiredArgsConstructor
public class TutoringPackageController {

    private final TutoringPackageService tutoringPackageService;

    @Operation(summary = "Create a new tutoring package")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutoring Package created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TutoringPackageResponseDto> create(@RequestBody TutoringPackageRequestDto tutoringPackageRequestDto) {
        TutoringPackageResponseDto createdPackage = tutoringPackageService.create(tutoringPackageRequestDto);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing tutoring package")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutoring Package updated successfully"),
            @ApiResponse(responseCode = "404", description = "Tutoring Package not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TutoringPackageResponseDto> update(@PathVariable UUID id, @RequestBody TutoringPackageRequestDto tutoringPackageRequestDto) {
        TutoringPackageResponseDto updatedPackage = tutoringPackageService.update(id, tutoringPackageRequestDto);
        return new ResponseEntity<>(updatedPackage, HttpStatus.OK);
    }

    @Operation(summary = "Delete a tutoring package")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tutoring Package deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tutoring Package not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tutoringPackageService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get a tutoring package by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutoring Package found"),
            @ApiResponse(responseCode = "404", description = "Tutoring Package not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TutoringPackageResponseDto> getById(@PathVariable UUID id) {
        TutoringPackageResponseDto tutoringPackage = tutoringPackageService.getById(id);
        return new ResponseEntity<>(tutoringPackage, HttpStatus.OK);
    }

    @Operation(summary = "Get all tutoring packages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all Tutoring Packages")
    })
    @GetMapping
    public ResponseEntity<List<TutoringPackageResponseDto>> getAll() {
        List<TutoringPackageResponseDto> tutoringPackages = tutoringPackageService.getAll();
        return new ResponseEntity<>(tutoringPackages, HttpStatus.OK);
    }
}
