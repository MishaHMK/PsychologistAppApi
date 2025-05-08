package psychologist.project.dto.review;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewDto {
    private String reviewText;
    @NotNull(message = "Review score is required")
    private Integer rate;
}
