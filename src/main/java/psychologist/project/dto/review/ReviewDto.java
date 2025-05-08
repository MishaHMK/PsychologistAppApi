package psychologist.project.dto.review;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ReviewDto {
    private int id;
    private String reviewText;
    private String reviewerName;
    private Integer reviewerAge;
    private Integer rate;
    private Integer sessionsCount;
    private LocalDate reviewDate;
    private Long psychologistId;
    private Long userId;
}
