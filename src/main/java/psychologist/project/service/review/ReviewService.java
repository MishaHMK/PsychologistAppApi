package psychologist.project.service.review;

import java.util.List;
import org.springframework.data.domain.Pageable;
import psychologist.project.dto.review.CreateReviewDto;
import psychologist.project.dto.review.ReviewDto;

public interface ReviewService {
    ReviewDto save(Long psychologistId, CreateReviewDto createReviewDto);

    List<ReviewDto> getAllReviewsForPsychologist(Long psychologistId, Pageable pageable);

    ReviewDto getReviewById(Long reviewId);

    void delete(Long id);
}
