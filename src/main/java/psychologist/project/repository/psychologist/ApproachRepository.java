package psychologist.project.repository.psychologist;

import org.springframework.data.jpa.repository.JpaRepository;
import psychologist.project.model.Approach;

public interface ApproachRepository extends JpaRepository<Approach, Long> {
}
