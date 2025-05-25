package alatoo.edu.edtechmentorshipplatform.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
