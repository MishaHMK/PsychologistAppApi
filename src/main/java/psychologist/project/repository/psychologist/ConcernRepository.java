package psychologist.project.repository.psychologist;

import org.springframework.data.jpa.repository.JpaRepository;
import psychologist.project.model.Concern;

public interface ConcernRepository extends JpaRepository<Concern, Long> {
}
