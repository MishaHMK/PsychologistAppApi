package psychologist.project.repository.psychologist;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import psychologist.project.model.Psychologist;

import java.util.List;
import java.util.Optional;

public interface PsychologistRepository
        extends JpaRepository<Psychologist, Long> {
    @Override
    @EntityGraph(attributePaths = "speciality")
    Optional<Psychologist> findById(Long id);
}
