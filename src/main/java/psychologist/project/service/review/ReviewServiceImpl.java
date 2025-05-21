package psychologist.project.service.review;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psychologist.project.dto.booking.BookingDto;
import psychologist.project.dto.review.CreateReviewDto;
import psychologist.project.dto.review.ReviewDto;
import psychologist.project.exception.AccessException;
import psychologist.project.mapper.ReviewMapper;
import psychologist.project.model.Booking.BookingStatus;
import psychologist.project.model.Psychologist;
import psychologist.project.model.Review;
import psychologist.project.model.User;
import psychologist.project.repository.reviews.ReviewRepository;
import psychologist.project.security.SecurityUtil;
import psychologist.project.service.booking.BookingService;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final int reviewPageSize = 6;

    private final ReviewMapper reviewMapper;
    private final BookingService bookingService;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto save(Long psychologistId, CreateReviewDto createReviewDto) {
        Review review = reviewMapper.toEntity(createReviewDto);
        User loggedInUser = SecurityUtil.getLoggedInUser();
        List<BookingDto> allUserPsychologistsBookings =
                getUserPsychologistsPreviousBookings(psychologistId, loggedInUser.getId());
        review.setUser(loggedInUser)
                .setPsychologist(new Psychologist().setId(psychologistId))
                .setSessionsCount(allUserPsychologistsBookings.size())
                .setReviewDate(LocalDate.now());
        reviewRepository.save(review);
        ReviewDto dto = reviewMapper.toDto(review);
        dto.setReviewerAge(Period.between(loggedInUser.getBirthDate(),
                LocalDate.now()).getYears())
                .setReviewerName(loggedInUser.getFirstName());
        return dto;
    }

    @Override
    public List<ReviewDto> getAllReviewsForPsychologist(Long psychologistId, Pageable pageable) {
        return reviewRepository.findAllByPsychologistId(psychologistId, pageable)
                .stream()
                .map(review -> reviewMapper.toDto(review)
                        .setReviewerAge(Period.between(review.getUser().getBirthDate(),
                                LocalDate.now()).getYears())
                        .setReviewerName(review.getUser().getFirstName()))
                .toList();
    }

    @Cacheable(
            value = "recentReviewsCache"
    )
    @Override
    public List<ReviewDto> getRecentReviewsForPsychologist(Long psychologistId) {
        return reviewRepository.findLatestByPsychologistId(
                psychologistId, PageRequest.of(0, reviewPageSize))
                .stream()
                .map(review -> reviewMapper.toDto(review)
                            .setReviewerAge(Period.between(review.getUser().getBirthDate(),
                                    LocalDate.now()).getYears())
                            .setReviewerName(review.getUser().getFirstName()))
                .toList();
    }

    @Override
    public ReviewDto getReviewById(Long reviewId) {
        Review firstById = reviewRepository.findFirstById(reviewId);
        return reviewMapper.toDto(firstById)
                .setReviewerAge(Period.between(firstById.getUser().getBirthDate(),
                        LocalDate.now()).getYears())
                .setReviewerName(firstById.getUser().getFirstName());
    }

    @Override
    public void delete(Long id) {
        ReviewDto toDelete = getReviewById(id);
        User loggedInUser = SecurityUtil.getLoggedInUser();
        if (loggedInUser.getRole() == User.Role.ADMIN
                || toDelete.getUserId().equals(loggedInUser.getId())) {
            throw new AccessException("You can't access this review");
        }
        reviewRepository.deleteById(id);
    }

    private List<BookingDto> getUserPsychologistsPreviousBookings(
            Long psychologistId, Long userId) {
        return bookingService.getAllUserPsychologistsBookings(
                                psychologistId, userId)
                        .stream()
                        .filter(b -> (b.getStatus().equals(BookingStatus.PENDING.toString())
                                || b.getStatus().equals(BookingStatus.CONFIRMED.toString()))
                                && b.getStartTime().isBefore(LocalDateTime.now()))
                        .toList();
    }
}
