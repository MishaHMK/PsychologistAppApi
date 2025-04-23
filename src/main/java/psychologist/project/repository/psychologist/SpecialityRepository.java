package psychologist.project.repository.psychologist;

import org.springframework.data.jpa.repository.JpaRepository;
import psychologist.project.model.Speciality;

public interface SpecialityRepository
        extends JpaRepository<Speciality, Long> {
}
