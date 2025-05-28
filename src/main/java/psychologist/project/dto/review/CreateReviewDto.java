package psychologist.project.dto.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReviewDto {
    @Size(max = 2000, message = "No more than 2000 symbols in review")
    private String reviewText;
    @NotNull(message = "Review score is required")
    private Integer rate;
}
