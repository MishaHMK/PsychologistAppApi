package psychologist.project.repository.reviews;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import psychologist.project.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"user", "psychologist"})
    Page<Review> findAllByPsychologistId(Long psychologistId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "psychologist"})
    Review findFirstById(Long reviewId);
}
