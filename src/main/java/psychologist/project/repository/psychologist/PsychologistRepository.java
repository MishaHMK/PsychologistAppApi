package psychologist.project.repository.psychologist;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import psychologist.project.model.Psychologist;

public interface PsychologistRepository
        extends JpaRepository<Psychologist, Long>,
                JpaSpecificationExecutor<Psychologist> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"speciality", "approaches", "concerns"})
    Optional<Psychologist> findById(@NonNull Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"speciality", "approaches", "concerns"})
    Page<Psychologist> findAll(@Nullable Specification<Psychologist> spec,
                               Pageable pageable);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"speciality", "approaches", "concerns"})
    Page<Psychologist> findAll(Pageable pageable);
}
