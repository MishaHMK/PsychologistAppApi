package psychologist.project.repository.reviews;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import psychologist.project.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"user", "psychologist"})
    Page<Review> findAllByPsychologistId(Long psychologistId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "psychologist"})
    @Query("SELECT r FROM Review r "
            + "JOIN FETCH r.psychologist p "
            + "JOIN FETCH r.user u "
            + "WHERE p.id = :psychologistId "
            + "ORDER BY r.reviewDate DESC ")
    List<Review> findLatestByPsychologistId(Long psychologistId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "psychologist"})
    Review findFirstById(Long reviewId);
}
