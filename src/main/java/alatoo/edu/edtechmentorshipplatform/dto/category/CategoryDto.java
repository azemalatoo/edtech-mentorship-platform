package alatoo.edu.edtechmentorshipplatform.dto.category;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
