package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import psychologist.project.dto.review.CreateReviewDto;
import psychologist.project.dto.review.ReviewDto;
import psychologist.project.service.review.ReviewService;

@Tag(name = "Reviews controller",
        description = "Reviews management endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    @Operation(summary = "Create new review",
            description = "Create review with given data")
    public ReviewDto createReview(
            @Valid @RequestBody CreateReviewDto createDto,
            @RequestParam Long psychologistId) {
        return reviewService.save(psychologistId, createDto);
    }

    @GetMapping("/all/{psychologistId}")
    @Operation(summary = "Get all psychologist reviews",
            description = "Get all reviews by psychologist id")
    public List<ReviewDto> getAllReviewsForPsychologist(
            @PathVariable Long psychologistId,
            @ParameterObject Pageable pageable) {
        return reviewService.getAllReviewsForPsychologist(psychologistId, pageable);
    }

    @GetMapping("/review-page/{psychologistId}")
    @Operation(summary = "Get reviews for psychologist page",
            description = "Get most recent reviews of psychologist, no more than 6")
    public List<ReviewDto> getAllReviewsForPsychologistPage(
            @PathVariable Long psychologistId) {
        return reviewService.getRecentReviewsForPsychologist(psychologistId);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "Get psychologist review",
            description = "Get specific psychologist review by id")
    public ReviewDto getAllReviewById(
            @PathVariable Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete psychologist review",
            description = "Delete specific psychologist review by id")
    public void deleteReviewById(
            @PathVariable Long reviewId) {
        reviewService.delete(reviewId);
    }
}
